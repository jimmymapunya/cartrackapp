package co.cartrack.za.api

import co.cartrack.za.model.ListItemData
import co.cartrack.za.model.OmdbResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("?apikey=22fe2f1e")
    suspend fun getAllData(
        @Query("type")
        type: String,
        @Query("s")
        search: String

    ): Response<OmdbResponse>


    @GET("?apikey=22fe2f1e")
    suspend fun getItemData(
        @Query("i")
        id: String

    ): Response<ListItemData>

}
