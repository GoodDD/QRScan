package com.example.qrscan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qrcodescan.ListItem
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import java.util.*
import kotlin.collections.ArrayList

class SharedViewModel: ViewModel() {



    val multiMode = MutableLiveData<Boolean>()

    // Nav
    val navigatedFrom = MutableLiveData<String>()

    // fun and var for list
    // Variable for history list
    val arrayData = ArrayList<ListItem>()

    /*fun addToArrayData(listItem: ListItem) {
        arrayData.add(listItem)
    }

    fun removeFromArrayData(listItem: ListItem?) {
        arrayData.remove(listItem)
    }*/

    // barcode var
    val barcode = MutableLiveData<String>()

    // barcode scan time var
    val barcodeScanTime = MutableLiveData<String>()

    // Selected barcode to remove
    // Maybe change single delete system in future
    val selectedBarcode = MutableLiveData<ListItem>()

}