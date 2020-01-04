package com.pwr.expertsystem.business_logic

class InterfaceEngine {
    private val riskGroupsInterview = RiskGroupsInterview()
    private val diseasesInterview = DiseasesInterview(setOf()) // TODO pass set of risk groups form riskGroupInterview

    fun getNextRuleAndQuestion(): Pair<Rule?, Question<out Any>?>{
        return when(val nextDQ = getNextRiskGroupRuleAndQuestion()){
            Pair(null, null) -> getNextDiseaseRuleAndQuestion()
            else -> getNextRiskGroupRuleAndQuestion()
        }
    }

    /**
     * Returns the next question to ask within Risk Groups questions group.
     * If there are no question left it returns null.
     */
    private fun getNextRiskGroupRuleAndQuestion(): Pair<Rule?, Question<out Any>?> {
        val nextRule =
            riskGroupsRules.find { it.conditions.any { condition -> condition.conditionStatus == NotChecked } }
        val nextCondition =
            nextRule?.conditions?.find { condition -> condition.conditionStatus == NotChecked }
        return Pair(nextRule, nextCondition?.question)
    }

    /**
     * Returns the next question to ask within Diseases Groups questions group.
     * If there are no question left it returns null.
     */
    private fun getNextDiseaseRuleAndQuestion(): Pair<Rule?, Question<out Any>?> {
        val nextRule =
            diseasesRules.find { it.conditions.any { condition -> condition.conditionStatus == NotChecked } }
        val nextCondition =
            nextRule?.conditions?.find { condition -> condition.conditionStatus == NotChecked }
        return Pair(nextRule, nextCondition?.question)
    }

    val riskGroupsRules = listOf(
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

    val diseasesRules = listOf(
        Rule(
            conditions = setOf(
                Condition("Pacjent ma zgagę", diseasesInterview.pyrosis) { it },
                Condition(
                    "U pacjenta występuje cofanie się treści żołądkowej do przełyku",
                    diseasesInterview.reflux
                ) { it },
                Condition("Pacjent ma chrypkę", diseasesInterview.pyrosis) { it },
                Condition("Pacjent ma kaszel", diseasesInterview.cough) { it },
                Condition(
                    "Pacjent odczuwa ból zamostkowy",
                    diseasesInterview.retrosternalPain
                ) { it }
            ),
            conclusion = Conclusion("Choroba", "Choroba refluksowa przełyku")
        ),
        Rule(
            conditions = setOf(
                Condition("Pacjent ma zgagę", diseasesInterview.pyrosis) { it },
                Condition(
                    "U pacjenta występuje cofanie się treści żołądkowej do przełyku",
                    diseasesInterview.reflux
                ) { it },
                Condition("Pacjent ma chrypkę", diseasesInterview.pyrosis) { it },
                Condition("Pacjent ma kaszel", diseasesInterview.cough) { it },
                Condition(
                    "Pacjent odczuwa ból zamostkowy",
                    diseasesInterview.retrosternalPain
                ) { it },
                Condition("Pacjent cierpi na dysfagię", diseasesInterview.dysphagia){ it },
                Condition("Pacjent cierpi na odynofagię", diseasesInterview.odynophagia){ it },
                Condition("U pacjenta wystąpiła gwałtowna utrata masy ciała", diseasesInterview.weightLoss){ it },
                Condition("U pacjenta występuje krwawienie z górnego odcinka przewodu pokarmowego", diseasesInterview.gastrointestinalBleeding){ it }
                ),
            conclusion = Conclusion("Choroba", "Ostra choroba refluksowa przełyku")
        ),
        Rule(
            conditions = setOf(
                Condition("Pacjent ma kaszel", diseasesInterview.cough) { it },
                Condition(
                    "Pacjent odczuwa ból zamostkowy",
                    diseasesInterview.retrosternalPain
                ) { it },
                Condition("Pacjent cierpi na dysfagię", diseasesInterview.dysphagia){ it },
                Condition("Pacjent cierpi na odynofagię", diseasesInterview.odynophagia){ it },
                Condition("U pacjenta wystąpiła gwałtowna utrata masy ciała", diseasesInterview.weightLoss){ it },
                Condition("Węzły chłonne pacjenta są powiększone", diseasesInterview.swollenGlands){ it }
            ),
            conclusion = Conclusion("Choroba", "Rak przełyku")
        )
    )


    /**
     *
     */
    // get rules by hit rate (upper limit)
}