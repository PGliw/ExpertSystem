package com.pwr.expertsystem.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.pwr.expertsystem.R
import com.pwr.expertsystem.business_logic.Checked
import com.pwr.expertsystem.business_logic.Condition
import com.pwr.expertsystem.business_logic.NotChecked
import com.pwr.expertsystem.business_logic.SkippedCondition

class ConditionsAdapter(context: Context, private val items: Array<Condition<out Any>>) :
    ArrayAdapter<Condition<out Any>>(context, -1 , items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = LayoutInflater.from(context).inflate(R.layout.condition_item, parent, false)
        val icon = rowView.findViewById<ImageView>(R.id.image_condition_item_icon)
        val description = rowView.findViewById<TextView>(R.id.text_condition_item_title)
        val imageSrc = when(val status = items[position].conditionStatus){
            NotChecked -> R.drawable.ic_help_outline_black_24dp
            SkippedCondition -> R.drawable.ic_remove_black_24dp
            is Checked -> if(status.result) R.drawable.ic_check_circle_black_24dp else R.drawable.ic_clear_black_24dp
        }
        icon.setImageResource(imageSrc)
        description.text = items[position].description
        return rowView
    }
}