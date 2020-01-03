package com.pwr.expertsystem


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pwr.expertsystem.adapters.ConditionsAdapter
import com.pwr.expertsystem.business_logic.Rule
import com.pwr.expertsystem.utils.getConditionsDescriptions
import kotlinx.android.synthetic.main.fragment_explanation.*

/**
 * A simple [Fragment] subclass.
 */
class ExplanationFragment : Fragment() {

    companion object{
        const val TAG = "ExplanationFragment"
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explanation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderRule(mainViewModel.ruleToExplain)
    }

    private fun renderRule(rule: Rule?) {
        if (rule == null) {
            findNavController().navigateUp()
            return
        }
        val conclusionText = "${rule.conclusion.label} = ${rule.conclusion.value}"
        text_fragment_explanation_rule_conclusion.text = conclusionText
        list_fragment_explanation_conditions.adapter = ConditionsAdapter(
            requireContext(),
            rule.conditions.toTypedArray()
        )
    }
}
