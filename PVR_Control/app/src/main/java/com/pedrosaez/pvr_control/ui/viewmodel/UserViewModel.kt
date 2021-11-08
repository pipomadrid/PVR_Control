package com.pedrosaez.pvr_control.ui.viewmodel


import androidx.lifecycle.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.pedrosaez.pvr_control.application.App
import com.pedrosaez.pvr_control.database.entities.PvrMachine
import com.pedrosaez.pvr_control.database.entities.User
import com.pedrosaez.pvr_control.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserViewModel: ViewModel() {


    private val userRepository:UserRepository
    val getAllUser:LiveData<List<User>>

    init {
        val db = App.obtenerDatabase()
        userRepository = UserRepository(db.userDao())
        getAllUser = userRepository.getAllUser().asLiveData()

    }
    fun save (user: User){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.insertUser(user)
            }
        }

    }

    fun delete (user: User){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.deleteUser(user)
            }
        }

    }

    fun update (user: User){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.updateUser(user)
            }
        }

    }


    fun signUp(email: String, password: String): MutableLiveData<Exception> {
        val data = MutableLiveData<Exception>()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
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