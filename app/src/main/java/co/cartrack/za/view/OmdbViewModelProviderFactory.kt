package co.cartrack.za.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.cartrack.za.repository.OmdbRepository
import co.cartrack.za.viewmodel.OmdbViewModel

class OmdbViewModelProviderFactory (
    val omdbRepository: OmdbRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OmdbViewModel(omdbRepository) as T
    }
}