package co.cartrack.za.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.cartrack.za.R
import co.cartrack.za.adapter.OmdbAdapter
import co.cartrack.za.databinding.FragmentSavedOmdbBinding
import co.cartrack.za.view.OmdbActivity
import co.cartrack.za.viewmodel.OmdbViewModel


class SavedOmdbFragment : Fragment(R.layout.fragment_saved_omdb) {

    private lateinit var binding: FragmentSavedOmdbBinding

    lateinit var viewModel: OmdbViewModel
    lateinit var omdbAdapter: OmdbAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedOmdbBinding.bind(view)
        viewModel = (activity as OmdbActivity).viewModel

        setupRecyclerView()
        initListener()
        getSavedData()
    }

    private fun initListener(){
        omdbAdapter.setOnItemClickListener {

            val bundle = Bundle().apply {
                putSerializable("search", it)
            }
            findNavController().navigate(
                R.id.action_savedOmdbFragment_to_omdbItemFragment,
                bundle
            )
        }

        binding.imgClose.setOnClickListener {
            deleteList()
        }
    }

    private fun setupRecyclerView() {
        omdbAdapter = OmdbAdapter()
        binding.rvSavedRecycler.apply {
            adapter = omdbAdapter
            layoutManager = LinearLayoutManager(context)
            val decoration = DividerItemDecoration(activity, LinearLayout.VERTICAL)
            addItemDecoration(decoration)
        }
    }

    private fun getSavedData(){

        viewModel.getSavedResults().observe(viewLifecycleOwner, Observer { search ->

            if (search.isNotEmpty()){
                omdbAdapter.differ.submitList(search)
                binding.tvStatus.visibility = View.INVISIBLE
                binding.imgClose.visibility = View.VISIBLE
            }
            else{
                binding.tvStatus.visibility = View.VISIBLE
                binding.tvStatus.text = getString(R.string.status_message)
                    binding.imgClose.visibility = View.INVISIBLE
            }
        })
    }

    //delete data from database
    private fun deleteList(){

        val list = omdbAdapter.differ.currentList
        for (i in 0 until list.size) {
            viewModel.deleteResults(list[i])
        }
    }

}