package dev.trooped.tvquickbars.platform

import android.view.InputDevice
import android.view.KeyEvent

/** Stable snapshot of useful InputDevice identity/capability fields. */
data class FireRemoteIdentity(
    val name: String,
    val vendorId: Int,
    val productId: Int,
    val descriptor: String,
    val sources: Int,
    val keyboardType: Int,
)

/**
 * Internal hardware compatibility profile. Profiles normalize hardware quirks only;
 * upstream QuickBars remains responsible for trigger assignment and press gestures.
 */
interface FireRemoteProfile {
    val id: String
    fun matches(identity: FireRemoteIdentity): Boolean
    fun normalizeKeyCode(keyCode: Int): Int = keyCode
}

private object StandardAmazonRemoteProfile : FireRemoteProfile {
    override val id: String = "amazon-fire-remote-standard"

    override fun matches(identity: FireRemoteIdentity): Boolean =
        (identity.sources and InputDevice.SOURCE_DPAD) == InputDevice.SOURCE_DPAD &&
            identity.keyboardType == InputDevice.KEYBOARD_TYPE_NON_ALPHABETIC
}

/**
 * Profile registry for Amazon Fire TV remotes.
 *
 * Android runtime deviceId is intentionally not stored or matched: Amazon documents it
 * as arbitrary. Vendor/product-specific profiles can be added once IDs have been captured
 * and verified on real hardware; unknown Fire remotes safely use the standard profile.
 */
object FireRemoteProfileRegistry {
    private val profiles: List<FireRemoteProfile> = listOf(StandardAmazonRemoteProfile)

    fun identity(device: InputDevice): FireRemoteIdentity = FireRemoteIdentity(
        name = device.name.orEmpty(),
        vendorId = device.vendorId,
        productId = device.productId,
        descriptor = device.descriptor.orEmpty(),
        sources = device.sources,
        keyboardType = device.keyboardType,
    )

    fun isFireRemote(device: InputDevice): Boolean =
        StandardAmazonRemoteProfile.matches(identity(device))

    fun profileFor(device: InputDevice?): FireRemoteProfile? {
        device ?: return null
        val identity = identity(device)
        return profiles.firstOrNull { it.matches(identity) }
    }

    fun normalizeKey(event: KeyEvent): Int {
        val profile = profileFor(event.device) ?: return event.keyCode
        return profile.normalizeKeyCode(event.keyCode)
    }
}
