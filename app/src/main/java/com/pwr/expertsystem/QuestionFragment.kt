package com.pwr.expertsystem


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pwr.expertsystem.business_logic.Answered
import com.pwr.expertsystem.business_logic.Question
import com.pwr.expertsystem.business_logic.Rule
import com.pwr.expertsystem.business_logic.Skipped
import com.pwr.expertsystem.utils.selectedPosition
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.question_input_autocomplete.*
import kotlinx.android.synthetic.main.question_input_multichoice.*
import kotlinx.android.synthetic.main.question_input_numerical.*
import kotlinx.android.synthetic.main.question_input_radio_custom.*
import kotlinx.android.synthetic.main.question_input_radio_yes_no_skip.*

/**
 * A simple [Fragment] subclass.
 */
class QuestionFragment : Fragment() {

    companion object{
        const val TAG = "QuestionFragment"
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val (rule, question) = mainViewModel.interfaceEngine.getNextRuleAndQuestion()
        if (question != null && rule != null) {
            text_question_fragment_question_content.text = question.content
            help_button.setOnClickListener {
                rule.renderHelp()
            }
            when (question) {
                is Question.NumericalQuestion -> renderQuestion(question, rule)
                is Question.BooleanQuestion -> renderQuestion(question, rule)
                is Question.RadioQuestion -> renderQuestion(question, rule)
                is Question.AutoFillQuestion -> renderQuestion(question, rule)
                is Question.MultiChoiceQuestion -> renderQuestion(question, rule)
            }
        } else findNavController().navigate(R.id.action_questionFragment_to_resultsFragment)
    }

    private fun renderQuestion(question: Question.NumericalQuestion, rule: Rule) {
        stub_fragment_question.layoutResource = R.layout.question_input_numerical
        stub_fragment_question.inflate()
        button_question_fragment_next.setOnClickListener {
            val age = edit_text_question_input_numerical.text.toString().toIntOrNull()
            question.answerStatus = if (age != null) Answered(age) else Skipped()
            rule.evalRuleAndNavigate()
        }
    }

    private fun renderQuestion(question: Question.BooleanQuestion, rule: Rule) {
        stub_fragment_question.layoutResource = R.layout.question_input_radio_yes_no_skip
        stub_fragment_question.inflate()
        button_question_fragment_next.setOnClickListener {
            val answer = when (radio_group_question_radio_input_yes_no_skip.selectedPosition) {
                0 -> Answered(true)
                1 -> Answered(false)
                else -> Skipped<Boolean>()
            }
            question.answerStatus = answer
            rule.evalRuleAndNavigate()
        }
    }

    private fun renderQuestion(question: Question.RadioQuestion, rule: Rule) {
        stub_fragment_question.layoutResource = R.layout.question_input_radio_custom
        stub_fragment_question.inflate()
        // Inflate radio buttons
        radio_group_question_radio_input_custom.apply {
            for (option in question.options) {
                val optionRadioButton = RadioButton(requireContext()).apply {
                    text = option
                }
                addView(optionRadioButton)
            }
            val optionSkip = RadioButton(requireContext()).apply {
                text = getString(R.string.skip)
            }
            addView(optionSkip)
        }

        button_question_fragment_next.setOnClickListener {
            val answer =
                when (val selectedPos = radio_group_question_radio_input_custom.selectedPosition) {
                    in question.options.indices -> Answered(question.options[selectedPos])
                    else -> Skipped<String>()
                }
            question.answerStatus = answer
            rule.evalRuleAndNavigate()
        }
    }

    private fun renderQuestion(question: Question.AutoFillQuestion, rule: Rule) {
        stub_fragment_question.layoutResource = R.layout.question_input_autocomplete
        stub_fragment_question.inflate()

        val adapter = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_list_item_1, question.hints
        )
        edit_text_question_input_autocomplete.setAdapter(adapter)

        button_question_fragment_next.setOnClickListener {
            val text = edit_text_question_input_autocomplete.text.toString()
            // TODO enable multiple choice
            question.answerStatus = if (text.isNotBlank()) Answered(listOf(text)) else Skipped()
            rule.evalRuleAndNavigate()
        }
    }

    private fun renderQuestion(question: Question.MultiChoiceQuestion, rule: Rule) {
        stub_fragment_question.layoutResource = R.layout.question_input_multichoice
        stub_fragment_question.inflate()

        linear_layout_question_input_multichoice.apply {
            for (option in question.options) {
                val optionCheckBox = CheckBox(requireContext()).apply {
                    text = option
                }
                addView(optionCheckBox)
            }
        }

        button_question_fragment_next.setOnClickListener {
            val checkedCheckBoxes = linear_layout_question_input_multichoice.getCheckedCheckboxes()
            val checkedOptions = checkedCheckBoxes.map { it.text.toString() }
            question.answerStatus = if(checkedOptions.isEmpty()) Skipped() else Answered(checkedOptions)
            rule.evalRuleAndNavigate()
        }
    }

    private fun Rule.renderHelp() {
        val message =
            "Sprawdzana jest reguła o wniosku:\n ${conclusion.label} = ${conclusion.value}"
        AlertDialog.Builder(requireContext())
            .setTitle("Dlaczego to pytanie")
            .setMessage(message)
            .setPositiveButton("Zamknij") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun Rule.evalRuleAndNavigate() =
        when (eval()) {
            true -> renderNewFactPopUp("Znaleziono nowy fakt:\n ${conclusion.label} = ${conclusion.value}")
            false -> navigateToNextQuestion()
        }

    private fun navigateToNextQuestion() =
        findNavController().navigate(R.id.action_questionFragment_self)

    private fun renderNewFactPopUp(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("Zobacz wyniki") { dialog, _ ->
                dialog.dismiss()
                findNavController().navigate(R.id.action_questionFragment_to_resultsFragment)
            }
            .setNegativeButton("Kontynuuj diagnostykę") { dialog, _ ->
                dialog.dismiss()
                navigateToNextQuestion()
            }
            .show()
    }

    private fun LinearLayout.getCheckedCheckboxes(): List<CheckBox> {
        val result = mutableListOf<CheckBox>()
        for (child in children) {
            if (child is CheckBox && child.isChecked) result.add(child)
        }
        return result
    }
}
