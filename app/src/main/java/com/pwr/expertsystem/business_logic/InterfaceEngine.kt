package com.pwr.expertsystem.business_logic

/**
 * Class responsible for examination (reasoning)
 * <p> The actual data is loosely based on Internet. It was not consulted with any
 * doctor and MUST NOT be used as replacement of medical examination </p>
 * @property riskGroupsRules - rules implying if patient belongs to certain risk groups
 * @property diseasesRules - rules implying if patient has certain diseases
 */
class InterfaceEngine {
    private val riskGroupsInterview = RiskGroupsInterview()
    private var diseasesInterview: IInterview? = null
    private var currentInterview: IInterview = riskGroupsInterview

    val riskGroupsRules: List<Rule>
        get() = riskGroupsInterview.rules

    val diseasesRules: List<Rule>?
        get() = diseasesInterview?.rules

    /**
     * @return next question to ask within Risk Groups questions group.
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