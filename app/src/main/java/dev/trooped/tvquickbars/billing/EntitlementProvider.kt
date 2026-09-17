package dev.trooped.tvquickbars.billing

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

/** Store-neutral seam for QuickBars' existing Plus entitlement. */
interface EntitlementProvider {
    val isPlus: StateFlow<Boolean>

    fun start(context: Context)
    fun refresh()
    fun purchasePlus()
}
