package com.pwr.expertsystem

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pwr.expertsystem.business_logic.InterfaceEngine
import com.pwr.expertsystem.business_logic.Rule

class MainViewModel : ViewModel() {
    /**
     * Interface engine used for examination and reasoning
     */
    var interfaceEngine = InterfaceEngine()
        private set

    /**
     * Rule to be currently displayed on the screen
     */
    var ruleToExplain: Rule? = null

    /**
     * Rules that should be currently displayed on the screen
     */
    var rulesListToDisplay: List<Rule>? = interfaceEngine.riskGroupsRules
        private set

    /**
     * Gets the title of the interview denoted by given index
     * @param interviewIndex - 0 if risk group title should be displayed or 1 when diseases title
     */
    fun getInterviewTitle(interviewIndex: Int) = when (interviewIndex) {
        0 -> "Grupy ryzyka"
        else -> "Choroby"
    }

    /**
     * Sets the rules that should be displayed
     * @param interviewIndex - 0 if risk group rules should be displayed or 1 when diseases rules
     */
    fun setRulesToDisplay(interviewIndex: Int) {
        rulesListToDisplay = when (interviewIndex) {
            0 -> interfaceEngine.riskGroupsRules
            else -> interfaceEngine.diseasesRules
        }
        Log.d("setRulesToDisplay", rulesListToDisplay.toString())
    }

    /**
     * Clears the reasoning session history
     */
    fun clearResults() {
        interfaceEngine = InterfaceEngine()
    }
}