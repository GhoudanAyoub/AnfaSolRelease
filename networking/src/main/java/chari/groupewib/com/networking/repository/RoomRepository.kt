package ghoudan.anfaSolution.com.networking.repository


import chari.groupewib.com.app_models.Item
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.networking.api.RoomDao
import dagger.Reusable
import ghoudan.anfaSolution.com.app_models.User
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@Reusable
class RoomRepository @Inject constructor(
    private val roomDao: RoomDao
) {
    suspend fun insertUser(users: List<User>) {
        roomDao.insertUser(users)
    }

     fun getAllUsers(): Flow<List<User> >{
        return roomDao.getAllUsers()
    }

    suspend fun insertCustomers(customerAnfa: List<CustomerAnfa>) {
        roomDao.insertCustomers(customerAnfa)
    }

     fun getAllCustomers(): Flow<List<CustomerAnfa> >{
        return roomDao.getAllCustomers()
    }

    fun getCustomerByID(code: String): CustomerAnfa {
        return roomDao.getCustomerByID(code.toInt())
    }

    fun getSupplierByID(code: String): SupplierAnfa {
        return roomDao.getSupplierByID(code.toInt())
    }

    suspend fun insertSuppliers(supplierAnfa: List<SupplierAnfa>) {
        roomDao.insertSuppliers(supplierAnfa)
    }

     fun getAllSuppliers(): Flow<List<SupplierAnfa>> {
        return roomDao.getAllSuppliers()
    }
    fun getItemByID(code: String): Item {
        return roomDao.getItemByID(code.toInt())
    }

    suspend fun insertItems(item: List<Item>) {
        roomDao.insertItems(item)
    }

     fun getAllItems(): Flow<List<Item>> {
        return roomDao.getAllItems()
    }
}
