package com.bignerdranch.android.mydialer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecycleAdapter(private val context: Context, private val contacts: Array<Contact>) : RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {
    class ViewHolder (itemView: View) : RecyclerView.ViewHolder (itemView) {
        var name : TextView = itemView.findViewById(R.id.textName)
        var phone : TextView = itemView.findViewById(R.id.textPhone)
        var type : TextView = itemView.findViewById(R.id.textType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rview_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = contacts[position]

        holder.name.text = data.name
        holder.phone.text = data.phone
        holder.type.text = data.type
    }
}