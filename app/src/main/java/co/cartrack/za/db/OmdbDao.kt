package co.cartrack.za.db

import androidx.lifecycle.LiveData
import androidx.room.*
import co.cartrack.za.model.Search

@Dao
interface OmdbDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(search: Search): Long

    @Query("SELECT * from search_results_tbl")
    fun getAllData(): LiveData<List<Search>>

    @Delete
    suspend fun delete(search: Search)
}