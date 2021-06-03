package co.cartrack.za.view.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.cartrack.za.R
import co.cartrack.za.adapter.OmdbAdapter
import co.cartrack.za.databinding.FragmentSearchOmdbBinding
import co.cartrack.za.utils.Constants.Companion.DEBUG
import co.cartrack.za.utils.Constants.Companion.hideProgressBar
import co.cartrack.za.utils.Constants.Companion.showProgressBar
import co.cartrack.za.utils.Resource
import co.cartrack.za.view.OmdbActivity
import co.cartrack.za.viewmodel.OmdbViewModel

class SearchOmdbFragment : Fragment(R.layout.fragment_search_omdb),
    AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentSearchOmdbBinding

    private lateinit var type: String
    lateinit var omdbAdapter: OmdbAdapter
    lateinit var viewModel: OmdbViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchOmdbBinding.bind(view)
        viewModel = (activity as OmdbActivity).viewModel

        initSpinner()
        setupRecyclerView()
        initListener()

    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }


    private fun setupRecyclerView() {
        omdbAdapter = OmdbAdapter()
        binding.recyclerView.apply {
            adapter = omdbAdapter
            layoutManager = LinearLayoutManager(context)
            val decoration = DividerItemDecoration(activity, LinearLayout.VERTICAL)
            addItemDecoration(decoration)
        }
    }

    private fun getDataAllData(type: String, search: String) {

        if (isNetworkAvailable(context)){
            viewModel.getAllData(type, search)
            viewModel.omdbData.observe(viewLifecycleOwner, Observer { response ->

                when (response) {

                    is Resource.Success -> {
                        response.data?.let { omdbResponse ->
                            hideProgressBar(binding.progressBar)
                            omdbAdapter.differ.submitList(omdbResponse.Search)

                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar(binding.progressBar)
                        response.message?.let { message ->
                            Log.e(DEBUG, "An error occurred: $message")
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar(binding.progressBar)
                    }
                }
            })
        } else{
            Toast.makeText(context, "No network", Toast.LENGTH_LONG).show()
        }

    }

    private fun initListener() {

        binding.btnSearch.setOnClickListener {
            if (type != "Please select" && binding.edtSearch.text.toString() != "")
                getDataAllData(type, binding.edtSearch.text.toString())
            else
                Toast.makeText(context, "Please enter all the fields", Toast.LENGTH_LONG).show()
        }

        omdbAdapter.setOnItemClickListener {

            val bundle = Bundle().apply {
                putSerializable("search", it)
            }
            findNavController().navigate(
                R.id.action_searchOmdbFragment_to_omdbItemFragment,
                bundle
            )
        }
    }

    private fun initSpinner() {

        val adapter = context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.category,
                android.R.layout.simple_spinner_item
            )
        }

        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = this
    }

    override fun onStop() {
        super.onStop()

        val currentFragment = findNavController().currentDestination
        if (currentFragment?.id == R.id.searchOmdbFragment) {
            val list = omdbAdapter.differ.currentList
            for (i in 0 until list.size) {
                viewModel.saveSearchResults(list[i])
            }
        }
    }

    //Spinner
    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        type = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}