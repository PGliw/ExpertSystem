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
    val riskGroups = mutableSetOf<String>()
}

sealed class AnswerStatus<T>
class NotAsked<T> : AnswerStatus<T>()
class Skipped<T> : AnswerStatus<T>()
data class Answered<T>(val value: T) : AnswerStatus<T>()


sealed class ConditionStatus
object NotChecked : ConditionStatus()
object SkippedCondition : ConditionStatus()
data class Checked(val result: Boolean) : ConditionStatus()


sealed class Question<T>(val content: String, var answerStatus: AnswerStatus<T> = NotAsked()) {

    class NumericalQuestion(
        content: String,
        answerStatus: AnswerStatus<Int> = NotAsked()
    ) : Question<Int>(content, answerStatus)

    class BooleanQuestion(
        content: String,
        status: AnswerStatus<Boolean> = NotAsked()
    ) : Question<Boolean>(content, status)

    class RadioQuestion(
        content: String,
        val options: Array<String>,
        answerStatus: AnswerStatus<String> = NotAsked()
    ) : Question<String>(content, answerStatus)

    class AutoFillQuestion(
        content: String,
        val hints: Array<String>,
        answerStatus: AnswerStatus<List<String>> = NotAsked()
    ) : Question<List<String>>(content, answerStatus)
}

class Condition<T>(
    val description: String,
    val question: Question<T>,
    val evaluation: (T) -> Boolean
) {
    val conditionStatus: ConditionStatus
    get() = when (val answer = question.answerStatus) {
        is NotAsked -> NotChecked
        is Skipped -> SkippedCondition
        is Answered -> Checked(
            result = evaluation(answer.value)
        )
    }
}


class Rule(
    val conditions: Set<Condition<out Any>>,
    val conclusion: () -> Unit
)

fun main() {

    val interview = RiskGroupsInterview()

    val riskGroupsRules = listOf(
        Rule(
            conditions = setOf(
                Condition(
                    "Pacjent ma co najmniej 40 lat",
                    interview.age
                ) { it >= 40 }
            ),
            //conclusion = { interview.riskGroups.add("Choroba refluksowa przełyku") }
            conclusion = { println("CONCLUSION: Choroba refluksowa przełyku") }
        ),
        Rule(
            conditions = setOf(
                Condition(
                    "Pacjent jest mężczyzną",
                    interview.sex
                ) { it == "mężczyzna" },
                Condition(
                    "Pacjent ma co najmniej 40 lat",
                    interview.age
                ) { it >= 40 },
                Condition(
                    "Pacjent pali wyroby tytoniowe",
                    interview.smoking
                ) { it },
                Condition(
                    "Pacjent pije alkohol",
                    interview.drinking
                ) { it }
            ),
            //conclusion = { interview.riskGroups.add("Rak przełyku") }
            conclusion = { println("CONCLUSION: Rak przełyku") }
        ),
        Rule(
            conditions = setOf(
                Condition(
                    "Pacjent ma co najmniej 60 lat",
                    interview.age
                ) { it >= 60 },
                Condition(
                    "Pacjent bierze NSLPZ lub leki przeciwkrzepliwe",
                    interview.medicines
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
                    interview.diseases
                ) {
                    it.any { disease ->
                        disease in arrayOf("Cukrzyca typu 1.", "Zespół Downa", "Zespół Turnera")
                    }
                }
            ),
            // conclusion = { interview.riskGroups.add("Celiaklia") }
            conclusion = { println("Celiaklia") }
        ))

    for(rule in riskGroupsRules){
        rule.process()
    }

}


fun Rule.process() {
    for (condition in conditions) {
        when(condition.conditionStatus){
            NotChecked -> condition.question.ask()
        }
    }
    if(conditions.all { it.conditionStatus == Checked(
            true
        )
        }){
        conclusion.invoke()
    }
}

fun Question<out Any>.ask(){
    val prompt = when(this){
        is Question.BooleanQuestion -> "boolean> "
        is Question.RadioQuestion -> "radio> "
        is Question.AutoFillQuestion -> "autofill> "
        is Question.NumericalQuestion -> "numerical> "
    }
    println(content)
    print(prompt)
    val result = readLine() ?: throw NullPointerException("readLine() == null")
    when(this){
        is Question.BooleanQuestion -> answerStatus =
            if(result == "SKIP") Skipped() else Answered(result.toBoolean())
        is Question.RadioQuestion -> answerStatus =
            if(result == "SKIP") Skipped() else Answered(result)
        is Question.AutoFillQuestion -> answerStatus =
            if(result == "SKIP") Skipped() else Answered(listOf(result))
        is Question.NumericalQuestion -> answerStatus =
            if(result == "SKIP") Skipped() else Answered(result.toInt())
    }
}