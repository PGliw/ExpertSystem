package com.pwr.expertsystem.business_logic

class InterfaceEngine {
    private val riskGroupsInterview = RiskGroupsInterview()
    private var diseasesInterview: IInterview? = null
    private var currentInterview: IInterview = riskGroupsInterview

    val riskGroupsRules = riskGroupsInterview.rules
    val diseasesRules = diseasesInterview?.rules

    /**
     * Returns the next question to ask within Risk Groups questions group.
     * If there are no question left it returns null.
     */
    fun getNextRuleAndQuestion(): Pair<Rule?, Question<out Any>?> {
        var pair = currentInterview.nextRuleAndQuestion()
        // if there is no further questions in Risk Group Interview, than
        // pass the implied facts to diseasesInterview and set currentInterview to it
        if (pair == Pair(null, null) && currentInterview is RiskGroupsInterview) {
            val dInterview = DiseasesInterview(currentInterview.conclusions)
            diseasesInterview = dInterview
            currentInterview = dInterview
            pair = currentInterview.nextRuleAndQuestion()
        }
        return pair
    }
}