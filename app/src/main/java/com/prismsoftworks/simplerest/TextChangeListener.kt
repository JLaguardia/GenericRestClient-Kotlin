package com.prismsoftworks.simplerest

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View

class TextChangeListener(context: Context): TextWatcher {
    val mContext = context

    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
}