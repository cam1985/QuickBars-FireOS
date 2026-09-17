package dev.trooped.tvquickbars

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import com.revenuecat.purchases.interfaces.UpdatedCustomerInfoListener
import dev.trooped.tvquickbars.data.AppIdProvider
import dev.trooped.tvquickbars.persistence.AppPrefs
import dev.trooped.tvquickbars.persistence.SecurePrefsManager
import dev.trooped.tvquickbars.utils.DemoModeManager
import dev.trooped.tvquickbars.utils.PlusStatusManager
import dev.trooped.tvquickbars.utils.SecureStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Application class that provides global context access.
 * Does not change app navigation flow - SetupActivity will still be the first screen shown.
 */
class QuickBarsApp : Application(), Application.ActivityLifecycleCallbacks {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var currentActivity: Activity? = null
    private var revenueCatConfigured = false

    override fun onCreate() {
        super.onCreate()
        instance = this

        AppPrefs.ensureShowToastOnEntityTriggerDefault(this)

        appScope.launch {
            SecureStore.getHAToken(this@QuickBarsApp)
            DemoModeManager.checkAndEnableDemoMode(this@QuickBarsApp)
        }
        registerActivityLifecycleCallbacks(this)

        // During Fire OS beta all Plus features are unlocked and Amazon IAP is deliberately
        // disabled. Keep RevenueCat available in source for easy upstream merges, but do not
        // initialise Google-store purchasing on the Fire beta path.
        if (!BuildConfig.FIRE_BETA_UNLOCK_PLUS) {
            Purchases.configure(
                PurchasesConfiguration.Builder(this, BuildConfig.REVENUECAT_API_KEY).build()
            )
            revenueCatConfigured = true
            Purchases.sharedInstance.updatedCustomerInfoListener =
                UpdatedCustomerInfoListener { info: CustomerInfo ->
                    PlusStatusManager.update(info)
                }
        }

        AppIdProvider.ensure(applicationContext)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

    override fun onTerminate() {
        if (revenueCatConfigured) {
            Purchases.sharedInstance.updatedCustomerInfoListener = null
        }
        super.onTerminate()
    }

    override fun onActivityDestroyed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) {
        if (currentActivity === activity) currentActivity = null
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit

    companion object {
        private lateinit var instance: QuickBarsApp

        var showToastOnEntityTrigger: Boolean
            get() = AppPrefs.isShowToastOnEntityTriggerEnabled(instance.applicationContext)
            set(value) = AppPrefs.setShowToastOnEntityTriggerEnabled(instance.applicationContext, value)

        fun getAppContext(): Context = instance.applicationContext
    }

    private fun clearHaCredentials() {
        SecurePrefsManager.clearCredentials(this)
        Toast.makeText(this, "Home Assistant credentials cleared", Toast.LENGTH_SHORT).show()
    }
}
