package database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RestaurantEntity::class], version = 1)
abstract class ResDatabase:RoomDatabase() {
    abstract fun restaurantDao():RestaurantDao
}