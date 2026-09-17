package dev.trooped.tvquickbars.billing

import android.content.Context
import android.util.Log
import com.amazon.device.iap.PurchasingListener
import com.amazon.device.iap.PurchasingService
import com.amazon.device.iap.model.ProductDataResponse
import com.amazon.device.iap.model.PurchaseResponse
import com.amazon.device.iap.model.PurchaseUpdatesResponse
import com.amazon.device.iap.model.Receipt
import com.amazon.device.iap.model.UserDataResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Amazon Appstore implementation for the existing one-time QuickBars Plus unlock.
 *
 * This provider is intentionally not selected while FIRE_BETA_UNLOCK_PLUS is true.
 * Receipt Verification Service (RVS) hardening is required before production enablement.
 */
class AmazonEntitlementProvider : EntitlementProvider, PurchasingListener {
    private val plus = MutableStateFlow(false)
    override val isPlus: StateFlow<Boolean> = plus

    private var refreshSawActivePlus = false

    override fun start(context: Context) {
        PurchasingService.registerListener(context.applicationContext, this)
    }

    override fun refresh() {
        refreshSawActivePlus = false
        PurchasingService.getUserData()
        PurchasingService.getProductData(setOf(PLUS_SKU))
        PurchasingService.getPurchaseUpdates(true)
    }

    override fun purchasePlus() {
        PurchasingService.purchase(PLUS_SKU)
    }

    override fun onUserDataResponse(response: UserDataResponse) {
        Log.d(TAG, "Amazon user-data status=${response.requestStatus}")
    }

    override fun onProductDataResponse(response: ProductDataResponse) {
        Log.d(TAG, "Amazon product-data status=${response.requestStatus}")
    }

    override fun onPurchaseResponse(response: PurchaseResponse) {
        when (response.requestStatus) {
            PurchaseResponse.RequestStatus.SUCCESSFUL,
            PurchaseResponse.RequestStatus.ALREADY_PURCHASED -> {
                plus.value = response.receipt.isActivePlusReceipt()
                if (!plus.value) refresh()
            }
            else -> Log.w(TAG, "Amazon purchase status=${response.requestStatus}")
        }
    }

    override fun onPurchaseUpdatesResponse(response: PurchaseUpdatesResponse) {
        when (response.requestStatus) {
            PurchaseUpdatesResponse.RequestStatus.SUCCESSFUL -> {
                if (response.receipts.any { it.isActivePlusReceipt() }) {
                    refreshSawActivePlus = true
                }
                if (response.hasMore()) {
                    PurchasingService.getPurchaseUpdates(false)
                } else {
                    plus.value = refreshSawActivePlus
                    refreshSawActivePlus = false
                }
            }
            else -> Log.w(TAG, "Amazon purchase-updates status=${response.requestStatus}")
        }
    }

    private fun Receipt?.isActivePlusReceipt(): Boolean =
        this != null && sku == PLUS_SKU && !isCanceled

    companion object {
        const val PLUS_SKU = "plus_unlock"
        private const val TAG = "AmazonEntitlement"
    }
}
