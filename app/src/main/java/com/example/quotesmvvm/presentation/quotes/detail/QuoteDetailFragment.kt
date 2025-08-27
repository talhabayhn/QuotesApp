package com.example.quotesmvvm.presentation.quotes.detail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.quotesmvvm.R
import com.example.quotesmvvm.data.repository.QuoteRepositoryImpl
import com.example.quotesmvvm.databinding.FragmentQuoteDetailBinding
import com.example.quotesmvvm.domain.usecase.GetQuoteByIdUseCase

class QuoteDetailFragment : Fragment(R.layout.fragment_quote_detail) {
    private var _binding: FragmentQuoteDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val KEY_ID = "key_id"
        fun newInstance(id: String) = QuoteDetailFragment().apply {
            arguments = bundleOf(KEY_ID to id)
        }
    }

    private lateinit var vm: QuoteDetailViewModel

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)
        _binding = FragmentQuoteDetailBinding.bind(v)

        val repo = QuoteRepositoryImpl()
        val getById = GetQuoteByIdUseCase(repo)
        vm = ViewModelProvider(this, QuoteDetailViewModel.Factory(getById))[QuoteDetailViewModel::class.java]

        val id = requireArguments().getString(KEY_ID)!!
        vm.ui.observe(viewLifecycleOwner) { st ->
            when (st) {
                QuoteDetailViewModel.UiState.Loading -> binding.progress.isVisible = true
                is QuoteDetailViewModel.UiState.Error -> {
                    binding.progress.isVisible = false
                    binding.textContent.text = "Error: ${st.msg}"
                }
                is QuoteDetailViewModel.UiState.Success -> {
                    binding.progress.isVisible = false
                    binding.textAuthor.text = "— ${st.quote.author}"
                    binding.textContent.text = "“${st.quote.content}”"
                }
            }
        }
        vm.load(id)
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}