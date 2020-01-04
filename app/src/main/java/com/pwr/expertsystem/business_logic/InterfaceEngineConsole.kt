package com.pwr.expertsystem.business_logic

interface IInterview{
    val rules: List<Rule>
    fun nextRuleAndQuestion(): Pair<Rule?, Question<out Any>?>{
        val nextRule = rules.find { it.conditions.any { condition -> condition.conditionStatus == NotChecked } }
        val nextCondition =
            nextRule?.conditions?.find { condition -> condition.conditionStatus == NotChecked }
        return Pair(nextRule, nextCondition?.question)
    }
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
    val conclusion: Conclusion
){
    fun eval() = conditions.all { it.conditionStatus == Checked(true) }
}

data class Conclusion(
    val label: String,
    val value: String
)


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
        print(conclusion)
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

