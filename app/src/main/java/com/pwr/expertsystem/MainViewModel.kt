package com.pwr.expertsystem

import androidx.lifecycle.ViewModel
import com.pwr.expertsystem.business_logic.InterfaceEngine
import kotlin.random.Random

class MainViewModel : ViewModel(){
    val interfaceEngine = InterfaceEngine()
}