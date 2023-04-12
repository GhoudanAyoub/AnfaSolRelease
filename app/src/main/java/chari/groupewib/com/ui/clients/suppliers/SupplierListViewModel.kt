package ghoudan.anfaSolution.com.ui.clients.suppliers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chari.groupewib.com.networking.request.CreateSupplierRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.networking.repository.ClientListRepository
import ghoudan.anfaSolution.com.networking.state.EpApiState
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SupplierListViewModel @Inject constructor(
    private val clientListRepository: ClientListRepository
) : ViewModel() {

    private var suppliersList: List<SupplierAnfa>? = null

    private var supplierListLiveData: MutableLiveData<EpApiState<List<SupplierAnfa>>> =
        MutableLiveData()
    val supplierList: LiveData<EpApiState<List<SupplierAnfa>>> = supplierListLiveData

    private val addSupplierAnfaLiveData: MutableLiveData<EpApiState<SupplierAnfa>> =
        MutableLiveData()
    val addSupplier: LiveData<EpApiState<SupplierAnfa>> = addSupplierAnfaLiveData

    private val getSupplierAnfaLiveData: MutableLiveData<EpApiState<SupplierAnfa>> =
        MutableLiveData()
    val Supplier: LiveData<EpApiState<SupplierAnfa>> = getSupplierAnfaLiveData

    private val deleteSupplierAnfaLiveData: MutableLiveData<EpApiState<Boolean>> =
        MutableLiveData()
    val deleteSupplier: LiveData<EpApiState<Boolean>> = deleteSupplierAnfaLiveData

    fun getSuppliers() {
        viewModelScope.launch {
            clientListRepository.getSuppliersRoom().collect {
                suppliersList = it.data
                supplierListLiveData.value = it
            }
        }
    }
    fun getSingleSuppliers(clientId: String) {
        viewModelScope.launch {
            clientListRepository.getSingleSuppliers(clientId).collect {
                getSupplierAnfaLiveData.value = it
            }
        }
    }

    fun addSupplier(order: CreateSupplierRequest) {
        viewModelScope.launch {
            clientListRepository.addSupplier(order).collect { result ->
                addSupplierAnfaLiveData.value = result
            }
        }
    }

    fun deleteSupplier(customer: SupplierAnfa) {
        viewModelScope.launch {
            clientListRepository.deleteSupplier(customer).collect { result ->
                deleteSupplierAnfaLiveData.value = result
            }
        }
    }

    fun updateSupplier(customer: SupplierAnfa, order: CreateSupplierRequest) {
        viewModelScope.launch {
            customer.etag?.let {
                customer.code?.let { code ->
                    clientListRepository.updateSupplier(it, code, order).collect { result ->
                        addSupplierAnfaLiveData.value = result
                    }
                }
            }
        }
    }
}
