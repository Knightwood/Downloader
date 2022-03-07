package com.kiylx.toolslib

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * 在更改文字之前移除监听器，在更改文字之后重新添加监听器。以此避免成为永动机、不停触发文字更改的监听。
 */
abstract class MyTextWatcher(private val editText: EditText) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        editText.removeTextChangedListener(this)
        editText.text = checkText(s)
        editText.addTextChangedListener(this)
    }

   abstract fun checkText(s: Editable?): Editable?
}