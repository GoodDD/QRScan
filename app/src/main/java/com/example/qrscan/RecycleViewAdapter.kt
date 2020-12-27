package com.example.qrscan

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcodescan.ListItem

class RecycleViewAdapter(private val dataSet: ArrayList<ListItem>,
                         private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {


    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
            RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)), View.OnClickListener, View.OnLongClickListener {
        private var itemTitle: TextView? = null
        private var itemDate: TextView? = null


        init {
            itemTitle = itemView.findViewById(R.id.bcode_item_title)
            itemDate = itemView.findViewById(R.id.bcode_item_desc)

            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)

        }

        override fun onClick(v: View?) {

            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onLongItemClick(position)
            }

            return true
        }

        fun bind(listItem: ListItem) {
            itemTitle?.text = listItem.title
            itemDate?.text = listItem.date
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RecycleViewAdapter.ViewHolder, position: Int) {
        val listItem: ListItem = dataSet[position]
        if (listItem.isSelected()) {
            holder.itemView.setBackgroundColor(Color.CYAN)
        }else{
            holder.itemView.setBackgroundColor(Color.WHITE)
        }
        holder.bind(listItem)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onLongItemClick(position: Int)
    }

}