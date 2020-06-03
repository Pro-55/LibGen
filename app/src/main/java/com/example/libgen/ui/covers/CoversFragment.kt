package com.example.libgen.ui.covers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.libgen.BaseFragment
import com.example.libgen.R
import com.example.libgen.databinding.FragmentCoversBinding
import com.example.libgen.utli.extensions.getMainViewModel
import com.example.libgen.utli.extensions.glide

class CoversFragment : BaseFragment() {

    companion object {
        private val TAG = CoversFragment::class.java.simpleName
    }

    //Global
    private lateinit var binding: FragmentCoversBinding
    private val viewModel by lazy { requireActivity().getMainViewModel() }
    private val args by navArgs<CoversFragmentArgs>()
    private val glide by lazy { glide() }
    private var adapter: CoversAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_covers, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

        setupRecyclerView()

        viewModel.getCovers(args.id).observe(viewLifecycleOwner, Observer { adapter?.swapData(it) })

    }

    private fun setListeners() {

        binding.imgBtnBack.setOnClickListener { onBackPressed() }

    }

    private fun setupRecyclerView() {

        adapter = CoversAdapter(glide)

        binding.recyclerCovers.layoutManager =
            LinearLayoutManager(requireContext(), HORIZONTAL, false)
        binding.recyclerCovers.adapter = adapter

        PagerSnapHelper().attachToRecyclerView(binding.recyclerCovers)

    }

}