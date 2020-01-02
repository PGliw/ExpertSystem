package com.pwr.expertsystem


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pwr.expertsystem.utils.selectedPosition
import com.pwr.expertsystem.utils.snack
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.question_input_autocomplete.*
import kotlinx.android.synthetic.main.question_input_numerical.*
import kotlinx.android.synthetic.main.question_input_radio_custom.*
import kotlinx.android.synthetic.main.question_input_radio_yes_no_skip.*

/**
 * A simple [Fragment] subclass.
 */
class QuestionFragment : Fragment() {

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
        val rand = mainViewModel.next()
        when (rand % 4) {
            0 -> renderCustomRadioQuestion()
            1 -> renderNumericalQuestion()
            2 -> renderAutocompleteQuestion()
            else -> renderYesNoSkipQuestion()
        }
    }

    private fun renderNumericalQuestion() {
        stub_fragment_question.layoutResource = R.layout.question_input_numerical
        stub_fragment_question.inflate()
        button_question_fragment_next.setOnClickListener {
            val age = edit_text_question_input_numerical.text.toString()
            snack(age)
            navigateToNextQuestion()
        }
    }

    private fun renderYesNoSkipQuestion() {
        stub_fragment_question.layoutResource = R.layout.question_input_radio_yes_no_skip
        stub_fragment_question.inflate()
        button_question_fragment_next.setOnClickListener {
            val selectedPos = radio_group_question_radio_input_yes_no_skip.selectedPosition
            snack("$selectedPos")
            navigateToNextQuestion()
        }
    }

    private fun renderCustomRadioQuestion() {
        stub_fragment_question.layoutResource = R.layout.question_input_radio_custom
        stub_fragment_question.inflate()
        val option1 = RadioButton(requireContext()).apply {
            text = "Option1"
        }
        val option2 = RadioButton(requireContext()).apply {
            text = "Option2"
        }
        val option3 = RadioButton(requireContext()).apply {
            text = "Option2"
        }
        val option4 = RadioButton(requireContext()).apply {
            text = getString(R.string.skip)
        }
        radio_group_question_radio_input_custom.apply {
            addView(option1)
            addView(option2)
            addView(option3)
            addView(option4)
        }

        button_question_fragment_next.setOnClickListener {
            val selectedPos = radio_group_question_radio_input_custom.selectedPosition
            snack("$selectedPos")
            navigateToNextQuestion()
        }
    }

    private fun renderAutocompleteQuestion() {
        stub_fragment_question.layoutResource = R.layout.question_input_autocomplete
        stub_fragment_question.inflate()

        val adapter = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_list_item_1, arrayOf("aaaa", "aaab", "aaac"))

        edit_text_question_input_autocomplete.setAdapter(adapter)

        button_question_fragment_next.setOnClickListener {
            val text = edit_text_question_input_autocomplete.text.toString()
            snack(text)
            navigateToNextQuestion()
        }
    }

    private fun navigateToNextQuestion() =
        findNavController().navigate(R.id.action_questionFragment_self)

}
