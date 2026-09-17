package dev.trooped.tvquickbars.utils

import android.util.Log
import com.revenuecat.purchases.CustomerInfo
import dev.trooped.tvquickbars.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * PlusStatusManager
 * Determines Plus status during runtime while preserving upstream semantics.
 * Fire OS beta builds force Plus enabled until Amazon purchasing is enabled.
 */
object PlusStatusManager {
    private val _isPlus = MutableStateFlow(BuildConfig.FIRE_BETA_UNLOCK_PLUS)
    val isPlus = _isPlus.asStateFlow()

    fun update(info: CustomerInfo) {
        _isPlus.value = if (BuildConfig.FIRE_BETA_UNLOCK_PLUS) {
            true
        } else {
            info.entitlements["plus"]?.isActive == true
        }
        Log.d("PlusStatus", "Entitlement refresh → isPlus=${_isPlus.value}")
    }
}
