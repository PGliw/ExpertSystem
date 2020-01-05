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
        Question.BooleanQuestion("Czy u pacjenta wystąpiła znacząca utrata masy ciała?")
    private val bleeding =
        Question.MultiChoiceQuestion(
            "Czy u pacjenta występuje krwawienia? Jeśli tak to jakie?",
            options = arrayOf(
                "krwawienie z górnego odcinka przewodu pokarmowego",
                "krew w kale",
                "krwawienie z odbytnicy",
                "krwawienie z dolnego odcinka przewodu pokarmowego",
                "krew przy wypróżnianiu",
                "odksztuszanie krwi",
                "żadne z powyższych"
            )
        )
    private val swollenGlands =
        Question.BooleanQuestion("Czy węzły chłonne pacjenta są powiększone?")
    private val upperAbdomenPain = Question.BooleanQuestion("Czy pacjent odczuwa ból w nadbrzuszu?")
    private val nausea = Question.BooleanQuestion("Czy pacjentowi dokuczają nudności?")
    private val vomit = Question.BooleanQuestion("Czy pacjent wymiotuje?")
    private val bloatedness = Question.BooleanQuestion("Czy pacjent ma wzdęcia?")
    private val lackOfApeetite = Question.BooleanQuestion("Czy pacjent nie ma apetytu?")
    private val abdomenPain = Question.BooleanQuestion("Czy pacjent odczuwa ból brzucha?")
    private val symptomsIntensity = Question.MultiChoiceQuestion(
        "Kiedy objawy się nasilają?",
        arrayOf(
            "1-3h po posiłku",
            "zaraz po posiłku",
            "w nocy",
            "rano",
            "nie występują w nocy",
            "intensywność objawów się nie zmienia"
        )
    )
    private val diarrhoea = Question.BooleanQuestion("Czy pacjent ma biegunkę?")
    private val obstruction = Question.BooleanQuestion("Czy pacjent ma zaparcia?")
    private val evacuationFrequency = Question.RadioQuestion(
        "Jaka jest częstotliwość (rytm) wypróżnień pacjenta?",
        arrayOf(
            "mniej niż 3 razy w tygodniu",
            "mniej niż 3 razy na dobę, ale więcej niż 3 razy w tygodniu",
            "ponad 3 razy na dobę",
            "do 20 razy na dobę",
            "zmienna intensywność"
        )
    )
    private val suddenBowelMovement =
        Question.BooleanQuestion("Czy pacjent odczuwa nagłe parcie na stolec?")
    private val fever = Question.BooleanQuestion("Czy pacjent ma gorączkę?")
    private val weakness = Question.BooleanQuestion("Czy pacjent jest osłabiony?")

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
                Condition("U pacjenta wystąpiła znacząca utrata masy ciała", weightLoss) { it },
                Condition(
                    "U pacjenta występuje krwawienie z górnego odcinka przewodu pokarmowego",
                    bleeding
                ) { it.contains("krwawienie z górnego odcinka przewodu pokarmowego") }
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
                Condition("U pacjenta wystąpiła znacząca utrata masy ciała", weightLoss) { it },
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
            conclusion = Conclusion("Choroba", "Wczesny rak żołądka")
        ),

        // 6. Zaawansowany rak żołądka
        Rule(
            conditions = setOf(
                Condition("Pacjent odczuwa nudności", nausea) { it },
                Condition("Pacjent odczuwa ból w nadbrzuszu", upperAbdomenPain) { it },
                Condition("Pacjent cierpi na wzdęcia", bloatedness) { it },
                Condition("Pacjent cierpi na dysfagię", dysphagia) { it },
                Condition("Pacjent cierpi na odynofagię", odynophagia) { it },
                Condition("U pacjenta wystąpiła znacząca utrata masy ciała", weightLoss) { it },
                Condition("Pacjent ma powiększone węzły chłonne", swollenGlands) { it },
                Condition("Pacjent wymiotuje", vomit) { it }
            ),
            conclusion = Conclusion("Choroba", "Zaawansowany rak żołądka")
        ),

        // 7. Celiaklia
        Rule(
            conditions = setOf(
                Condition("Pacjent odczuwa ból brzucha", abdomenPain) { it },
                Condition("Pacjent ma biegunkę", diarrhoea) { it },
                Condition("Pacjent wymiotuje", vomit) { it },
                Condition("U pacjenta wystąpiła znacząca utrata masy ciała", weightLoss) { it },
                Condition(
                    "U pacjenta występuje cofanie się treści żołądkowej do przełyku",
                    reflux
                ) { it }
            ),
            conclusion = Conclusion("Choroba", "Celiaklia")
        ),

        // 8. IBS
        Rule(
            conditions = setOf(
                Condition("Pacjent odczuwa nudności", nausea) { it },
                Condition("Pacjent cierpi na wzdęcia", bloatedness) { it },
                Condition(
                    "Objawy pacjenta nasilają zaraz po posiłku ale nie występją w nocy",
                    symptomsIntensity
                ) {
                    it.contains("zaraz po posiłku") && it.contains("nie występują w nocy")
                },
                Condition("Pacjent ma biegunkę", diarrhoea) { it },
                Condition("Pacjent ma zaparcia", obstruction) { it },
                Condition("Pacjent odczuwa ból brzucha", abdomenPain) { it },
                Condition(
                    "Pacjent wypróżnia się mniej niż 3 razy w tygodniu lub częściej niż 3 razy na dobę",
                    evacuationFrequency
                ) {
                    it in arrayOf("mniej niż 3 razy w tygodniu", "ponad 3 razy na dobę")
                },
                Condition("Pacjent odczuwa nagłe parcie na stolec", suddenBowelMovement) { it }
            ),
            conclusion = Conclusion("Choroba", "Zespół jelita drażliwego")
        ),

        // 9. Uchyłki jelita grubego
        Rule(
            conditions = setOf(
                Condition("Pacjent ma gorączkę", fever) { it },
                Condition(
                    "Pacjent ma zmienną intensywność wypróżnień",
                    evacuationFrequency
                ) { it == "zmienna intensywność" },
                Condition("Pacjent ma zaparcia", obstruction) { it },
                Condition("Pacjent odczuwa ból brzucha", abdomenPain) { it },
                Condition("Pacjent cierpi na wzdęcia", bloatedness) { it }
            ),
            conclusion = Conclusion("Choroba", "Uchyłki jelita grubego")
        ),

        // 10. Wrzodziejące zapalenie jelita grubego
        Rule(
            conditions = setOf(
                Condition("Pacjent ma gorączkę", fever) { it },
                Condition("Pacjent jest osłabiony", weakness) { it },
                Condition(
                    "Pacjent wypróżnia się bardzo często (nawet do 20. razy na dobę)",
                    evacuationFrequency
                ) {
                    it == "do 20 razy na dobę"
                },
                Condition("Pacjent odczuwa ból brzucha", abdomenPain) { it },
                Condition("Pacjent ma biegunkę", diarrhoea) { it },
                Condition("W kale pacjenta występuje krew", bleeding){
                    it.contains("krew w kale")
                },
                Condition("U pacjenta wystąpiła znacząca utrata masy ciała", weightLoss) { it }
                ),
            conclusion = Conclusion("Choroba", "Wrzodziejące zapalenie jelita grubego")
        )
    )

}