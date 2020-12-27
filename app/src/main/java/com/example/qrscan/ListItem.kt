package com.example.qrcodescan

class ListItem {

    var title: String? = null
    var date: String? = null

    private var isSelected: Boolean = false

    constructor(title: String?, date: String?) {
        this.title = title
        this.date = date
    }

    public fun setSelected(selected: Boolean) {
        isSelected = selected
    }

    public fun isSelected(): Boolean {
        return isSelected
    }

}