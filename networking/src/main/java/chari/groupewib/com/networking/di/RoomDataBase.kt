package ghoudan.anfaSolution.com.networking.di

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import chari.groupewib.com.app_models.Item
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.app_models.User
import ghoudan.anfaSolution.com.networking.api.RoomDao
import ghoudan.anfaSolution.com.networking.offline.Converters


@Database(
    entities = [SupplierAnfa::class, CustomerAnfa::class, Item::class, User::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class RoomDataBase : RoomDatabase() {
    abstract fun roomDao(): RoomDao
}
