package ghoudan.anfaSolution.com.chari

import ghoudan.anfaSolution.com.BuildConfig
import timber.log.Timber

class FlavorUtils {
    companion object {
        fun printFlavorName() {
            Timber.e(BuildConfig.FLAVOR)
        }
    }
}
