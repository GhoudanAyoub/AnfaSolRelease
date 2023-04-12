package ghoudan.anfaSolution.com.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import ghoudan.anfaSolution.com.app_models.Customer
import ghoudan.anfaSolution.com.networking.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountInfoFragmentViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    fun getCustomer(customerId: Int): LiveData<Customer> {
        return liveData {
//            customerRepository.getCustomer(customerId).collect { customer ->
//                emit(customer)
//            }
        }
    }
}
