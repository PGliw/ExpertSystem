package com.pwr.expertsystem

import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainViewModel : ViewModel(){
    private val rand = Random(1)
    fun next() = rand.nextInt()
}