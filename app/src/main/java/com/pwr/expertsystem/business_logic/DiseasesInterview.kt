package com.pwr.expertsystem.business_logic

class DiseasesInterview(val riskGroups: Set<String>) {
    val pyrosis = Question.BooleanQuestion("Czy pacjent ma zgagę?")
    val reflux =
        Question.BooleanQuestion("Czy u pacjenta występuje cofanie się treści żołądkowej do przełyku?")
    val cough = Question.BooleanQuestion("Czy pacjent ma kaszel?")
    val hoarseThroat = Question.BooleanQuestion("Czy pacjent ma chrypkę?")
    val retrosternalPain = Question.BooleanQuestion("Czy pacjent odczuwa ból zamostkowy?")
    val dysphagia = Question.BooleanQuestion("Czy pacjent ma problemy z połykaniem (dysfagia)?")
    val odynophagia =
        Question.BooleanQuestion("Czy pacjent odczuwa ból przy połykaniu (odynofagia)?")
    val weightLoss =
        Question.BooleanQuestion("Czy u pacjenta wystąpiła gwałtowna utrata masy ciała?")
    val gastrointestinalBleeding =
        Question.BooleanQuestion("Czy u pacjenta występuje krwawienie z górnego odcinka przewodu pokarmowego?")
    val swollenGlands = Question.BooleanQuestion("Czy węzły chłonne pacjenta są powiększone?")
    val upperAbdomenPain = Question.BooleanQuestion("Czy pacjent odczuwa ból w nadbrzuszu?")
    val nausea = Question.BooleanQuestion("Czy pacjentowi dokuczają nudności?")
    val vomit = Question.BooleanQuestion("Czy pacjent wymiotuje?")
    val bloatedness = Question.BooleanQuestion("Czy pacjent ma wzdęcia?")
    val lackOfApeetite = Question.BooleanQuestion("Czy pacjent nie ma apetytu?")
    val symptomsIntensity = Question.AutoFillQuestion("Kiedy objawy się nasilają?",
        arrayOf("1-3h po posiłku", "w nocy", "rano", "intensywność objawów się nie zmienia"))
    val diarrhoea = Question.BooleanQuestion("Czy pacjent ma biegunkę?")
}