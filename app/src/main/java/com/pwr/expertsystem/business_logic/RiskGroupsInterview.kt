package com.pwr.expertsystem.business_logic

class RiskGroupsInterview : IInterview{
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
        arrayOf("Cukrzyca typu 1.", "Zespół Downa", "Zespół Turnera", "Żadne z powyższych")
    )
    private val medicines =
        Question.MultiChoiceQuestion(
            "Które z poniższych leków przyjmuje pacjent?",
            arrayOf("NSLPZ", "Leki przeciwzakrzepowe", "Żadne z wymienionych")
        )

    override val rules = listOf(
        Rule(
            conditions = setOf(
                Condition(
                    "Pacjent ma co najmniej 40 lat",
                    age
                ) { it >= 40 }
            ),
            conclusion = Conclusion("Grupa ryzyka", "Choroba refluksowa przełyku")
        ),
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
        ))
}