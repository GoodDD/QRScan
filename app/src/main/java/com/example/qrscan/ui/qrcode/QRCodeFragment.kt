package com.example.qrscan.ui.qrcode

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.qrcodescan.ListItem
import com.example.qrscan.R
import com.example.qrscan.SharedViewModel
import org.w3c.dom.Text

class QRCodeFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        val root = inflater.inflate(R.layout.fragment_qrcode, container, false)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.show()

        val barcodeTitle = root.findViewById<TextView>(R.id.barcode_title)
        val barcodeDate = root. findViewById<TextView>(R.id.barcode_date)

        barcodeTitle.text = sharedViewModel.barcode.value
        barcodeDate.text = sharedViewModel.barcodeScanTime.value

        saveScanResult(sharedViewModel)

        return root
    }

    private fun saveScanResult(sharedViewModel: SharedViewModel) {
        when (sharedViewModel.navigatedFrom.value) {
            "scanFragment" -> {
                val barcode =  sharedViewModel.barcode.value
                val barcodeScanTime = sharedViewModel.barcodeScanTime.value
                val li = ListItem(barcode, barcodeScanTime)
                sharedViewModel.arrayData.add(li)
                sharedViewModel.selectedBarcode.value = li
                return
            }
            "historyFragment" -> {
                return
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.item_delete -> {
                val li = sharedViewModel.selectedBarcode.value
                sharedViewModel.arrayData.remove(li)
                findNavController().popBackStack()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}