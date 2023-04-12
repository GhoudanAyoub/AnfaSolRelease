package chari.groupewib.com.ui.command.items


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chari.groupewib.com.app_models.Item
import chari.groupewib.com.app_models.ItemStock
import chari.groupewib.com.app_models.SingleItemResponse
import chari.groupewib.com.networking.request.CreateItemRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import ghoudan.anfaSolution.com.networking.offline.Resource
import ghoudan.anfaSolution.com.networking.repository.ClientListRepository
import ghoudan.anfaSolution.com.networking.state.EpApiState
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ItemViewModel @Inject constructor(
    private val clientListRepository: ClientListRepository
) : ViewModel() {

    private var itemList: List<Item>? = null

    private var itemsListLiveData: MutableLiveData<Resource<List<Item>>> =
        MutableLiveData()
    val itemsList: LiveData<Resource<List<Item>>> = itemsListLiveData

    private var stockListLiveData: MutableLiveData<EpApiState<List<ItemStock>>> =
        MutableLiveData()
    val stockList: LiveData<EpApiState<List<ItemStock>>> = stockListLiveData

    private var getArticleAnfaLiveData: MutableLiveData<EpApiState<SingleItemResponse>> =
        MutableLiveData()
    val Item: LiveData<EpApiState<SingleItemResponse>> = getArticleAnfaLiveData


    private var addItemAnfaLiveData: MutableLiveData<EpApiState<SingleItemResponse>> =
        MutableLiveData()
    val addItem: LiveData<EpApiState<SingleItemResponse>> = addItemAnfaLiveData

    fun getSingleItem(clientId: String) {
        viewModelScope.launch {
            clientListRepository.getSingleItem(clientId).collect {
                getArticleAnfaLiveData.value = it
            }
        }
    }

    fun addItem(order: CreateItemRequest) {
        viewModelScope.launch {
            clientListRepository.addItem(order).collect { result ->
                addItemAnfaLiveData.value = result
            }
        }
    }

    fun deleteItem(item: SingleItemResponse) {
//        viewModelScope.launch {
//            clientListRepository.deleteClient(customer).collect { result ->
//                deleteCustomerAnfaLiveData.value = result
//            }
//        }
    }

    fun updateItem(item: SingleItemResponse, order: CreateItemRequest) {
        viewModelScope.launch {
            item.etag?.let {
                item.code.let { code ->
                    clientListRepository.updateItem(it, code.toString(), order).collect { result ->
                        addItemAnfaLiveData.value = result
                    }
                }
            }
        }
    }

    fun fetchItems(query: String) {
        itemList?.let {
            itemsListLiveData.value = if (query.isEmpty()) Resource.Success(it) else
                Resource.Success(it.filter {
                    it.code.toString().contains(query, true) || it.description.toString()
                        .contains(query, true)
                })
        }
    }

    fun getAllItems() {
        viewModelScope.launch {
            clientListRepository.getAllItems().collect {
                itemList = it.data
                itemsListLiveData.value = it
            }
        }
    }

    fun getStock() {
        viewModelScope.launch {
            clientListRepository.getStock().collect {
                stockListLiveData.value = it
            }
        }
    }

}
