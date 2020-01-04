package com.pwr.expertsystem


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.pwr.expertsystem.adapters.ResultsAdapter
import com.pwr.expertsystem.business_logic.Rule
import kotlinx.android.synthetic.main.fragment_results.*

/**
 * A simple [Fragment] subclass.
 */
class ResultsFragment : Fragment() {

    companion object {
        const val TAG = "ResultsFragment"
        const val NUM_SUBFRAGMENTS = 2
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_pager_fragment_results.adapter = ResultsPagerAdapter(this)
        view_pager_fragment_results.registerOnPageChangeCallback(onPageChangeCallback)

        TabLayoutMediator(
            tab_layout_fragment_results,
            view_pager_fragment_results
        ) { tab, position ->
            tab.text = mainViewModel.getInterviewTitle(position)
        }.attach()

        button_results_fragment_close.setOnClickListener {
            findNavController().navigate(R.id.action_resultsFragment_to_startFragment)
        }
    }

    inner class ResultsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = NUM_SUBFRAGMENTS
        override fun createFragment(position: Int): Fragment {
            mainViewModel.setRulesToDisplay(position)
            return ResultsListFragment()
        }
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) = mainViewModel.setRulesToDisplay(position)
    }
}
