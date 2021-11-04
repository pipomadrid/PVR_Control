package com.pedrosaez.pvr_control.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class PairMediatorLiveData<F,S>(first:LiveData<F>, second:LiveData<S>): MediatorLiveData<Pair<F?, S?>>() {


    init {
        addSource(first) { firstLiveDataValue: F -> value = firstLiveDataValue to second.value }
        addSource(second) { secondLiveDataValue: S -> value = first.value to secondLiveDataValue }
    }
}