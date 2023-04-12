package ghoudan.anfaSolution.com.networking.repository

import ghoudan.anfaSolution.com.app_models.Customer
import ghoudan.anfaSolution.com.networking.api.EpApi
import ghoudan.anfaSolution.com.networking.entity.mapper.toAppModel
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@Reusable
class CustomerRepository @Inject constructor(
    private val api: EpApi
) {

//    fun getCustomer(customerId: Int): Flow<Customer> {
//        return flow {
//            val url = "customers/$customerId"
//            val result = api.getCustomer(url)
//            emit(result.data.toAppModel())
//        }
//    }
}
