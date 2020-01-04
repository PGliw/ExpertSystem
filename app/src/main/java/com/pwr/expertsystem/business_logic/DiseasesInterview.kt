package com.pwr.expertsystem.business_logic

class DiseasesInterview(riskGroups: List<Conclusion>) : IInterview {
    private val riskGroupsQuestion = Question.AutoFillQuestion(
        "Jakie są grupy ryzyka?",
        riskGroups.map { it.value }.toTypedArray(),
        Answered(riskGroups.map { it.value })
    )
    private val pyrosis = Question.BooleanQuestion("Czy pacjent ma zgagę?")
    private val reflux =
        Question.BooleanQuestion("Czy u pacjenta występuje cofanie się treści żołądkowej do przełyku?")
    private val cough = Question.BooleanQuestion("Czy pacjent ma kaszel?")
    private val hoarseThroat = Question.BooleanQuestion("Czy pacjent ma chrypkę?")
    private val retrosternalPain = Question.BooleanQuestion("Czy pacjent odczuwa ból zamostkowy?")
    private val dysphagia =
        Question.BooleanQuestion("Czy pacjent ma problemy z połykaniem (dysfagia)?")
    private val odynophagia =
        Question.BooleanQuestion("Czy pacjent odczuwa ból przy połykaniu (odynofagia)?")
    private val weightLoss =
        Question.BooleanQuestion("Czy u pacjenta wystąpiła gwałtowna utrata masy ciała?")
    private val gastrointestinalBleeding =
        Question.BooleanQuestion("Czy u pacjenta występuje krwawienie z górnego odcinka przewodu pokarmowego?")
    private val swollenGlands =
        Question.BooleanQuestion("Czy węzły chłonne pacjenta są powiększone?")
    private val upperAbdomenPain = Question.BooleanQuestion("Czy pacjent odczuwa ból w nadbrzuszu?")
    private val nausea = Question.BooleanQuestion("Czy pacjentowi dokuczają nudności?")
    private val vomit = Question.BooleanQuestion("Czy pacjent wymiotuje?")
    private val bloatedness = Question.BooleanQuestion("Czy pacjent ma wzdęcia?")
    private val lackOfApeetite = Question.BooleanQuestion("Czy pacjent nie ma apetytu?")
    private val symptomsIntensity = Question.MultiChoiceQuestion(
        "Kiedy objawy się nasilają?",
        arrayOf(
            "1-3h po posiłku",
            "w nocy",
            "rano",
            "nie występują w nocy",
            "intensywność objawów się nie zmienia"
        )
    )
    val diarrhoea = Question.BooleanQuestion("Czy pacjent ma biegunkę?")

    override val rules = listOf(

        // 1. Choroba refluksowa przełyku
        Rule(
            conditions = setOf(
                Condition("Pacjent ma zgagę", pyrosis) { it },
                Condition(
                    "U pacjenta występuje cofanie się treści żołądkowej do przełyku",
                    reflux
                ) { it },
                Condition("Pacjent ma chrypkę", pyrosis) { it },
                Condition("Pacjent ma kaszel", cough) { it },
                Condition(
                    "Pacjent odczuwa ból zamostkowy",
                    retrosternalPain
                ) { it }
            ),
            conclusion = Conclusion("Choroba", "Choroba refluksowa przełyku")
        ),

        // 2. Ostra choroba refluksowa przełyku
        Rule(
            conditions = setOf(
                Condition("Pacjent ma zgagę", pyrosis) { it },
                Condition(
                    "U pacjenta występuje cofanie się treści żołądkowej do przełyku",
                    reflux
                ) { it },
                Condition("Pacjent ma chrypkę", pyrosis) { it },
                Condition("Pacjent ma kaszel", cough) { it },
                Condition(
                    "Pacjent odczuwa ból zamostkowy",
                    retrosternalPain
                ) { it },
                Condition("Pacjent cierpi na dysfagię", dysphagia) { it },
                Condition("Pacjent cierpi na odynofagię", odynophagia) { it },
                Condition("U pacjenta wystąpiła gwałtowna utrata masy ciała", weightLoss) { it },
                Condition(
                    "U pacjenta występuje krwawienie z górnego odcinka przewodu pokarmowego",
                    gastrointestinalBleeding
                ) { it }
            ),
            conclusion = Conclusion("Choroba", "Ostra choroba refluksowa przełyku")
        ),

        // 3. Rak przełyku
        Rule(
            conditions = setOf(
                Condition("Pacjent ma kaszel", cough) { it },
                Condition(
                    "Pacjent odczuwa ból zamostkowy",
                    retrosternalPain
                ) { it },
                Condition("Pacjent cierpi na dysfagię", dysphagia) { it },
                Condition("Pacjent cierpi na odynofagię", odynophagia) { it },
                Condition("U pacjenta wystąpiła gwałtowna utrata masy ciała", weightLoss) { it },
                Condition("Węzły chłonne pacjenta są powiększone", swollenGlands) { it }
            ),
            conclusion = Conclusion("Choroba", "Rak przełyku")
        ),


        // 4. Choroba wrzodowa żołądka
        Rule(
            conditions = setOf(
                Condition(
                    "Objawy pacjenta nasilają się od 1 do 3 godzin po posiłku, w nocy lub rano",
                    symptomsIntensity
                ) {
                    it.any { intensity ->
                        intensity in arrayOf(
                            "1-3h po posiłku",
                            "w nocy",
                            "rano"
                        )
                    }
                },
                Condition("Pacjent wymiotuje", vomit) { it },
                Condition("Pacjent odczuwa nudności", nausea) { it },
                Condition("Pacjent odczuwa ból w nadbrzuszu", upperAbdomenPain) { it }
            ),
            conclusion = Conclusion("Choroba", "Choroba wrzodowa żołądka")
        ),

        // 5. Wczesny rak żołądka
        Rule(
            conditions = setOf(
                Condition("Pacjent odczuwa nudności", nausea) { it },
                Condition("Pacjent odczuwa ból w nadbrzuszu", upperAbdomenPain) { it },
                Condition("Pacjent cierpi na wzdęcia", bloatedness) { it }
            ),
            conclusion = Conclusion("Choroba", "Wczesny ak żołądka")
        ),

        // 5. Zaawansowany rak żołądka
        Rule(
            conditions = setOf(
                Condition("Pacjent odczuwa nudności", nausea) { it },
                Condition("Pacjent odczuwa ból w nadbrzuszu", upperAbdomenPain) { it },
                Condition("Pacjent cierpi na wzdęcia", bloatedness) { it },
                Condition("Pacjent cierpi na dysfagię", dysphagia) { it },
                Condition("Pacjent cierpi na odynofagię", odynophagia) { it },
                Condition("U pacjenta wystąpiła gwałtowna utrata masy ciała", weightLoss) { it },
                Condition("Pacjent ma powiększone węzły chłonne", swollenGlands) { it },
                Condition("Pacjent wymiotuje", vomit) { it }
            ),
            conclusion = Conclusion("Choroba", "Wczesny ak żołądka")
        )

        // 6. Celiaklia

    )

}