package com.example.quotesmvvm.presentation.quotes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotesmvvm.R
import com.example.quotesmvvm.data.repository.QuoteRepositoryImpl
import com.example.quotesmvvm.databinding.FragmentQuotesBinding
import com.example.quotesmvvm.domain.usecase.GetQuotesUseCase
import com.example.quotesmvvm.core.hide
import com.example.quotesmvvm.core.show

class QuotesFragment : Fragment(R.layout.fragment_quotes) {
    private var _binding : FragmentQuotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var vm: QuotesViewModel
    private val adapter = QuotesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentQuotesBinding.bind(view)
        val repository = QuoteRepositoryImpl()
        val getQuotesUseCase = GetQuotesUseCase(repository)

        vm = ViewModelProvider(
            this,
            QuotesViewModel.Factory(getQuotesUseCase)
        ).get(QuotesViewModel::class.java)

        binding.recyclerQuotes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerQuotes.adapter = adapter

        observeUi()
        binding.textError.setOnClickListener { vm.refresh() }
    }

    private fun observeUi() = vm.ui.observe(viewLifecycleOwner) { state ->
        when (state) {
            is UiState.Loading -> {
                binding.progress.show()
                binding.textError.hide()
                binding.recyclerQuotes.hide()
            }
            is UiState.Error -> {
                binding.progress.hide()
                binding.textError.show()
                binding.recyclerQuotes.hide()
                binding.textError.text = "Error: ${state.message}\nTap to retry."
            }
            is UiState.Success -> {
                binding.progress.hide()
                binding.textError.hide()
                binding.recyclerQuotes.show()
                adapter.submit(state.data)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}