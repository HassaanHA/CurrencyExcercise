package com.andela.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andela.core.utils.StateHandler
import com.andela.currencyexcercise.databinding.FragmentDetailsBinding
import com.andela.domain.entity.Conversion
import com.andela.presentation.adapters.HistoryAdapter
import com.andela.presentation.adapters.TopCurrencyAdapter
import com.andela.presentation.viewModels.MainViewModel

class CurrencyDetailsFragment: Fragment() {

    private val binding by lazy { FragmentDetailsBinding.inflate(layoutInflater)}
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = mainViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupObserver()
        mainViewModel.getHistory()
        mainViewModel.getTopCurrencies()
    }

    // adapter methods:-----------------------------------------------------------------------------
    private fun setAdapter(list: List<Conversion>) {
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = HistoryAdapter(list)
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setupObserver() {

        lifecycleScope.launchWhenStarted {
            mainViewModel.topCurrencyStateFlow.collect {
                when (it) {
                    is StateHandler.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is StateHandler.Failure -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is StateHandler.Success -> {
                        binding.progressBar.visibility = View.GONE
                        setTopCurrencyAdapter(it.data)
                    }
                    is StateHandler.Empty -> {
                    }
                }
            }
        }

        mainViewModel.transactions.observe(viewLifecycleOwner) {
            setAdapter(it)
        }
    }

    private fun setTopCurrencyAdapter(list: List<Conversion>?){
        list?.let {
            binding.rvTopCurrencies.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = TopCurrencyAdapter(list)

                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )
            }
        }
    }


}