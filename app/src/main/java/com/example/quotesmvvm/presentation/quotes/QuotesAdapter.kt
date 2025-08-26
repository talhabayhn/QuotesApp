package com.example.quotesmvvm.presentation.quotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesmvvm.databinding.ItemQuoteBinding
import com.example.quotesmvvm.domain.model.Quote

class QuotesAdapter: RecyclerView.Adapter<QuotesAdapter.VH>() {

    private val items = mutableListOf<Quote>()

    fun submit(list: List<Quote>) {
        items.clear(); items.addAll(list); notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesAdapter.VH {
        val binding =ItemQuoteBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return VH(binding)
    }
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size

    class VH(private val binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(q: Quote) {
            binding.textContent.text = "“${q.content}”"
            binding.textAuthor.text = "— ${q.author}"
        }
    }

}