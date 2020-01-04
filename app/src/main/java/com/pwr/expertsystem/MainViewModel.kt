package com.pwr.expertsystem

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pwr.expertsystem.business_logic.InterfaceEngine
import com.pwr.expertsystem.business_logic.Rule

class MainViewModel : ViewModel() {
    var interfaceEngine = InterfaceEngine()
        private set

    var ruleToExplain: Rule? = null

    var rulesListToDisplay: List<Rule>? = interfaceEngine.riskGroupsRules
        private set

    fun getInterviewTitle(interviewIndex: Int) = when (interviewIndex) {
        0 -> "Grupy ryzyka"
        else -> "Choroby"
    }

    fun setRulesToDisplay(interviewIndex: Int) {
        // Log.d("setRulesToDisplay", "interviewIndex = $interviewIndex")
        rulesListToDisplay = when (interviewIndex) {
            0 -> interfaceEngine.riskGroupsRules
            else -> interfaceEngine.diseasesRules
        }
        Log.d("setRulesToDisplay", rulesListToDisplay.toString())
    }

    fun clearResults() {
        interfaceEngine = InterfaceEngine()
    }
}