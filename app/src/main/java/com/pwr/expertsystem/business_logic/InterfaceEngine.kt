package com.pwr.expertsystem.business_logic

class Rule(
    val conditions: Set<Condition<out Any>>,
    val conclusion: Fact<out Any>
) {
    init {
        conclusion.provingRules.add(this)
    }

    val conditionsToCheck = conditions.toMutableSet()
    val conditionsMet = mutableListOf<Condition<Any>>()
    val conditionsNotMet = mutableListOf<Condition<Any>>()
    val conditionsSkipped = mutableListOf<Condition<Any>>()
}

data class Condition<T>(
    val id: String,
    val question: Question,
    val value: (T) -> Boolean
)

data class Fact<T>(
    val id: String,
    val value: T
) {
    val provingRules = mutableListOf<Rule>()
}

sealed class Question(
    val content: String
) {
    class NumericalQuestion(
        content: String
    ) : Question(content)

    class BooleanQuestion(
        content: String
    ) : Question(content)

    class RadioQuestion(
        content: String,
        val answers: Array<String>
    ) : Question(content)

    class AutofillQuestion(
        content: String,
        val answers: Array<String>
    ) : Question(content)
}


fun main() {

    val ageQuestion = Question.NumericalQuestion("Podaj wiek pacjenta w latach?")
    val smokingQuestion = Question.BooleanQuestion("Czy pacjent pali?")
    val drinkingQuestion = Question.BooleanQuestion("Czy pacjent jest pijący?")
    val sexQuestion = Question.RadioQuestion("Podaj płeć pacjenta", arrayOf("Mężczyzna", "Kobieta"))
    val medicinesQuestion = Question.AutofillQuestion(
        "Jakie leki zażywa pacjent",
        arrayOf("cukrzyca typu 1", "zespół Downa", "zespół Turnera")
    )

    val facts = mutableSetOf<Fact<out Any>>()

    val riskGroupsRules = listOf(
        Rule(
            conditions = setOf(
                Condition<Int>(id = "wiek", question = ageQuestion) { it >= 40 }
            ),
            // Może tu pytanie "Czy u pacjenta zdiagnozowano już jakieś choroby?
            // i w Question lista pytań do wyboru
            conclusion = Fact<String>("grupa ryzyka", "Choroba refluksowa przełyku")
        ),
        Rule(
            conditions = setOf(
                Condition<String>(id = "płeć", question = sexQuestion) { it == "mężczyzna" },
                Condition<Int>(id = "wiek", question = ageQuestion) { it >= 40 },
                Condition<Boolean>(id = "palący", question = smokingQuestion) { it },
                Condition<Boolean>(id = "pijący", question = drinkingQuestion) { it }
            ),
            conclusion = Fact("grupa ryzyka", "Rak przełyku")
        ),
        Rule(
            conditions = setOf(
                Condition<Int>(id = "wiek", question = ageQuestion) { it >= 60 },
                Condition<String>(id = "leki", question = medicinesQuestion) {
                    it in setOf(
                        "Niesteroidowe leki przeciwzakrzepowe",
                        "Leki przeciwkrzepliwe"
                    )
                }),
            conclusion = Fact("grupa ryzyka", "Choroba wrzodowa żołądka")
        )
    )

    val r = riskGroupsRules[0].conditions.map { it.value }
    for(rule in riskGroupsRules) rule.process(facts)

}

fun Rule.process(factsAccumulator: MutableSet<Fact<out Any>>) {
    for (condition in conditions) {
        // if the matching fact is already in accumulator then don't ask again
        val factsIdsAccumulator = factsAccumulator.map { it.id }

        // if the matching fact is not in accumulator then ask for its value and add to acc
        if (condition.id !in factsIdsAccumulator) {
            println(condition.question)
            val prompt = when (condition.question) {
                is Question.BooleanQuestion -> "[true/false]>"
                is Question.RadioQuestion -> condition.question.answers.toString()
                is Question.AutofillQuestion -> condition.question.answers.toString()
                is Question.NumericalQuestion -> "[int]>"
            }
            print(prompt)
            val res = readLine()
            when (condition.question) {
                is Question.BooleanQuestion -> factsAccumulator.add(
                    Fact(
                        condition.id,
                        res?.toBoolean() ?: throw IllegalArgumentException("res == null")
                    )
                )
                is Question.RadioQuestion -> factsAccumulator.add(
                    Fact(
                        condition.id,
                        res ?: throw IllegalArgumentException("res == null")
                    )
                )
                is Question.AutofillQuestion -> factsAccumulator.add(
                    Fact(
                        condition.id,
                        res ?: throw IllegalArgumentException("res == null")
                    )
                )
                is Question.NumericalQuestion -> factsAccumulator.add(
                    Fact(
                        condition.id,
                        res?.toInt() ?: throw IllegalArgumentException("res == null")
                    )
                )
            }

            // find matching fact
            val matchingFact = factsAccumulator.find { it.id == condition.id }
                ?: throw NullPointerException("No fact found for given id!")

            print(matchingFact.value)

        }
    }
}
