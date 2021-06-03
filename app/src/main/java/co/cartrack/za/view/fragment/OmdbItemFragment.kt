package co.cartrack.za.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import co.cartrack.za.R
import co.cartrack.za.utils.Constants.Companion.DEBUG
import co.cartrack.za.utils.Constants.Companion.hideProgressBar
import co.cartrack.za.utils.Constants.Companion.showProgressBar
import co.cartrack.za.utils.Resource
import co.cartrack.za.view.OmdbActivity
import co.cartrack.za.viewmodel.OmdbViewModel
import kotlinx.android.synthetic.main.fragment_item_layout.*

class OmdbItemFragment : Fragment(R.layout.fragment_item_layout) {

    lateinit var viewModel: OmdbViewModel
    val args: OmdbItemFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as OmdbActivity).viewModel

        getItemData(args.search.imdbID)

    }

    private fun getItemData(id: String){
        viewModel.getItemData(id)
        viewModel.itemData.observe(viewLifecycleOwner, Observer { response ->

            when(response){
                is Resource.Success ->{
                    response.data?.let { itemDataResponse ->
                        hideProgressBar(progressBar)

                        tvTitle.text = itemDataResponse.Title
                        tvYear.text = itemDataResponse.Year
                        tvRated.text = itemDataResponse.Rated
                        tvReleased.text = itemDataResponse.Released
                        tvRuntime.text = itemDataResponse.Runtime
                        tvLanguage.text = itemDataResponse.Language
                        tvCountry.text = itemDataResponse.Country

                    }
                }
                is Resource.Error -> {
                    hideProgressBar(progressBar)
                    response.message?.let { message ->
                        Log.e(DEBUG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar(progressBar)
                }
            }
        })
    }

}