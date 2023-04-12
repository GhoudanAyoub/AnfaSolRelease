package ghoudan.anfaSolution.com.networking.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import chari.groupewib.com.app_models.Item
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.app_models.User
import kotlinx.coroutines.flow.Flow


@Dao
interface RoomDao {

    @Query("SELECT * FROM User")
     fun getAllUsers(): Flow<List<User>>

    @Insert(onConflict = REPLACE)
    suspend fun insertUser(user: List<User>)

    @Query("SELECT * FROM CustomerAnfa")
     fun getAllCustomers(): Flow<List<CustomerAnfa>>


    @Query("SELECT * FROM CustomerAnfa where code = :code")
    fun getCustomerByID(code: Int): CustomerAnfa

    @Insert(onConflict = REPLACE)
    suspend fun insertCustomers(customerAnfa: List<CustomerAnfa>)

    @Query("SELECT * FROM SupplierAnfa")
     fun getAllSuppliers(): Flow<List<SupplierAnfa>>

    @Insert(onConflict = REPLACE)
    suspend fun insertSuppliers(supplierAnfa: List<SupplierAnfa>)

    @Query("SELECT * FROM SupplierAnfa where code = :code")
    fun getSupplierByID(code: Int): SupplierAnfa

    @Query("SELECT * FROM Item")
     fun getAllItems(): Flow<List<Item>>

    @Insert(onConflict = REPLACE)
    suspend fun insertItems(item: List<Item>)

    @Query("SELECT * FROM Item where code = :code")
    fun getItemByID(code: Int): Item

}
