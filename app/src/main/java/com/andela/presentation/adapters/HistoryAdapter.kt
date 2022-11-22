package com.andela.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andela.currencyexcercise.R
import com.andela.domain.entity.Conversion

class HistoryAdapter(
    private val mList: List<Conversion>
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_details, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel = mList[position]
        holder.rateTv.text = "${itemViewModel.rate}"
        holder.baseTv.text = itemViewModel.baseCurrency
        holder.convertTV.text = itemViewModel.convertedCurrency
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val baseTv: TextView = itemView.findViewById(R.id.baseTv)
        val convertTV: TextView = itemView.findViewById(R.id.convertTV)
        val rateTv: TextView = itemView.findViewById(R.id.rateTv)
    }
}
