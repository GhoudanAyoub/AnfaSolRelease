package ghoudan.anfaSolution.com.ui.clients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chari.groupewib.com.networking.request.CreateClientRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.networking.repository.ClientListRepository
import ghoudan.anfaSolution.com.networking.state.CustomersState
import ghoudan.anfaSolution.com.networking.state.EpApiState
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ClientListViewModel @Inject constructor(
    private val clientListRepository: ClientListRepository
) : ViewModel() {

    private var clientList: List<CustomerAnfa>? = null
    private var customersLiveData: MutableLiveData<CustomersState> = MutableLiveData()
    val customers: LiveData<CustomersState> = customersLiveData

    private var customersListLiveData: MutableLiveData<EpApiState<List<CustomerAnfa>>> =
        MutableLiveData()
    val customersList: LiveData<EpApiState<List<CustomerAnfa>>> = customersListLiveData

    private val addCustomerAnfaLiveData: MutableLiveData<EpApiState<CustomerAnfa>> =
        MutableLiveData()
    val addCustomer: LiveData<EpApiState<CustomerAnfa>> = addCustomerAnfaLiveData
    private val getCustomerAnfaLiveData: MutableLiveData<EpApiState<CustomerAnfa>> =
        MutableLiveData()
    val Customer: LiveData<EpApiState<CustomerAnfa>> = getCustomerAnfaLiveData

    private val deleteCustomerAnfaLiveData: MutableLiveData<EpApiState<Boolean>> =
        MutableLiveData()
    val deleteCustomer: LiveData<EpApiState<Boolean>> = deleteCustomerAnfaLiveData

    fun fetchClients(query: String) {
        clientList?.let {
            customersListLiveData.value = if (query.isEmpty()) EpApiState.Success(it) else
                EpApiState.Success(it.filter {
                    it.name?.lowercase()?.contains(query, true) == true ||
                            it.code.lowercase().contains(query, true) ||
                            it.Phone_No?.contains(query, true) == true||
                            it.id.toString()?.contains(query, true) == true
                })
        }
    }

    fun getClients() {
        viewModelScope.launch {
            clientListRepository.getAllCustomersRoom().collect {
                clientList = it.data
                customersListLiveData.value = it
            }
        }
    }

    fun getSingleClients(clientId: String) {
        viewModelScope.launch {
            clientListRepository.getSingleClients(clientId).collect {
                getCustomerAnfaLiveData.value = it
            }
        }
    }

    fun addClient(order: CreateClientRequest) {
        viewModelScope.launch {
            clientListRepository.addClient(order).collect { result ->
                addCustomerAnfaLiveData.value = result
            }
        }
    }

    fun deleteClient(customer: CustomerAnfa) {
        viewModelScope.launch {
            clientListRepository.deleteClient(customer).collect { result ->
                deleteCustomerAnfaLiveData.value = result
            }
        }
    }

    fun updateClient(customer: CustomerAnfa, order: CreateClientRequest) {
        viewModelScope.launch {
            customer.etag?.let {
                customer.code.let { code ->
                    clientListRepository.updateClient(it, code.toString(), order)
                        .collect { result ->
                            addCustomerAnfaLiveData.value = result
                        }
                }
            }
        }
    }


}
