package com.pwr.expertsystem.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.pwr.expertsystem.R
import com.pwr.expertsystem.business_logic.Rule
import com.pwr.expertsystem.utils.getConditionsDescriptions
import com.pwr.expertsystem.utils.getSatisfiedConditionsDescriptions

class ResultsAdapter(
    private val items: List<Rule>,
    private val onRuleClick: (Rule) -> Unit
) : RecyclerView.Adapter<ResultsAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val card = LayoutInflater.from(parent.context).inflate(
            R.layout.results_item,
            parent,
            false
        ) as MaterialCardView
        return ResultViewHolder(card)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) =
        holder.bind(items[position], onRuleClick)

    class ResultViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.text_results_item_title)
        private val subtitle = view.findViewById<TextView>(R.id.text_results_item_subtitle)
        private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_item)
        fun bind(rule: Rule, onRuleClick: (Rule) -> Unit) {
            title.text = rule.conclusion.value
            val satisfiedConditions = rule.getSatisfiedConditionsDescriptions()
            val allConditions = rule.getConditionsDescriptions()
            val progress = (satisfiedConditions.size.toFloat() / allConditions.size * 100).toInt()
            progressBar.progress = progress
            val additionalInfo = "${satisfiedConditions.size} na ${allConditions.size} przesłanek"
            subtitle.text = additionalInfo
            view.setOnClickListener {
                onRuleClick(rule)
            }
        }
    }

}