package com.pwr.expertsystem.business_logic

class RiskGroupsInterview {
    val sex = Question.RadioQuestion(
        "Jaka jest płeć pacjenta",
        arrayOf("mężczyzna", "kobieta")
    )
    val age =
        Question.NumericalQuestion("Ile pacjent ma lat?")
    val smoking =
        Question.BooleanQuestion("Czy pacjent pali?")
    val drinking =
        Question.BooleanQuestion("Czy pacjent pije?")
    val diseases = Question.AutoFillQuestion(
        "Jakie choroby zdiagnozowano u pacjenta?",
        arrayOf("Cukrzyca typu 1.", "Zespół Downa", "Zespół Turnera")
    )
    val medicines =
        Question.AutoFillQuestion(
            "Jakie leki przyjmuje pacjent?",
            arrayOf("NSLPZ", "Leki przeciwzakrzepowe")
        )
}