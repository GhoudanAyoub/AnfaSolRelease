package ghoudan.anfaSolution.com.networking.offline

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


@Singleton
class NetworkHandler @Inject constructor(
    private val connectivityManager: ConnectivityManager
) :
        ConnectivityManager.NetworkCallback() {

    private val networkStateMStateFlow = MutableStateFlow(NetworkState.EMPTY)
    val networkStateStateFlow: Flow<NetworkState> = networkStateMStateFlow.asStateFlow()

    /**
     * used flow to allow multiple emission
     * user can try multiple times
     */
    private val networkFailedMLiveData = MutableLiveData<Boolean>()
    var networkFailedState: MutableLiveData<Boolean> = networkFailedMLiveData

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(this)
        }

        if (!checkNetworkAvailability()) {
            networkStateMStateFlow.value = NetworkState.UNAVAILABLE
        }
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        networkStateMStateFlow.value = NetworkState.AVAILABLE
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        networkStateMStateFlow.value = NetworkState.LOST
    }

    override fun onUnavailable() {
        super.onUnavailable()
        networkStateMStateFlow.value = NetworkState.UNAVAILABLE
    }

    /**
     * check network availability for Api 23+
     */
    @Suppress("DEPRECATION")
    fun checkNetworkAvailability(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return (
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    )
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }
}
