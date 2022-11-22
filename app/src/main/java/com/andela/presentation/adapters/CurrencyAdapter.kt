package com.andela.presentation.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.andela.domain.entity.Currency

class CurrencyAdapter(context: Context, textViewResourceId: Int, private val values: List<Currency>) : ArrayAdapter<Currency>(context, textViewResourceId, values) {

    override fun getCount() = values.size
    override fun getItem(position: Int) = values[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        label.text = values[position].title
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.text = values[position].title
        return label
    }
}