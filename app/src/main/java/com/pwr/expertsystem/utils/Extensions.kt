package com.pwr.expertsystem.utils

import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pwr.expertsystem.business_logic.Checked
import com.pwr.expertsystem.business_logic.NotChecked
import com.pwr.expertsystem.business_logic.Rule
import com.pwr.expertsystem.business_logic.SkippedCondition

fun Fragment.snack(message: String) = Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
fun Fragment.toast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

val RadioGroup.selectedPosition: Int
    get() {
        val selectedView = findViewById<RadioButton>(checkedRadioButtonId)
        return indexOfChild(selectedView)
    }

fun Rule.getSatisfiedConditionsDescriptions() = conditions.filter { it.conditionStatus == Checked(true) }.map { it.description }
fun Rule.getNotSatisfiedConditionsDescriptions() = conditions.filter { it.conditionStatus == Checked(false) }.map { it.description }
fun Rule.getSkippedConditionsDescriptions() = conditions.filter { it.conditionStatus == SkippedCondition }.map { it.description }
fun Rule.getNotCheckedConditionsDescriptions() = conditions.filter { it.conditionStatus == NotChecked }.map { it.description }
fun Rule.getConditionsDescriptions() = conditions.map { it.description }
fun Rule.getSatisfiedConditions() =  conditions.filter { it.conditionStatus == Checked(true) }


