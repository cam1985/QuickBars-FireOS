package dev.trooped.tvquickbars.billing

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** POC/beta provider: mirrors Plus behavior with every premium feature unlocked. */
object BetaUnlockedEntitlementProvider : EntitlementProvider {
    private val unlocked = MutableStateFlow(true)
    override val isPlus: StateFlow<Boolean> = unlocked

    override fun start(context: Context) = Unit
    override fun refresh() = Unit
    override fun purchasePlus() = Unit
}
