package ghoudan.anfaSolution.com.networking.repository

import androidx.room.withTransaction
import chari.groupewib.com.app_models.ItemStock
import chari.groupewib.com.networking.offline.networkBoundResource
import ghoudan.anfaSolution.com.networking.api.EpApi
import ghoudan.anfaSolution.com.networking.entity.mapper.toAppModel
import ghoudan.anfaSolution.com.networking.state.UserInfoState
import dagger.Reusable
import ghoudan.anfaSolution.com.app_models.User
import ghoudan.anfaSolution.com.networking.di.RoomDataBase
import ghoudan.anfaSolution.com.networking.state.EpApiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@Reusable
class UserInfoRepository @Inject constructor(
    private val api: EpApi,
    private val db: RoomDataBase,
    private val roomRepository: RoomRepository
) {

    fun fetchUserInfo() = networkBoundResource(
        query = {
            roomRepository.getAllUsers()
        },
        fetch = {
            api.getUserInfo().data.map { it.toAppModel() }
        },
        saveFetchResult = { user ->
            db.withTransaction {
                roomRepository.insertUser(user)
            }
        }
    )
    suspend fun fetchUserInfo2(): Flow<EpApiState<List<User>>> {
        return flow<EpApiState<List<User>>> {
            try {
                val result = api.getUserInfo()
                roomRepository.insertUser(result.data.map { it.toAppModel() })
                emit(EpApiState.Success(result.data.map { it.toAppModel() }))
            } catch (e: Exception) {
                emit(EpApiState.Error(e))
            }
        }.onStart {
            emit(EpApiState.Loading())
        }
    }
}
