package ghoudan.anfaSolution.com.networking.offline

import android.content.Context
import ghoudan.anfaSolution.com.networking.repository.ClientListRepository
import ghoudan.anfaSolution.com.networking.repository.RoomRepository
import ghoudan.anfaSolution.com.networking.state.EpApiState
import dagger.Reusable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

const val TAG_SINGLE_SHOT_WORKER = "TAG_SINGLE_SHOT_WORKER"

@Reusable
class Synchronizer @Inject constructor(
    @ApplicationContext val context: Context,
    private val clientRepository: ClientListRepository,
    private val roomRepository: RoomRepository,
    private val networkHandler: NetworkHandler
) {

//    private val workManager = WorkManager.getInstance(context)

    suspend fun launch(
        onFinish: () -> Unit,
        onConnectionLost: () -> Unit
    ) {
        startDataStorage(onFinish = {
            onFinish()
        }, onConnectionLost = {
            onConnectionLost()
        })
    }

    private fun startDataStorage(
        onFinish: () -> Unit,
        onConnectionLost: () -> Unit
    ) {
        val time = System.currentTimeMillis()
        runBlocking {
            CoroutineScope(Dispatchers.IO).launch {
                if (networkHandler.checkNetworkAvailability()) {
                    fetchAllData(onFinish, onError = {
                        Timber.w("Sync: Error Fetchin Data")
                        onFinish()
                    })
                    Timber.d("Sync: Sync Time : " + (System.currentTimeMillis() - time))
                } else {
                    Timber.w("Sync: Connection Lost")
                    onConnectionLost()
                }
            }
        }
    }

    private suspend fun fetchAllData(onFinish: () -> Unit, onError: () -> Unit) {
        clientRepository.getAllDataOnce()
            .collect { value ->
                when (value) {
                    is EpApiState.Loading ->
                        Timber.d("Sync: Loading Data To Room")
                    is EpApiState.Success -> {
                        Timber.d("Sync: Success Data is Ready")
                        onFinish()
                    }
                    is EpApiState.Error -> {
                        Timber.e("Sync: Fetch Data Error :" + value.error)
                        onError()
                    }
                    else -> {}
                }
            }
    }

}
