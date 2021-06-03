package co.cartrack.za.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.cartrack.za.model.ListItemData
import co.cartrack.za.model.OmdbResponse
import co.cartrack.za.model.Search
import co.cartrack.za.repository.OmdbRepository
import co.cartrack.za.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class OmdbViewModel(
    private val omdbRepository: OmdbRepository
) : ViewModel(){

    var omdbData: MutableLiveData<Resource<OmdbResponse>> = MutableLiveData()
    var omdbResponse: OmdbResponse? = null


    var itemData: MutableLiveData<Resource<ListItemData>> = MutableLiveData()
    var itemDataResponse: ListItemData? = null


    fun getAllData(type: String, s: String) = viewModelScope.launch {
        omdbData.postValue(Resource.Loading())
        val response = omdbRepository.getAllData(type, s)
        omdbData.postValue(handleOmdbResponse(response))

    }

    fun getItemData(id: String) = viewModelScope.launch {
        itemData.postValue(Resource.Loading())
        val response = omdbRepository.getItemData(id)
        itemData.postValue(handleItemResponse(response))
    }

    private fun handleOmdbResponse(response: Response<OmdbResponse>) : Resource<OmdbResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if (omdbResponse == null){
                    omdbResponse = resultResponse
                }
                else{
                    val oldSearch = omdbResponse?.Search
                    val newSearch = resultResponse.Search
                    oldSearch?.addAll(newSearch)
                }
                return Resource.Success(omdbResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleItemResponse(response: Response<ListItemData>) : Resource<ListItemData>{
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->

                if (itemDataResponse == null){
                    itemDataResponse = resultResponse
                }
                return Resource.Success(itemDataResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())

    }

    fun saveSearchResults(searchResults: Search) = viewModelScope.launch {
       omdbRepository.upsert(searchResults)
    }

    fun getSavedResults() = omdbRepository.getSavedResults()

    fun deleteResults(searchResults: Search) = viewModelScope.launch {
        omdbRepository.delete(searchResults)
    }

}