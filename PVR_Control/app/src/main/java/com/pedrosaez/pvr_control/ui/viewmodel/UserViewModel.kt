package com.pedrosaez.pvr_control.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class UserViewModel: ViewModel() {


    fun signUp(email: String, password: String): MutableLiveData<Exception> {
        val data = MutableLiveData<Exception>()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful()) {
                        data.value = it.exception
                    } else {
                        data.value = it.exception
                    }
                }
        return data
    }

    fun signIn(email:String, password: String):LiveData<Task<AuthResult>>{

        val data =  MutableLiveData<Task<AuthResult>>()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener {
            data.value = it
        }
        return data

    }

}