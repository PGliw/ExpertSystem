package com.pwr.expertsystem


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pwr.expertsystem.adapters.ResultsAdapter
import com.pwr.expertsystem.business_logic.Rule
import kotlinx.android.synthetic.main.fragment_results.*

/**
 * A simple [Fragment] subclass.
 */
class ResultsFragment : Fragment() {

    private val mainViewModel by lazy{
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Clear the previous rule selection if any
        mainViewModel.ruleToExplain = null

        recycler_fragment_results_risk_groups.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ResultsAdapter(mainViewModel.interfaceEngine.riskGroupsRules, this::onRuleClick)
        recycler_fragment_results_risk_groups.adapter = adapter
        button_results_fragment_close.setOnClickListener {
            findNavController().navigate(R.id.action_resultsFragment_to_startFragment)
        }
    }

    private fun onRuleClick(rule: Rule){
        mainViewModel.ruleToExplain = rule
        findNavController().navigate(R.id.action_resultsFragment_to_explanationFragment)
    }
}
