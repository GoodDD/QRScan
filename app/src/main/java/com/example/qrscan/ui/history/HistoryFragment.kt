package com.example.qrscan.ui.history

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qrcodescan.ListItem
import com.example.qrscan.R
import com.example.qrscan.RecycleViewAdapter
import com.example.qrscan.SharedViewModel

class HistoryFragment : Fragment(), RecycleViewAdapter.OnItemClickListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecycleViewAdapter
    private lateinit var actionBar: ActionBar

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        actionBar = (activity as AppCompatActivity).supportActionBar!!
        actionBar?.hide()

        adapter = RecycleViewAdapter(sharedViewModel.arrayData, this)

        val root = inflater.inflate(R.layout.fragment_history, container, false)

        recyclerView = root.findViewById(R.id.history_list)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
        }

        recyclerView.adapter = adapter

        sharedViewModel.multiMode.value = false
        for (item in sharedViewModel.arrayData) {
            item.setSelected(false)
        }

        return root
    }

    override fun onItemClick(position: Int) {

        if (sharedViewModel.multiMode.value == true){
            Log.e(null, "multi mode - on")
            val clickedItem: ListItem = sharedViewModel.arrayData[position]

            if (clickedItem.isSelected()){
                clickedItem.setSelected(false)
            }else{
                clickedItem.setSelected(true)
            }


            var on: Boolean = true

            for (item in sharedViewModel.arrayData) {
                if (item.isSelected()){
                    on = true
                    recyclerView.adapter?.notifyDataSetChanged()
                    return
                }else{
                    on = false
                }
            }

            if (!on) {
                sharedViewModel.multiMode.value = false
                actionBar.hide()
            }

            recyclerView.adapter?.notifyDataSetChanged()

        }else{

            val clickedItem: ListItem = sharedViewModel.arrayData[position]
            recyclerView.adapter?.notifyDataSetChanged()

            sharedViewModel.barcode.value = clickedItem.title
            sharedViewModel.barcodeScanTime.value = clickedItem.date

            sharedViewModel.navigatedFrom.value = "historyFragment"
            sharedViewModel.selectedBarcode.value = clickedItem

            findNavController().navigate(R.id.action_navigation_history_to_navigation_qrcode)

        }
    }

    override fun onLongItemClick(position: Int) {
        actionBar.show()
        actionBar.title = "multi-select"
        sharedViewModel.multiMode.value = true

        val clickedItem: ListItem = sharedViewModel.arrayData[position]
        clickedItem.setSelected(true)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.item_delete -> {

                val itemsToRemove = ArrayList<ListItem>()

                for (item in sharedViewModel.arrayData) {
                    if (item.isSelected()) {
                        itemsToRemove.add(item)
                    }else{
                        continue
                    }
                }

                actionBar.hide()
                sharedViewModel.multiMode.value = false

                sharedViewModel.arrayData.removeAll(itemsToRemove)
                recyclerView.adapter?.notifyDataSetChanged()

            }
        }

        return super.onOptionsItemSelected(item)
    }

}
