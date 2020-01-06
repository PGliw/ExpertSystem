package com.pwr.expertsystem.business_logic

/**
 * Interview about risk groups of the patient
 * <p> The risk groups are loosely based on Internet. They were not consulted with any
 * doctor and MUST NOT be used as replacement of medical examination </p>
 */
class RiskGroupsInterview : IInterview {
    private val sex = Question.RadioQuestion(
        "Jaka jest płeć pacjenta",
        arrayOf("mężczyzna", "kobieta")
    )
    private val age =
        Question.NumericalQuestion("Ile pacjent ma lat?")
    private val smoking =
        Question.BooleanQuestion("Czy pacjent pali?")
    private val drinking =
        Question.BooleanQuestion("Czy pacjent pije?")
    private val diseases = Question.MultiChoiceQuestion(
        "Jakie choroby zdiagnozowano u pacjenta?",
        arrayOf(
            "Cukrzyca typu 1.",
            "Cukrzyca typu 2.",
            "Zespół Downa",
            "Zespół Turnera",
            "zakażenie Helicobacter pylori",
            "choroba Addisona-Biermera",
            "choroba Menetriera",
            "otyłość",
            "Żadne z powyższych"
        )
    )
    private val medicines =
        Question.MultiChoiceQuestion(
            "Które z poniższych leków przyjmuje pacjent?",
            arrayOf("NSLPZ", "Leki przeciwzakrzepowe", "Żadne z wymienionych")
        )
    private val bloodGroup =
        Question.RadioQuestion("Jaka jest grupa krwi pacjenta?", arrayOf("0", "A", "B", "AB"))
    private val staticLifestyle =
        Question.BooleanQuestion("Czy pacjent prowadzi siedzący tryb życia")
    private val hygieneRules = Question.BooleanQuestion("Czy pacjent przestrzega zasady higieny?")
    private val kidsContact =
        Question.BooleanQuestion("Czy pacjent przebywa z dziećmi w wieku przedszkolnym?")
    private val rawPork =
        Question.BooleanQuestion("Czy pacjent spożywał surowe lub niedogotowane mięso wieprzowe?")

    override val rules = listOf(

        // 1. Choroba refluksowa przełyku
        Rule(
            conditions = setOf(
                Condition(
                    "Pacjent ma co najmniej 40 lat",
                    age
                ) { it >= 40 }
            ),
            conclusion = Conclusion("Grupa ryzyka", "Choroba refluksowa przełyku")
        ),

        // 2. Rak przełyku
        Rule(
            conditions = setOf(
                Condition(
                    "Pacjent jest mężczyzną",
                    sex
                ) { it == "mężczyzna" },
                Condition(
                    "Pacjent ma co najmniej 40 lat",
                    age
                ) { it >= 40 },
                Condition(
                    "Pacjent pali wyroby tytoniowe",
                    smoking
                ) { it },
                Condition(
                    "Pacjent pije alkohol",
                    drinking
                ) { it }
            ),
            conclusion = Conclusion("Grupa ryzyka", "Rak przełyku")
        ),

        // 3. Choroba wrzodowa żołądka
        Rule(
            conditions = setOf(
                Condition(
                    "Pacjent ma co najmniej 60 lat",
                    age
                ) { it >= 60 },
                Condition(
                    "Pacjent bierze NSLPZ lub leki przeciwkrzepliwe",
                    medicines
                ) {
                    it.any { medicine ->
                        medicine in arrayOf("NSLPZ", "Leki przeciwzakrzepowe")
                    }
                }
            ),
            conclusion = Conclusion("Grupa ryzyka", "Choroba wrzodowa żołądka")
        ),

        // 4. Celiaklia
        Rule(
            conditions = setOf(
                Condition(
                    "U pacjenta zdiagnozowano cukrzycę typu 1 lub zespół Downa lub zespół Turnera",
                    diseases
                ) {
                    it.any { disease ->
                        disease in arrayOf("Cukrzyca typu 1.", "Zespół Downa", "Zespół Turnera")
                    }
                }

            ),
            conclusion = Conclusion("Grupa ryzyka", "Celiaklia")
        ),

        // 5. Rak żołądka
        Rule(
            conditions = setOf(
                Condition(
                    "Pacjent jest mężczyzną",
                    sex
                ) { it == "mężczyzna" },
                Condition(
                    "Pacjent ma co najmniej 50 lat",
                    age
                ) { it >= 50 },
                Condition(
                    "Pacjent pali wyroby tytoniowe",
                    smoking
                ) { it },
                Condition(
                    "Pacjent pije alkohol",
                    drinking
                ) { it },
                Condition(
                    "Pacjent ma grupę krwi A",
                    bloodGroup
                ) { it == "A" },
                Condition(
                    "U pacjenta zdiagnozowano zakażenie Helicobacter pylori, chorobę Addisona-Biermera lub chorobę Menetriera",
                    diseases
                ) {
                    it.any { disease ->
                        disease in arrayOf(
                            "zakażenie Helicobacter pylori",
                            "choroba Addisona-Biermera",
                            "choroba Menetriera"
                        )
                    }
                }
            ),
            conclusion = Conclusion("Grupa ryzyka", "Rak żołądka")
        ),

        // 6. Rak jelita grubego
        Rule(
            conditions = setOf(
                Condition(
                    "Pacjent jest mężczyzną",
                    sex
                ) { it == "mężczyzna" },
                Condition(
                    "Pacjent ma co najmniej 60 lat",
                    age
                ) { it >= 60 },
                Condition(
                    "Pacjent pali wyroby tytoniowe",
                    smoking
                ) { it },
                Condition(
                    "Pacjent pije alkohol",
                    drinking
                ) { it },
                Condition(
                    "Pacjent cierpi na cukrzycę typu 2. lub otyłość",
                    diseases
                ) {
                    it.any { disease ->
                        disease in arrayOf("cukrzyca typu 2.", "otyłość")
                    }
                }
            ),
            conclusion = Conclusion("Grupa ryzyka", "Rak jelita grubego")
        ),

        // 7. Choroba hemoroidalna
        Rule(
            conditions = setOf(
                Condition("Pacjent prowadzi siedzący tryb życia", staticLifestyle) { it },
                Condition("Pacjent cierpi na otyłość", diseases) { it.contains("otyłość") }
            ),
            conclusion = Conclusion("Grupa ryzyka", "Choroba hemoroidalna")
        ),

        // 8. Owsica
        Rule(
            conditions = setOf(
                Condition("Pacjent nie przestrzega zasad higieny", hygieneRules) { !it },
                Condition("Pacjent przebywa z dziećmi w wieku przedszkolnym", kidsContact) { it }
            ),
            conclusion = Conclusion("Grupa ryzyka", "Owsica")
        ),

        // 9. Tasiemczyca
        Rule(
            conditions = setOf(
                Condition("Pacjent nie przestrzega zasad higieny", hygieneRules) { !it },
                Condition("Pacjet spożył surowe lub niedogotowane mięso wieprzowe", rawPork) { it }
            ),
            conclusion = Conclusion("Grupa ryzyka", "Tasiemczyca")
        )
    )
}