package com.pwr.expertsystem

import androidx.lifecycle.ViewModel
import com.pwr.expertsystem.business_logic.InterfaceEngine
import kotlin.random.Random

class MainViewModel : ViewModel() {
    var interfaceEngine = InterfaceEngine()
    private set

    fun clearResults() {
        interfaceEngine = InterfaceEngine()
    }
}