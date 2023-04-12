package ghoudan.anfaSolution.com.networking.repository

import androidx.room.withTransaction
import chari.groupewib.com.app_models.Item
import chari.groupewib.com.app_models.ItemStock
import chari.groupewib.com.app_models.SingleItemResponse
import chari.groupewib.com.networking.offline.networkBoundResource
import chari.groupewib.com.networking.request.CreateClientRequest
import chari.groupewib.com.networking.request.CreateItemRequest
import chari.groupewib.com.networking.request.CreateSupplierRequest
import chari.groupewib.com.networking.response.ErrorEntity
import dagger.Reusable
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.networking.api.EpApi
import ghoudan.anfaSolution.com.networking.di.RoomDataBase
import ghoudan.anfaSolution.com.networking.entity.mapper.toAppModel
import ghoudan.anfaSolution.com.networking.state.EpApiState
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

@Reusable
class ClientListRepository @Inject constructor(
    private val api: EpApi,
    private val roomRepository: RoomRepository,
    private val db: RoomDataBase,
    private val jsonSerializer: Json
) {


        fun getAllCustomers() = networkBoundResource(
        query = {
            roomRepository.getAllCustomers()
        },
        fetch = {
            api.getClients().data.map { it.toAppModel() }
        },
        saveFetchResult = { user ->
            db.withTransaction {
                roomRepository.insertCustomers(user)
            }
        }
    )

    fun getSuppliers() = networkBoundResource(
        query = {
                roomRepository.getAllSuppliers()
        },
        fetch = {
            api.getSuppliers().data.map { it.toAppModel() }
        },
        saveFetchResult = { user ->
            db.withTransaction {
                roomRepository.insertSuppliers(user)
            }
        }
    )
    suspend fun getAllDataOnce(): Flow<EpApiState<Boolean>> {
        return flow<EpApiState<Boolean>> {
            runBlocking {
//                withContext(Dispatchers.IO) {
//                    val articles = api.getArticles().data.map { it.toAppModel() }
//                    roomRepository.insertItems(articles)
//                }
                emit(EpApiState.Success(true))
//                withContext(Dispatchers.IO) {
//                    val clients = api.getClients().data.map { it.toAppModel() }
//                    roomRepository.insertCustomers(clients)
//                }
//                withContext(Dispatchers.IO) {
//                    val suppliers = api.getSuppliers().data.map { it.toAppModel() }
//                    roomRepository.insertSuppliers(suppliers)
//                    emit(EpApiState.Success(true))
//                }
            }

        }.catch { exception ->
            emit(EpApiState.Error(exception))
        }.onStart { emit(EpApiState.Loading()) }
    }

    suspend fun getStock(): Flow<EpApiState<List<ItemStock>>> {
        return flow<EpApiState<List<ItemStock>>> {
            val articles = api.getStock().data.map { it.toAppModel() }
            emit(EpApiState.Success(articles))
        }.catch { exception ->
            emit(EpApiState.Error(exception))
        }.onStart {
            emit(EpApiState.Loading())
        }
    }

    suspend fun getCustomerByID(guid: String): Flow<EpApiState<CustomerAnfa>> {
        return flow<EpApiState<CustomerAnfa>> {
            emit(EpApiState.Success(roomRepository.getCustomerByID(guid)))
        }.catch { exception ->
            emit(EpApiState.Error(exception))
        }.onStart {
            emit(EpApiState.Loading())
        }
    }

    suspend fun getSupplierByID(guid: String): Flow<EpApiState<SupplierAnfa>> {
        return flow<EpApiState<SupplierAnfa>> {
            emit(EpApiState.Success(roomRepository.getSupplierByID(guid)))
        }.catch { exception ->
            emit(EpApiState.Error(exception))
        }.onStart {
            emit(EpApiState.Loading())
        }
    }

    suspend fun deleteClient(customer: CustomerAnfa): Flow<EpApiState<Boolean>> {
        return flow<EpApiState<Boolean>> {
            val result =
                customer.etag?.let { etag -> api.deleteClient(etag, customer.code.toString()) }
            if (result?.code() == 400||result?.code() == 404) {
                val response = try {
                    result.errorBody()?.string()?.let {
                        jsonSerializer.decodeFromString(
                            ErrorEntity.serializer(), it
                        )
                    }
                } catch (e: Exception) {
                    null
                }
                emit(EpApiState.Error(Exception(response?.error?.message)))
            }
            if (result?.code() == 204) {
                result.body()?.let { emit(EpApiState.Success(true)) }
            } else
                result?.body()?.let { emit(EpApiState.Success(true)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch {exception ->
                if(exception.toString().contains("java.io.EOFException"))
                    emit(EpApiState.Success(true))
                else
                emit(EpApiState.Error(exception))
            }
    }

    suspend fun updateClient(
        etag: String,
        No: String,
        order: CreateClientRequest
    ): Flow<EpApiState<CustomerAnfa>> {
        return flow<EpApiState<CustomerAnfa>> {
            val result = api.updateClient(etag, No, order)
            if (result?.code() == 400) {
                val response = try {
                    result.errorBody()?.string()?.let {
                        jsonSerializer.decodeFromString(
                            ErrorEntity.serializer(), it
                        )
                    }
                } catch (e: Exception) {
                    null
                }
                emit(EpApiState.Error(Exception(response?.error?.message)))
            }
            if (result.code() == 200) {
                result.body()?.let { emit(EpApiState.Success(it)) }
            } else
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }

    suspend fun addClient(order: CreateClientRequest): Flow<EpApiState<CustomerAnfa>> {
        return flow<EpApiState<CustomerAnfa>> {
            val result = api.addClient(order)
            if (result.code() == 400) {
                val response = try {
                    result.errorBody()?.string()?.let {
                        jsonSerializer.decodeFromString(
                            ErrorEntity.serializer(), it
                        )
                    }
                } catch (e: Exception) {
                    null
                }
                emit(EpApiState.Error(Exception(response?.error?.message)))
            }
            if (result.code() == 201) {
                result.body()?.let { emit(EpApiState.Success(it)) }
            } else
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }
    suspend fun getSingleItem(clientId: String): Flow<EpApiState<SingleItemResponse>> {
        return flow<EpApiState<SingleItemResponse>> {
            val result = api.getSingleItem(clientId)
            if (result.code() == 400) {
                val response = try {
                    result.errorBody()?.string()?.let {
                        jsonSerializer.decodeFromString(
                            ErrorEntity.serializer(), it
                        )
                    }
                } catch (e: Exception) {
                    null
                }
                emit(EpApiState.Error(Exception(response?.error?.message)))
            }
            result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }


    suspend fun updateItem(
        etag: String,
        No: String,
        order: CreateItemRequest
    ): Flow<EpApiState<SingleItemResponse>> {
        return flow<EpApiState<SingleItemResponse>> {
            val result = api.updateItem(etag, No, order)
            if (result?.code() == 400) {
                val response = try {
                    result.errorBody()?.string()?.let {
                        jsonSerializer.decodeFromString(
                            ErrorEntity.serializer(), it
                        )
                    }
                } catch (e: Exception) {
                    null
                }
                emit(EpApiState.Error(Exception(response?.error?.message)))
            }
            if (result.code() == 200) {
                result.body()?.let { emit(EpApiState.Success(it)) }
            } else
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }

    suspend fun addItem(order: CreateItemRequest): Flow<EpApiState<SingleItemResponse>> {
        return flow<EpApiState<SingleItemResponse>> {
            val result = api.addItem(order)
            if (result.code() == 400) {
                val response = try {
                    result.errorBody()?.string()?.let {
                        jsonSerializer.decodeFromString(
                            ErrorEntity.serializer(), it
                        )
                    }
                } catch (e: Exception) {
                    null
                }
                emit(EpApiState.Error(Exception(response?.error?.message)))
            }
            if (result.code() == 201) {
                result.body()?.let { emit(EpApiState.Success(it)) }
            } else
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }
    suspend fun getSingleClients(clientId: String): Flow<EpApiState<CustomerAnfa>> {
        return flow<EpApiState<CustomerAnfa>> {
            val result = api.getSingleClients(clientId)
            result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }


    suspend fun getSingleSuppliers(clientId: String): Flow<EpApiState<SupplierAnfa>> {
        return flow<EpApiState<SupplierAnfa>> {
            val result = api.getSingleSuppliers(clientId)
            result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }

    suspend fun getSuppliersRoom(): Flow<EpApiState<List<SupplierAnfa>>> {
//        val data = roomRepository.getAllSuppliers()
        return flow<EpApiState<List<SupplierAnfa>>> {
            val suppliers = api.getSuppliers().data.map { it.toAppModel() }
            emit(EpApiState.Success(suppliers))
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }

    suspend fun getAllCustomersRoom(): Flow<EpApiState<List<CustomerAnfa>>> {
//        val data = roomRepository.getAllCustomers()
        return flow<EpApiState<List<CustomerAnfa>>> {
            val clients = api.getClients().data.map { it.toAppModel() }
            emit(EpApiState.Success(clients))
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }

    suspend fun getAllItems() =
        networkBoundResource(
            query = {
                roomRepository.getAllItems()
            },
            fetch = {
                api.getArticles().data.map { it.toAppModel() }
            },
            saveFetchResult = { articles ->
                db.withTransaction {
                    roomRepository.insertItems(articles)
                }
            }

        )

    suspend fun getAllItems2(): Flow<EpApiState<List<Item>>> {
        val data = roomRepository.getAllItems()
        return flow<EpApiState<List<Item>>> {
            val articles = api.getArticles().data.map { it.toAppModel() }
            emit(EpApiState.Success(articles))
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }

    suspend fun addSupplier(order: CreateSupplierRequest): Flow<EpApiState<SupplierAnfa>> {
        return flow<EpApiState<SupplierAnfa>> {
            val result = api.addSupplier(order)
            if (result.code() == 400) {
                val response = try {
                    result.errorBody()?.string()?.let {
                        jsonSerializer.decodeFromString(
                            ErrorEntity.serializer(), it
                        )
                    }
                } catch (e: Exception) {
                    null
                }
                emit(EpApiState.Error(Exception(response?.error?.message)))
            }
            if (result.code() == 201) {
                result.body()?.let { emit(EpApiState.Success(it)) }
            } else
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }

    suspend fun updateSupplier(
        etag: String,
        No: String,
        order: CreateSupplierRequest
    ): Flow<EpApiState<SupplierAnfa>> {
        return flow<EpApiState<SupplierAnfa>> {
            val result = api.updateSupplier(etag, No, order)
            if (result.code() == 200)
                result.body()?.let { emit(EpApiState.Success(it)) }
            if (result.code() == 400) {
                val response = try {
                    result.errorBody()?.string()?.let {
                        jsonSerializer.decodeFromString(
                            ErrorEntity.serializer(), it
                        )
                    }
                } catch (e: Exception) {
                    null
                }
                emit(EpApiState.Error(Exception(response?.error?.message)))
            }
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }

    suspend fun deleteSupplier(supplier: SupplierAnfa): Flow<EpApiState<Boolean>> {
        return flow<EpApiState<Boolean>> {
            val result =
                supplier.etag?.let { etag -> api.deleteSupplier(etag, supplier.code.toString()) }
            if (result?.code() == 400||result?.code() == 404) {
                val response = try {
                    result.errorBody()?.string()?.let {
                        jsonSerializer.decodeFromString(
                            ErrorEntity.serializer(), it
                        )
                    }
                } catch (e: Exception) {
                    null
                }
                emit(EpApiState.Error(Exception(response?.error?.message)))
            }
            if (result?.code() == 204) {
                result.body()?.let { emit(EpApiState.Failure()) }
            } else
                result?.body()?.let { emit(EpApiState.Success(true)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch {exception->
                if(exception.toString().contains("java.io.EOFException"))
                    emit(EpApiState.Success(true))
                else
                emit(EpApiState.Error(exception))
            }
    }
}
