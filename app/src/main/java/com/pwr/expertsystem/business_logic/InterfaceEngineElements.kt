package com.pwr.expertsystem.business_logic

/**
 * Interface of an Interview - a set of questions and rules
 * @property rules - list of rules that make up the interview
 * @property conclusions - list of conclusions implied by fired rules
 */
interface IInterview {
    val rules: List<Rule>
    val conclusions: List<Conclusion>
        get() = rules.filter {
            it.conditions.all { condition ->
                condition.conditionStatus == Checked(
                    true
                )
            }
        }.map { it.conclusion }

    /**
     * Function returns next question to be asked with its corresponding rule.
     * If there are no question or rule to be left it returns Pair(null, null)
     * @return Pair of rule and the question that corresponds to one of conditions of the rule
     */
    fun nextRuleAndQuestion(): Pair<Rule?, Question<out Any>?> {
        val nextRule =
            rules.find { it.conditions.any { condition -> condition.conditionStatus == NotChecked } }
        val nextCondition =
            nextRule?.conditions?.find { condition -> condition.conditionStatus == NotChecked }
        return Pair(nextRule, nextCondition?.question)
    }
}

/**
 * Class hodling the status of an answer
 */
sealed class AnswerStatus<T>
class NotAsked<T> : AnswerStatus<T>()
class Skipped<T> : AnswerStatus<T>()
data class Answered<T>(val value: T) : AnswerStatus<T>()

/**
 * Class holding the status of condition
 */
sealed class ConditionStatus
object NotChecked : ConditionStatus()
object SkippedCondition : ConditionStatus()
data class Checked(val result: Boolean) : ConditionStatus()

/**
 * Base class for limited number of question types
 * @param content - textual representation of the question
 * @param answerStatus - status of the answer to the question
 */
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

    class MultiChoiceQuestion(
        content: String,
        val options: Array<String>,
        answerStatus: AnswerStatus<List<String>> = NotAsked()
    ) : Question<List<String>>(content, answerStatus)
}

/**
 * Class responsible for holding an condition
 * @param T - type of condition - e.g Int
 * @param description - textual description of the condition, eg. "Number is bigger than 5"
 * @param question - Question that is must be answered to evaluate the condition
 * @param evaluation - Function that evaluates the condition, e.g. (Int) -> {it > 5}
 * @property conditionStatus - status of the condition (weather it was skipped, checked or not)
 */
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

/**
 * Class denoting a rule of type {cond1 and cond2 and ... and cond3} => conclusion
 * @param conditions set of conditions that needs to be evaluated to imply
 * @param conclusion the part of the rule that is implied by conditions
 */
class Rule(
    val conditions: Set<Condition<out Any>>,
    val conclusion: Conclusion
) {
    /**
     * Function tells weather all conditions are checked and are true
     * @return true if all conditions are checked and true, otherwise false
     */
    fun eval() = conditions.all { it.conditionStatus == Checked(true) }
}

/**
 * Key - value class for holding the conclusion of rule
 * Note that it is simple implementation that holds only strings
 * In future it could be combined with a generic type
 */
data class Conclusion(
    val label: String,
    val value: String
)



