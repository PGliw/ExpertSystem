package com.pwr.expertsystem

import androidx.lifecycle.ViewModel
import com.pwr.expertsystem.business_logic.InterfaceEngine
import com.pwr.expertsystem.business_logic.Rule
import kotlin.random.Random

class MainViewModel : ViewModel() {
    var interfaceEngine = InterfaceEngine()
    private set

    var ruleToExplain: Rule? = null

    fun clearResults() {
        interfaceEngine = InterfaceEngine()
    }
}