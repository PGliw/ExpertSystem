package com.pwr.expertsystem.business_logic

import androidx.lifecycle.MutableLiveData

class InterfaceEngine {
    private val riskGroupFound = MutableLiveData<String>()
    private val riskGroups = mutableSetOf<String>()
    private val riskGroupsInterview = RiskGroupsInterview()
    private val riskGroupsRules = listOf(
        Rule(
            conditions = setOf(
                Condition(
                    "Pacjent ma co najmniej 40 lat",
                    riskGroupsInterview.age
                ) { it >= 40 }
            ),
            conclusion = Conclusion("Grupa ryzyka", "Choroba refluksowa przełyku")
        ),
        Rule(
            conditions = setOf(
                Condition(
                    "Pacjent jest mężczyzną",
                    riskGroupsInterview.sex
                ) { it == "mężczyzna" },
                Condition(
                    "Pacjent ma co najmniej 40 lat",
                    riskGroupsInterview.age
                ) { it >= 40 },
                Condition(
                    "Pacjent pali wyroby tytoniowe",
                    riskGroupsInterview.smoking
                ) { it },
                Condition(
                    "Pacjent pije alkohol",
                    riskGroupsInterview.drinking
                ) { it }
            ),
            conclusion = Conclusion("Grupa ryzyka", "Rak przełyku")
        ),
        Rule(
            conditions = setOf(
                Condition(
                    "Pacjent ma co najmniej 60 lat",
                    riskGroupsInterview.age
                ) { it >= 60 },
                Condition(
                    "Pacjent bierze NSLPZ lub leki przeciwkrzepliwe",
                    riskGroupsInterview.medicines
                ) {
                    it.any { medicine ->
                        medicine in arrayOf("NSLPZ", "Leki przeciwzakrzepowe")
                    }
                }
            ),
            conclusion = Conclusion("Grupa ryzyka", "Choroba wrzodowa żołądka")
        ),
        Rule(
            conditions = setOf(
                Condition(
                    "U pacjenta zdiagnozowano cukrzycę typu 1 lub zespół Downa lub zespół Turnera",
                    riskGroupsInterview.diseases
                ) {
                    it.any { disease ->
                        disease in arrayOf("Cukrzyca typu 1.", "Zespół Downa", "Zespół Turnera")
                    }
                }
            ),
            conclusion = Conclusion("Grupa ryzyka", "Celiaklia")
        ))

    /**
     * Returns the next question to ask within Risk Groups questions group.
     * If there are no question left it returns null.
     */
    fun getNextRiskGroupQuestion(): Pair<Rule?, Question<out Any>?> {
        val nextRule =
            riskGroupsRules.find { it.conditions.any { condition -> condition.conditionStatus == NotChecked } }
        val nextCondition =
            nextRule?.conditions?.find { condition -> condition.conditionStatus == NotChecked }
        return Pair(nextRule, nextCondition?.question)
    }
}