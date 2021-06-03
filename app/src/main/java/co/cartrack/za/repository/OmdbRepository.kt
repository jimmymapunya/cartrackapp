package co.cartrack.za.repository


import co.cartrack.za.api.ApiService.Companion.api
import co.cartrack.za.db.OmdbDatabase
import co.cartrack.za.model.Search

class OmdbRepository(
    val db: OmdbDatabase
) {

     suspend fun getAllData(type: String, search: String) =
        api.getAllData(type, search)

    suspend fun getItemData(id: String) =
        api.getItemData(id)

    suspend fun upsert(searchResults: Search) = db.getOmdbDao().upsert(searchResults)

    fun getSavedResults() = db.getOmdbDao().getAllData()

    suspend fun delete(searchResults: Search) = db.getOmdbDao().delete(searchResults)
}