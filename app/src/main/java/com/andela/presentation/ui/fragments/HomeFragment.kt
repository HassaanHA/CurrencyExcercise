package com.andela.presentation.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.andela.core.utils.NetworkUtil
import com.andela.core.utils.ObserveObserver
import com.andela.core.utils.StateHandler
import com.andela.currencyexcercise.R
import com.andela.currencyexcercise.databinding.FragmentHomeBinding
import com.andela.domain.entity.Currency
import com.andela.presentation.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val mainViewModel by activityViewModels<MainViewModel>()

    private lateinit var currencies: List<Currency>

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
        callWebServiceForQuestionList()
        setupObserver()
        setupClickListener()
    }

    private fun callWebServiceForQuestionList() {
        if (NetworkUtil.connectionAvailable(requireContext())) {
            mainViewModel.getAllCurrencies()
        } else {
            NetworkUtil.showDialog(requireContext())
        }
    }

    private fun setupClickListener() {
        binding.baseTv.setOnClickListener {
            settingCurrenciesList(true)
        }

        binding.convertedTv.setOnClickListener {
            settingCurrenciesList(false)
        }

        binding.baseEt.addTextChangedListener {
            if (!TextUtils.isEmpty(binding.baseEt.text.toString()) && !TextUtils.isEmpty(binding.convertedEt.text.toString())) {
                mainViewModel.convertDetails()
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.currencyStateFlow.collect {
                when (it) {
                    is StateHandler.Loading -> {
                        binding.detailsButton.isClickable = false
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is StateHandler.Failure -> {
                        binding.detailsButton.isClickable = true
                        binding.progressBar.visibility = View.GONE
                    }
                    is StateHandler.Success -> {
                        binding.detailsButton.isClickable = true
                        binding.progressBar.visibility = View.GONE
                        currencies = it.data!!
                    }
                    is StateHandler.Empty -> {
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            mainViewModel.currencyConvertedValue.collect {
                when (it) {
                    is StateHandler.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is StateHandler.Failure -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is StateHandler.Success -> {
                        binding.detailsButton.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                    }
                    is StateHandler.Empty -> {
                    }
                }
            }
        }
        mainViewModel.loadDetails.observe(viewLifecycleOwner, launchDetailsFragment)
    }

    private val launchDetailsFragment = ObserveObserver<Boolean> {
        if (it) {
            findNavController().navigate(R.id.listFragment)
        }
    }

    private fun settingCurrenciesList(isBase: Boolean) {
        val text = if (isBase) "Base" else "Conversion"
        val builderSingle: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builderSingle.setTitle("Select $text Currency:-")

        val arrayAdapter =
            ArrayAdapter<Currency>(requireContext(), android.R.layout.select_dialog_singlechoice)
        arrayAdapter.addAll(currencies)

        builderSingle.setNegativeButton("cancel"
        ) { dialog, _ -> dialog.dismiss() }

        builderSingle.setAdapter(arrayAdapter
        ) { _, index ->
            val strName = arrayAdapter.getItem(index)?.title ?: ""
            if (isBase) {
                mainViewModel.from.postValue(strName)
            } else {
                mainViewModel.toText.postValue(strName)
            }
            mainViewModel.convertDetails()
        }
        builderSingle.show()
    }

}