package ghoudan.anfaSolution.com.networking.state

import ghoudan.anfaSolution.com.app_models.Customer

sealed class CustomersState {
    data class Success(val data: List<Customer>) : CustomersState()
    data class Error(val exception: Exception) : CustomersState()
    object Loading : CustomersState()
}
