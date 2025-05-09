package chari.groupewib.com.ui.command.sales

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import chari.groupewib.com.app_models.Item
import chari.groupewib.com.networking.entity.PackingListEntity
import chari.groupewib.com.networking.entity.StockSaisieEntity
import chari.groupewib.com.networking.request.BuildPackingListUrl
import chari.groupewib.com.networking.request.PurchaseOrderHeaderRequest
import chari.groupewib.com.networking.request.PurchaseOrderHeaderResult
import chari.groupewib.com.networking.request.PurchaseOrderLinesRequest
import chari.groupewib.com.networking.request.SalesOrderHeaderRequest
import chari.groupewib.com.networking.request.SalesOrderHeaderResult
import chari.groupewib.com.networking.request.SalesOrderLinesRequest
import chari.groupewib.com.networking.request.UpdatePurchaseHeaderRequest
import chari.groupewib.com.networking.request.UpdateSalesHeaderRequest
import chari.groupewib.com.networking.response.SalesOrderLinesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import ghoudan.anfaSolution.com.app_models.CommandAchat
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.app_models.PurchaseOrder
import ghoudan.anfaSolution.com.app_models.Slot
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.networking.repository.ClientListRepository
import ghoudan.anfaSolution.com.networking.repository.OrderRepository
import ghoudan.anfaSolution.com.networking.state.EpApiState
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

@HiltViewModel
class CommandViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val clientListRepository: ClientListRepository,
) : ViewModel() {

    val items = mutableListOf<Item>()
    private val orderLiveData: MutableLiveData<EpApiState<List<CommandAchat>>> =
        MutableLiveData()
    val commands: LiveData<EpApiState<List<CommandAchat>>> = orderLiveData

    private val orderPushesLiveData: MutableLiveData<EpApiState<List<PurchaseOrder>>> =
        MutableLiveData()
    val achats: LiveData<EpApiState<List<PurchaseOrder>>> = orderPushesLiveData

    private val slotLiveData: MutableLiveData<EpApiState<List<Slot>>> =
        MutableLiveData()
    val slots: LiveData<EpApiState<List<Slot>>> = slotLiveData

    private val customerAnfaLiveData: MutableLiveData<EpApiState<CustomerAnfa>> =
        MutableLiveData()
    val customer: LiveData<EpApiState<CustomerAnfa>> = customerAnfaLiveData

    private val supplierAnfaLiveData: MutableLiveData<EpApiState<SupplierAnfa>> =
        MutableLiveData()
    val supplier: LiveData<EpApiState<SupplierAnfa>> = supplierAnfaLiveData

    private val addOrderLiveData: MutableLiveData<EpApiState<SalesOrderLinesResponse>> =
        MutableLiveData()
    val addOrder: LiveData<EpApiState<SalesOrderLinesResponse>> = addOrderLiveData

    private val addHeaderOrderLiveData: MutableLiveData<EpApiState<SalesOrderHeaderResult>> =
        MutableLiveData()
    val addHeaderOrder: LiveData<EpApiState<SalesOrderHeaderResult>> = addHeaderOrderLiveData

    private val updateHeaderOrderLiveData: MutableLiveData<EpApiState<SalesOrderHeaderResult>> =
        MutableLiveData()
    val updateHeaderOrder: LiveData<EpApiState<SalesOrderHeaderResult>> = updateHeaderOrderLiveData

    private val headerOrderLiveData: MutableLiveData<EpApiState<SalesOrderHeaderResult>> =
        MutableLiveData()
    val getHeaderOrder: LiveData<EpApiState<SalesOrderHeaderResult>> = headerOrderLiveData

    private val addPurchaseHeaderOrderLiveData: MutableLiveData<EpApiState<PurchaseOrderHeaderResult>> =
        MutableLiveData()
    val addPurchaseHeaderOrder: LiveData<EpApiState<PurchaseOrderHeaderResult>> =
        addPurchaseHeaderOrderLiveData

    private val updatePurchaseHeaderOrderLiveData: MutableLiveData<EpApiState<PurchaseOrderHeaderResult>> =
        MutableLiveData()
    val updatePurchaseHeaderOrder: LiveData<EpApiState<PurchaseOrderHeaderResult>> =
        updatePurchaseHeaderOrderLiveData
    private val getPurchaseHeaderOrderLiveData: MutableLiveData<EpApiState<PurchaseOrderHeaderResult>> =
        MutableLiveData()
    val getPurchaseHeaderOrder: LiveData<EpApiState<PurchaseOrderHeaderResult>> =
        getPurchaseHeaderOrderLiveData

    private val deleteLineOrderLiveData: MutableLiveData<EpApiState<Boolean>> =
        MutableLiveData()
    val deleteLineOrder: LiveData<EpApiState<Boolean>> = deleteLineOrderLiveData

    private val updateLineOrderLiveData: MutableLiveData<EpApiState<SalesOrderLinesResponse>> =
        MutableLiveData()
    val updateLineOrder: LiveData<EpApiState<SalesOrderLinesResponse>> = updateLineOrderLiveData

    private val deleteDocumentOrderLiveData: MutableLiveData<EpApiState<Boolean>> =
        MutableLiveData()
    val deleteDocumentOrder: LiveData<EpApiState<Boolean>> = deleteDocumentOrderLiveData

    private val itemLineSaleOrderLiveData: MutableLiveData<EpApiState<List<Item>>> =
        MutableLiveData()
    val itemLineOrder: LiveData<EpApiState<List<Item>>> = itemLineSaleOrderLiveData

    //*********** Purchase Order ***********//
    fun addPurchaseCommandsHeader(customerId: String) {
        viewModelScope.launch {
            orderRepository.addPurchaseCommandsHeader(
                PurchaseOrderHeaderRequest(
                    customerId,
                    customerId
                )
            )
                .collect { addOrder ->
                    addOrder.let { commandAchat ->
                        addPurchaseHeaderOrderLiveData.value = commandAchat
                    }
                }
        }
    }

    fun getPurchaseCommandsHeader(
        type: String,
        document8no: String,
    ) {
        viewModelScope.launch {
            orderRepository.getPurchaseCommandsHeader(type, document8no)
                .collect { addOrder ->
                    getPurchaseHeaderOrderLiveData.value = addOrder
                }
        }
    }

    fun updatePurchaseCommandsHeader(
        type: String,
        document8no: String,
        order: UpdatePurchaseHeaderRequest,
    ) {
        viewModelScope.launch {
            orderRepository.getPurchaseCommandsHeader(type, document8no)
                .collect { purchaseHeader ->
                    purchaseHeader.data?.etag?.let { etag ->
                        orderRepository.updatePurchaseCommandsHeader(etag, type, document8no, order)
                            .collect { addOrder ->
                                addOrder.let { commandAchat ->
                                    updatePurchaseHeaderOrderLiveData.value = commandAchat
                                }
                            }
                    }
                }
        }
    }

    fun deletePurchaseCommandsHeader(
        etag: String,
        type: String,
        document8no: String,
    ) {
        viewModelScope.launch {
            orderRepository.deletePurchaseCommandsHeader(etag, type, document8no)
                .collect { addOrder ->
                    deleteDocumentOrderLiveData.value = addOrder
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deletePurchaseCommandsLine(
        etag: String,
        type: String,
        document8no: String,
        code: String,
    ) {
        viewModelScope.launch {
            items.first { it.code == code }.Line_No?.let { numLine ->
                orderRepository.deletePurchaseCommandsLine(etag, type, document8no, numLine)
                    .collect { addOrder ->
                        if (addOrder is EpApiState.Success)
                            items.removeIf { it.code == code }
                        deleteLineOrderLiveData.value = addOrder
                    }
            }
        }
    }

    fun updatePurchaseCommandsLine(
        type: String,
        document8no: String,
        code: String,
        lineNo: Int,
        weight: Double,
        units: Double,
        parcel: Int,
    ) {
        viewModelScope.launch {
            items.first { it.code == code }.also {
                val order = PurchaseOrderLinesRequest(
                    doc_type = type,
                    doc_No = document8no,
                    No = code,
                    total_Unit = units,
                    total_weight = weight,
                    Parcel = parcel,
                    Line_No = lineNo.toString(),
                )
                orderRepository.getSalesCommandsLines(type, document8no)
                    .collect { orderLine ->
                        orderLine.data?.first { it.code == code }.also { item ->
                            item?.etag?.let { etag ->
                                item.Line_No?.let { Line_No ->
                                    orderRepository.updatePurchaseCommandsLine(
                                        etag,
                                        type,
                                        document8no,
                                        Line_No,
                                        order
                                    ).collect { currentSale ->
                                        updateLineOrderLiveData.value = currentSale
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    fun addPurchaseCommandsLine(addOrder: PurchaseOrderHeaderResult, item: Item) {
        viewModelScope.launch {
            addOrder.let { commandAchat ->
                val order = PurchaseOrderLinesRequest(
                    doc_type = commandAchat.Type,
                    doc_No = commandAchat.No,
                    No = item.code,
                    total_Unit = item.unitCost,
                    total_weight = item.unitCost,
                    Parcel = item.parcel,
                    Line_No = (10_000..100_000).random().toString(),
                )
                orderRepository.addPurchaseCommandsLine(order).collect { currentSale ->
                    items.filter { itm -> itm.code == item.code }.forEach { itm ->
                        itm.itemWeight = currentSale.data?.total_weight
                        itm.itemUnits = currentSale.data?.total_Unit
                        itm.parcel = currentSale.data?.Parcel
                        itm.Document_No = currentSale.data?.doc_No
                        itm.Document_Type = currentSale.data?.doc_type
                        itm.Line_No = currentSale.data?.Line_No
                        itm.quantity = currentSale.data?.quantity
                        itm.etag = currentSale.data?.etag
                    }
                    addOrderLiveData.value = currentSale
                }
            }
        }
    }


    //*********** Sales Order ***********//
    fun addSalesCommandsHeader(customerId: String) {
        viewModelScope.launch {
            orderRepository.addSalesCommandsHeader(SalesOrderHeaderRequest(customerId = customerId))
                .collect { addOrder ->
                    if (items.size >= 1) items.clear()
                    addHeaderOrderLiveData.value = addOrder
                }
        }
    }

    fun getSalesCommandsHeader(
        type: String,
        document8no: String,
    ) {
        viewModelScope.launch {
            orderRepository.getSalesCommandsHeader(type, document8no).collect { headerResult ->
                headerOrderLiveData.value = headerResult
            }
        }
    }

    fun updateSalesCommandsHeader(
        type: String,
        document8no: String,
        order: UpdateSalesHeaderRequest,
    ) {
        viewModelScope.launch {
            orderRepository.getSalesCommandsHeader(type, document8no).collect { headerResult ->
                headerResult.data?.etag?.let { etag ->
                    orderRepository.updateSalesCommandsHeader(etag, type, document8no, order)
                        .collect { addOrder ->
                            if (items.size >= 1) items.clear()
                            updateHeaderOrderLiveData.value = addOrder
                        }
                }
            }
        }
    }

    fun addSalesCommandsLine(addOrder: SalesOrderHeaderResult, item: Item) {
        viewModelScope.launch {
            addOrder.let { commandAchat ->
                val order = SalesOrderLinesRequest(
                    doc_type = commandAchat.Type,
                    doc_No = commandAchat.No,
                    No = item.code.toString(),
                    Type = "Item",
                    total_Unit = item.itemUnits,
                    total_weight = item.itemWeight,
                    Parcel = item.parcel
                )
                orderRepository.addSalesCommandsLine(order).collect { currentSale ->
                    items.filter { itm -> itm.code == item.code }.map {
                        it.itemWeight = currentSale.data?.total_weight
                        it.itemUnits = currentSale.data?.total_Unit
                        it.parcel = currentSale.data?.Parcel
                        it.Document_No = currentSale.data?.doc_No
                        it.Document_Type = currentSale.data?.doc_type
                        it.Line_No = currentSale.data?.Line_No
                        it.quantity = currentSale.data?.quantity
                        it.etag = currentSale.data?.etag
                    }
                    addOrderLiveData.value = currentSale
                }
            }
        }
    }

    fun addSalesCommandsLineList(addOrder: SalesOrderHeaderResult, itemList: List<Item>) {
        viewModelScope.launch {
            var lastSale: EpApiState<SalesOrderLinesResponse>? = null
            addOrderLiveData.value = EpApiState.Loading()
            if (itemList.isNullOrEmpty())
                addOrderLiveData.value = EpApiState.Failure()
            itemList.map { item ->
                addOrder.let { commandAchat ->
                    val order = SalesOrderLinesRequest(
                        doc_type = commandAchat.Type,
                        doc_No = commandAchat.No,
                        No = item.code.toString(),
                        Type = "Item",
                        total_Unit = item.itemUnits,
                        total_weight = item.itemWeight,
                        Parcel = item.parcel
                    )
                    orderRepository.addSalesCommandsLine(order).collect { currentSale ->
                        items.filter { itm -> itm.code == item.code }.map {
                            it.itemWeight = currentSale.data?.total_weight
                            it.itemUnits = currentSale.data?.total_Unit
                            it.parcel = currentSale.data?.Parcel
                            it.Document_No = currentSale.data?.doc_No
                            it.Document_Type = currentSale.data?.doc_type
                            it.Line_No = currentSale.data?.Line_No
                            it.quantity = currentSale.data?.quantity
                            it.etag = currentSale.data?.etag
                        }
                        lastSale = currentSale
                    }
                }
            }
            if (lastSale?.data != null)
                itemList.map {
                    items.add(it)
                }
            addOrderLiveData.value = lastSale
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteSalesCommandsLine(
        Etag: String,
        type: String,
        document8no: String,
        code: String?,
    ) {
        viewModelScope.launch {
            items.first { it.code == code }.Line_No?.let { numLine ->
                orderRepository.getSalesCommandsLine(type, document8no, numLine)
                    .collect { orderLine ->
                        orderLine.data?.etag?.let { etag ->
                            orderLine.data?.Line_No?.let { Line_No ->
                                orderRepository.deleteSalesCommandsLine(
                                    etag,
                                    type,
                                    document8no,
                                    Line_No
                                )
                                    .collect { addOrder ->
                                        if (addOrder is EpApiState.Success)
                                            items.removeIf { it.code == code }
                                        deleteLineOrderLiveData.value = addOrder
                                    }
                            }
                        }
                    }
            }
        }
    }

    fun updateSalesCommandsLine(
        etag: String,
        type: String,
        document8no: String,
        Line_No: Int,
        weight: Double,
        units: Double,
        parcel: Int,
    ) {
        viewModelScope.launch {
            items.firstOrNull { it.Line_No == Line_No }.also {
                val order = SalesOrderLinesRequest(
                    doc_type = type,
                    doc_No = document8no,
                    No = it?.code,
                    Type = "Item",
                    Parcel = parcel,
                    total_Unit = units,
                    total_weight = weight
                )
                orderRepository.getSalesCommandsLine(type, document8no, Line_No)
                    .distinctUntilChanged()
                    .flowOn(Dispatchers.IO)
                    .collect { orderLine ->
                        orderLine.data?.etag?.let { it1 ->
                            orderRepository.updateSalesCommandsLine(
                                it1,
                                type,
                                document8no,
                                Line_No,
                                order
                            )
                                .collect { addOrder ->
                                    items.filter { it.Line_No == addOrder.data?.Line_No }.map {
                                        it.etag = addOrder.data?.etag
                                    }
                                    updateLineOrderLiveData.value = addOrder
                                }
                        }
                    }

            }
        }
    }

    fun deleteSalesCommandsHeader(
        etag: String,
        type: String,
        document8no: String,
    ) {
        viewModelScope.launch {
            orderRepository.deleteSalesCommandsHeader(etag, type, document8no)
                .collect { addOrder ->
                    deleteDocumentOrderLiveData.value = addOrder
                }
        }
    }

    fun getSalesCommandsLines(
        type: String,
        document8no: String,
        b: Boolean = false,
    ) {
        viewModelScope.launch {
            orderRepository.getSalesCommandsLines(type, document8no, b)
                .distinctUntilChanged()
                .flowOn(Dispatchers.IO)
                .collect { familyBrands ->
                    itemLineSaleOrderLiveData.value = null
                    itemLineSaleOrderLiveData.value = familyBrands
                }
        }
    }

    fun getCommand() {
        viewModelScope.launch {
            orderRepository.getSelesCommands()
                .distinctUntilChanged()
                .flowOn(Dispatchers.IO)
                .collect { familyBrands ->
                    orderLiveData.value = familyBrands
                }
        }
    }

    fun getPorchesCommands() {
        viewModelScope.launch {
            orderRepository.getPorchesCommands()
                .distinctUntilChanged()
                .flowOn(Dispatchers.IO)
                .collect { familyBrands ->
                    orderPushesLiveData.value = familyBrands
                }
        }
    }

    fun getCustomerByID(
        guid: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            clientListRepository.getCustomerByID(guid)
                .distinctUntilChanged()
                .flowOn(Dispatchers.IO)
                .collectLatest { result ->
                    viewModelScope.launch(Dispatchers.Main) {
                        customerAnfaLiveData.value = result
                    }
                }
        }
    }

    fun getSupplierByID(
        guid: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            clientListRepository.getSupplierByID(guid).collectLatest { result ->
                viewModelScope.launch(Dispatchers.Main) {
                    supplierAnfaLiveData.value = result
                }
            }
        }
    }

    fun addOrderLine(it: Item) {
        //to change later
//        val item = items.find { item -> item.code == it.code && item.Line_No==it.Line_No}
//        if (item != null) {
//            item.itemUnits?.plus(1)
//        } else {
//            items.add(it)
//        }
        items.add(it)
    }

    fun updateItemLine(currentItem: Item) {
        items.filter { item -> item.code == currentItem.code }.map {
            it.itemWeight = currentItem.itemWeight
            it.itemUnits = currentItem.itemUnits
            it.parcel = currentItem.parcel
        }
    }

    fun getStockSaisieList(): LiveData<EpApiState<List<StockSaisieEntity>>> {
        return liveData {
            orderRepository.getStockSaisieList()
                .distinctUntilChanged()
                .flowOn(Dispatchers.IO)
                .collect {
                    emit(it)
                }
        }
    }

    fun getPackingListEntity(docNo: String): LiveData<EpApiState<List<PackingListEntity>>> {
        return liveData {
            orderRepository.getPackingListEntity(docNo)
                .distinctUntilChanged()
                .flowOn(Dispatchers.IO)
                .collect {
                    emit(it)
                }
        }
    }

    //this to fetch the packing list by ligne ach when clicking on PackingList item
    fun getPackingListByColisNumber(
        NumColis: String,
        article_num: String,
    ): LiveData<EpApiState<List<PackingListEntity>>> {
        return liveData {
            orderRepository.getPackingListByColisNumber(NumColis, article_num)
                .distinctUntilChanged()
                .flowOn(Dispatchers.IO)
                .collect {
                    emit(it)
                }
        }
    }

    //this to fetch the colis selected inside packing list by ligne ach when clicking on seles line
    fun getPackingListByLigneAch(LigneAch: String): LiveData<EpApiState<List<PackingListEntity>>> {
        return liveData {
            orderRepository.getPackingListByLigneAch(LigneAch)
                .distinctUntilChanged()
                .flowOn(Dispatchers.IO)
                .collect {
                    emit(it)
                }
        }
    }

    private fun updatePackingList(buildPackingListUrl: BuildPackingListUrl): LiveData<EpApiState<PackingListEntity>> {
        return liveData {
            orderRepository.updatePackingList(buildPackingListUrl)
                .distinctUntilChanged()
                .flowOn(Dispatchers.IO)
                .collect {
                    emit(it)
                }
        }
    }


    fun updateArticleColis(
        stockSaisieList: List<PackingListEntity>,
        callback: (UpdateColisStatus) -> Unit,
    ) {
        callback.invoke(UpdateColisStatus.LOADING)

        val checkedItems = stockSaisieList.filter { it.isChecked }

        if (checkedItems.isEmpty()) {
            callback.invoke(UpdateColisStatus.DELIVERED)
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            // Process items in batches to avoid overwhelming the main thread
            val batchSize = 20
            val totalBatches = (checkedItems.size + batchSize - 1) / batchSize // Ceiling division

            for (batchIndex in 0 until totalBatches) {
                val startIndex = batchIndex * batchSize
                val endIndex = minOf(startIndex + batchSize, checkedItems.size)
                val batch = checkedItems.subList(startIndex, endIndex)

                val deferreds = batch.map { item ->
                    async {
                        val buildPackingListUrl = BuildPackingListUrl(
                            etag = item.etag,
                            documentNo = item.document_No,
                            codeFour = item.codeFour,
                            numColis = item.numColis.toInt(),
                            noArt = item.No_Art,
                            ligneAch = item.Ligne_ach
                        )

                        try {
                            orderRepository.updatePackingList(buildPackingListUrl)
                                .distinctUntilChanged()
                                .flowOn(Dispatchers.IO)
                                .collect { result ->
                                    result is EpApiState.Success
                                }
                        } catch (e: Exception) {
                            false
                        }
                    }
                }

                // Wait for batch completion before proceeding to next batch
                deferreds.awaitAll()
            }

            withContext(Dispatchers.Main) {
                callback.invoke(UpdateColisStatus.DELIVERED)
            }
        }
    }
}

enum class UpdateColisStatus {
    LOADING,
    DELIVERED,
    CANCELLED
}
