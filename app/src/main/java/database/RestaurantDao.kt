package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao

interface RestaurantDao {
    @Insert
    fun insertRes(restaurantEntity: RestaurantEntity)

    @Delete
    fun deleteRes(restaurantEntity: RestaurantEntity)

    @Query("SELECT*FROM favRes")
    fun getAllRes():List<RestaurantEntity>

    @Query("SELECT*FROM favRes WHERE restaurant_id=:restaurantId")
    fun getRestaurantId(restaurantId:String):RestaurantEntity
}