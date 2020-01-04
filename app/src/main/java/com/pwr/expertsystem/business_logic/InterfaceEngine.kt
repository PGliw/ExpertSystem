package com.pwr.expertsystem.business_logic

class InterfaceEngine {
    // TODO pass set of risk groups form riskGroupInterview
    private val riskGroupsInterview = RiskGroupsInterview()
    private val diseasesInterview = DiseasesInterview(setOf())
    private var currentInterview: IInterview = riskGroupsInterview

    val riskGroupsRules = riskGroupsInterview.rules
    val diseasesRules = diseasesInterview.rules

    /**
     * Returns the next question to ask within Risk Groups questions group.
     * If there are no question left it returns null.
     */
    fun getNextRuleAndQuestion(): Pair<Rule?, Question<out Any>?> {
        var pair = currentInterview.nextRuleAndQuestion()
        if (pair == Pair(null, null) && currentInterview is RiskGroupsInterview) {
            currentInterview = diseasesInterview
            pair = currentInterview.nextRuleAndQuestion()
        }
        return pair
    }
}