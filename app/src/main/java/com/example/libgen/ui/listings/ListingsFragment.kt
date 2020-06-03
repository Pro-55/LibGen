package com.example.libgen.ui.listings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.libgen.BaseFragment
import com.example.libgen.R
import com.example.libgen.databinding.FragmentListingsBinding
import com.example.libgen.utli.extensions.getMainViewModel
import com.example.libgen.utli.extensions.glide

class ListingsFragment : BaseFragment() {

    companion object {
        private val TAG = ListingsFragment::class.java.simpleName
    }

    //Global
    private lateinit var binding: FragmentListingsBinding
    private val viewModel by lazy { requireActivity().getMainViewModel() }
    private val glide by lazy { glide() }
    private var adapter: BooksAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

        setupRecyclerView()

        viewModel.getBooks()
        viewModel.books.observe(viewLifecycleOwner, Observer { adapter?.swapData(it) })

    }

    private fun setListeners() {

        binding.fabAddNewEntry.setOnClickListener { openNewEntry() }

    }

    private fun setupRecyclerView() {
        adapter = BooksAdapter(glide)
        adapter?.listener = object : BooksAdapter.Listener {
            override fun onCLick(_id: String) {
                openCovers(_id)
            }
        }

        binding.recyclerBooks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerBooks.adapter = adapter
    }

    private fun openNewEntry() {
        val action = ListingsFragmentDirections.navigateListingsToNewEntry()
        findNavController().navigate(action)
    }

    private fun openCovers(_id: String) {
        val action = ListingsFragmentDirections.navigateListingsToCovers(_id)
        findNavController().navigate(action)
    }

}