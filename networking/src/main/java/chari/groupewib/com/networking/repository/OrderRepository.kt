package ghoudan.anfaSolution.com.networking.repository

import chari.groupewib.com.app_models.Item
import chari.groupewib.com.networking.entity.PackingListEntity
import chari.groupewib.com.networking.entity.StockSaisieEntity
import chari.groupewib.com.networking.request.PurchaseOrderHeaderRequest
import chari.groupewib.com.networking.request.PurchaseOrderHeaderResult
import chari.groupewib.com.networking.request.PurchaseOrderLinesRequest
import chari.groupewib.com.networking.request.SalesOrderHeaderRequest
import chari.groupewib.com.networking.request.SalesOrderHeaderResult
import chari.groupewib.com.networking.request.SalesOrderLinesRequest
import chari.groupewib.com.networking.request.UpdatePurchaseHeaderRequest
import chari.groupewib.com.networking.request.UpdateSalesHeaderRequest
import chari.groupewib.com.networking.response.ErrorEntity
import chari.groupewib.com.networking.response.SalesOrderLinesResponse
import ghoudan.anfaSolution.com.app_models.CommandAchat
import ghoudan.anfaSolution.com.app_models.PurchaseOrder
import ghoudan.anfaSolution.com.networking.api.EpApi
import ghoudan.anfaSolution.com.networking.entity.mapper.toAppModel
import ghoudan.anfaSolution.com.networking.state.EpApiState
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json
import retrofit2.http.Path
import timber.log.Timber

class OrderRepository @Inject constructor(
    private val api: EpApi,
    private val jsonSerializer: Json
) {

    suspend fun updateSalesCommandsLine(
        Etag: String,
        type: String,
        document8no: String,
        numLine: Int,
        order: SalesOrderLinesRequest
    ): Flow<EpApiState<SalesOrderLinesResponse>> {
        return flow<EpApiState<SalesOrderLinesResponse>> {
            val result = api.updateSalesCommandsLine(Etag, type, document8no, numLine, order)
            if (result.code() == 400 || result.code() == 404) {
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
            if (result.code() == 200)
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                emit(EpApiState.Error(exception))
            }
    }

    suspend fun getSalesCommandsLine(
        type: String,
        document8no: String,
        numLine: Int
    ): Flow<EpApiState<SalesOrderLinesResponse>> {
        return flow<EpApiState<SalesOrderLinesResponse>> {
            val result = api.getSalesCommandsLine(type, document8no, numLine)
            if (result.code() == 400 || result.code() == 404) {
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
            if (result.code() == 200)
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                emit(EpApiState.Error(exception))
            }
    }

    suspend fun deleteSalesCommandsLine(
        Etag: String,
        type: String,
        document8no: String,
        numLine: Int
    ): Flow<EpApiState<Boolean>> {
        return flow<EpApiState<Boolean>> {
            val result = api.deleteSalesCommandsLine(Etag, type, document8no, numLine)
            if (result.code() == 400 || result.code() == 404) {
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
            if (result.code() == 204)
                emit(EpApiState.Success(true))
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                if (exception.toString().contains("java.io.EOFException"))
                    emit(EpApiState.Success(true))
                else
                    emit(EpApiState.Error(exception))
            }
    }

    suspend fun addSalesCommandsLine(order: SalesOrderLinesRequest): Flow<EpApiState<SalesOrderLinesResponse>> {
        return flow<EpApiState<SalesOrderLinesResponse>> {
            val result = api.addSalesCommandsLine(order)
            if (result.code() == 400 || result.code() == 404) {
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
            } else
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                emit(EpApiState.Error(exception))
            }
    }

    suspend fun deleteSalesCommandsHeader(
        Etag: String,
        type: String,
        document8no: String
    ): Flow<EpApiState<Boolean>> {
        return flow<EpApiState<Boolean>> {
            val result = api.deleteSalesCommandsHeader(type, document8no)
            if (result.code() == 400 || result.code() == 404) {
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
            if (result.code() == 204)
                emit(EpApiState.Failure())
            else
                emit(EpApiState.Success(true))
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                Timber.e("8888$exception")
                emit(EpApiState.Error(exception))
            }
    }

    suspend fun addSalesCommandsHeader(order: SalesOrderHeaderRequest): Flow<EpApiState<SalesOrderHeaderResult>> {
        return flow<EpApiState<SalesOrderHeaderResult>> {
            val result = api.addSalesCommandsHeader(order)
            if (result.code() == 400 || result.code() == 404) {
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
            } else
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                emit(EpApiState.Error(exception))
            }
    }

    suspend fun getSalesCommandsHeader(
        type: String,
        document8no: String
    ): Flow<EpApiState<SalesOrderHeaderResult>> {
        return flow<EpApiState<SalesOrderHeaderResult>> {
            val result = api.getSalesCommandsHeader(type, document8no)
            if (result.code() == 400 || result.code() == 404) {
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
            } else
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                emit(EpApiState.Error(exception))
            }
    }

    suspend fun updateSalesCommandsHeader(
        Etag: String,
        type: String,
        document8no: String,
        order: UpdateSalesHeaderRequest
    ): Flow<EpApiState<SalesOrderHeaderResult>> {
        return flow<EpApiState<SalesOrderHeaderResult>> {
            val result = api.updateSalesCommandsHeader(Etag, type, document8no, order)
            if (result.code() == 400 || result.code() == 404) {
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
            } else
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                emit(EpApiState.Error(exception))
            }
    }


    suspend fun getSalesCommandsLines(
        type: String,
        document8no: String,
        b: Boolean = false
    ): Flow<EpApiState<List<Item>>> {
        return flow<EpApiState<List<Item>>> {
            if (b) {
                val result = api.getSalesCommandsLines("Document_No eq '$document8no'")
                val data = result.data
                emit(EpApiState.Success(data.map { it.toAppModel() }))
            } else {
                val result = api.getPurchaseCommandsLines("Document_No eq '$document8no'")
                val data = result.data
                emit(EpApiState.Success(data.map { it.toAppModel() }))
            }
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }

    //************Purchase
    suspend fun deletePurchaseCommandsHeader(
        Etag: String,
        type: String,
        document8no: String
    ): Flow<EpApiState<Boolean>> {
        return flow<EpApiState<Boolean>> {
            val result = api.deletePurchaseCommandsHeader(type, document8no)
            if (result.code() == 204)
                emit(EpApiState.Failure())
            else
                emit(EpApiState.Success(true))
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                emit(EpApiState.Error(exception))
            }
    }

    suspend fun updatePurchaseCommandsHeader(
        etag: String,
        type: String,
        document8no: String,
        order: UpdatePurchaseHeaderRequest
    ): Flow<EpApiState<PurchaseOrderHeaderResult>> {
        return flow<EpApiState<PurchaseOrderHeaderResult>> {
            val result = api.updatePurchaseCommandsHeader(etag, type, document8no, order)
            if (result.code() == 400 || result.code() == 404) {
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
            } else
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                emit(EpApiState.Error(exception))
            }
    }

    suspend fun getPurchaseCommandsHeader(
        type: String,
        document8no: String
    ): Flow<EpApiState<PurchaseOrderHeaderResult>> {
        return flow<EpApiState<PurchaseOrderHeaderResult>> {
            val result = api.getPurchaseCommandsHeader(type, document8no)
            if (result.code() == 400 || result.code() == 404) {
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
            } else
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                emit(EpApiState.Error(exception))
            }
    }

    suspend fun addPurchaseCommandsHeader(order: PurchaseOrderHeaderRequest): Flow<EpApiState<PurchaseOrderHeaderResult>> {
        return flow<EpApiState<PurchaseOrderHeaderResult>> {
            val result = api.addPurchaseCommandsHeader(order)
            if (result.code() == 400 || result.code() == 404) {
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
            } else
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                emit(EpApiState.Error(exception))
            }
    }

    //************** Purchase Lines
    suspend fun deletePurchaseCommandsLine(
        Etag: String,
        type: String,
        document8no: String,
        numLine: Int
    ): Flow<EpApiState<Boolean>> {
        return flow<EpApiState<Boolean>> {
            val result = api.deletePurchaseCommandsLine(type, document8no, numLine)
            if (result.code() == 400 || result.code() == 404) {
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
            if (result.code() == 204)
                emit(EpApiState.Success(true))
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                if (exception.toString().contains("java.io.EOFException"))
                    emit(EpApiState.Success(true))
                else
                    emit(EpApiState.Error(exception))
            }
    }

    suspend fun addPurchaseCommandsLine(order: PurchaseOrderLinesRequest): Flow<EpApiState<SalesOrderLinesResponse>> {
        return flow<EpApiState<SalesOrderLinesResponse>> {
            val result = api.addPurchaseCommandsLine(order)
            if (result.code() == 400 || result.code() == 404) {
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
            } else
                result.body()?.let { emit(EpApiState.Success(it)) }
        }.onStart { emit(EpApiState.Loading()) }
            .catch { exception ->
                emit(EpApiState.Error(exception))
            }
    }

    suspend fun updatePurchaseCommandsLine(
        Etag: String,
        type: String,
        document8no: String,
        numLine: Int,
        order: PurchaseOrderLinesRequest
    ): Flow<EpApiState<SalesOrderLinesResponse>> {
        return flow<EpApiState<SalesOrderLinesResponse>> {
            val result = api.updatePurchaseCommandsLine(Etag, type, document8no, numLine, order)
            if (result.code() == 400 || result.code() == 404) {
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
            .catch { exception ->
                emit(EpApiState.Error(exception))
            }
    }


    suspend fun getSelesCommands(): Flow<EpApiState<List<CommandAchat>>> {
        return flow<EpApiState<List<CommandAchat>>> {
            val result = api.getSelesCommands()
            val data = result.data
            emit(EpApiState.Success(data))
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }


    suspend fun getPorchesCommands(): Flow<EpApiState<List<PurchaseOrder>>> {
        return flow<EpApiState<List<PurchaseOrder>>> {
            val result = api.getPorchesCommands()
            val data = result.data
            emit(EpApiState.Success(data))
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }

    suspend fun getStockSaisieList(): Flow<EpApiState<List<StockSaisieEntity>>> {
        return flow<EpApiState<List<StockSaisieEntity>>> {
            val result = api.getStockSaisieList()
            val data = result.data
            emit(EpApiState.Success(data))
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }

    suspend fun getPackingListEntity(docType: String,docNo: String): Flow<EpApiState<List<PackingListEntity>>> {
        return flow<EpApiState<List<PackingListEntity>>> {
            val result = api.getPackingList()
            val data = result.data
            emit(EpApiState.Success(data))
        }.onStart { emit(EpApiState.Loading()) }
            .catch {
                emit(EpApiState.Error(it))
            }
    }

}
