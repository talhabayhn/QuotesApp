package com.example.quotesmvvm.presentation.quotes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotesmvvm.R
import com.example.quotesmvvm.data.repository.QuoteRepositoryImpl
import com.example.quotesmvvm.databinding.FragmentQuotesBinding
import com.example.quotesmvvm.domain.usecase.GetQuotesUseCase
import com.example.quotesmvvm.core.hide
import com.example.quotesmvvm.core.show
import com.example.quotesmvvm.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.launch
import com.example.quotesmvvm.presentation.quotes.detail.QuoteDetailFragment

class QuotesFragment : Fragment(R.layout.fragment_quotes) {
    private var _binding : FragmentQuotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var vm: QuotesViewModel
    private lateinit var adapter: QuotesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentQuotesBinding.bind(view)
        val repository = QuoteRepositoryImpl()
        val getQuotes = GetQuotesUseCase(repository)
        val toggleFav = ToggleFavoriteUseCase(repository)

        vm = ViewModelProvider(this, QuotesViewModel.Factory(getQuotes, toggleFav))[QuotesViewModel::class.java]

        adapter = QuotesAdapter(
            onClick = { vm.onEvent(QuotesContract.Event.ClickItem(it.id)) },
            onToggleFavorite = { vm.onEvent(QuotesContract.Event.ToggleFavorite(it.id)) }
        )

        binding.recyclerQuotes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerQuotes.adapter = adapter

        // search input
        binding.editSearch.doOnTextChanged { text, _, _, _ ->
            vm.onEvent(QuotesContract.Event.Search(text?.toString().orEmpty()))
        }
        binding.textError.setOnClickListener { vm.onEvent(QuotesContract.Event.Refresh) }

        binding.buttonLoadMore.setOnClickListener {
            vm.onEvent(QuotesContract.Event.LoadMore)
        }

        // state
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collect { st ->
                    binding.progress.isVisible = st.isLoading
                    binding.textError.isVisible = st.error != null
                    binding.recyclerQuotes.isVisible = !st.isLoading && st.error == null
                    binding.textError.text = st.error ?: ""
                    if (st.error == null) adapter.submit(st.items)
                }
            }
        }

        // effects
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.effect.collect { eff ->
                    when (eff) {
                        is QuotesContract.Effect.NavigateToDetail -> {
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, QuoteDetailFragment.newInstance(eff.id))
                                .addToBackStack("QuoteDetail")
                                .commit()
                        }
                        is QuotesContract.Effect.ShowMessage ->
                            Toast.makeText(requireContext(), eff.text, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}