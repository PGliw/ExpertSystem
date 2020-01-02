package com.pwr.expertsystem.utils

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.snack(message: String) = Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()

val RadioGroup.selectedPosition: Int
    get() {
        val selectedView = findViewById<RadioButton>(checkedRadioButtonId)
        return indexOfChild(selectedView)
    }