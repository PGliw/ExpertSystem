package com.pwr.expertsystem.business_logic

class InterfaceEngine {
    private val riskGroupsInterview = RiskGroupsInterview()
    private val riskGroupsRules = listOf(
        Rule(
            conditions = setOf(
                Condition(
                    "Pacjent ma co najmniej 40 lat",
                    riskGroupsInterview.age
                ) { it >= 40 }
            ),
            //conclusion = { interview.riskGroups.add("Choroba refluksowa przełyku") }
            conclusion = { println("CONCLUSION: Choroba refluksowa przełyku") }
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
            //conclusion = { interview.riskGroups.add("Rak przełyku") }
            conclusion = { println("CONCLUSION: Rak przełyku") }
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
            //conclusion = { interview.riskGroups.add("Choroba wrzodowa żołądka") }
            conclusion = { println("CONCLUSION: Choroba wrzodowa żołądka") }
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
            // conclusion = { interview.riskGroups.add("Celiaklia") }
            conclusion = { println("Celiaklia") }
        ))

    /**
     * Returns the next question to ask within Risk Groups questions group.
     * If there are no question left it returns null.
     */
    fun getNextRiskGroupQuestion(): Question<out Any>?{
        val nextRule = riskGroupsRules.find { it.conditions.any{ condition -> condition.conditionStatus == NotChecked } }
        val nextCondition = nextRule?.conditions?.find { condition -> condition.conditionStatus == NotChecked  }
        return nextCondition?.question
    }
}