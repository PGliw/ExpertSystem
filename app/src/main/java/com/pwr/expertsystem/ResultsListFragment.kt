package com.pwr.expertsystem


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pwr.expertsystem.adapters.ResultsAdapter
import com.pwr.expertsystem.business_logic.Rule
import com.pwr.expertsystem.utils.snack
import kotlinx.android.synthetic.main.fragment_results_list.*

/**
 * A simple [Fragment] subclass.
 */
class ResultsListFragment : Fragment() {

    companion object{
        const val TAG = "ResultsListFragment"
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Clear the previous rule selection if any
        mainViewModel.ruleToExplain = null
        recycler_fragment_results_list_risk_groups.layoutManager =
            LinearLayoutManager(requireContext())
    }

    override fun onResume() {
        super.onResume()
        // Rendering the rules must happen in onResume method
        // Otherwise it will be not called after popping up from ExplanationFragment to ResultsFrag.
        when (val rules = mainViewModel.rulesListToDisplay) {
            null -> snack("Nie zaczęto wnioskowania w tym zakresie")
            else -> renderRulesList(rules)
        }
    }

    private fun renderRulesList(rules: List<Rule>) {
        Log.d(TAG, "rendering: $rules")
        val adapter = ResultsAdapter(rules, this::onRuleClick)
        recycler_fragment_results_list_risk_groups.adapter = adapter
    }

    private fun onRuleClick(rule: Rule) {
        mainViewModel.ruleToExplain = rule
        val mainNavView = requireActivity().findViewById<View>(R.id.nav_host_fragment)
        Navigation.findNavController(mainNavView)
            .navigate(R.id.action_resultsFragment_to_explanationFragment)
    }
}
