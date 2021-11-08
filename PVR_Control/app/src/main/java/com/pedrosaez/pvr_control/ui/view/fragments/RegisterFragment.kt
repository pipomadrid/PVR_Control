package com.pedrosaez.pvr_control.ui.view.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.ktx.Firebase
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.User
import com.pedrosaez.pvr_control.databinding.FragmentRegisterBinding
import com.pedrosaez.pvr_control.ui.viewmodel.UserViewModel

class RegisterFragment : Fragment() {


    // variables para crear el binding en los fragment
    private lateinit var binding: FragmentRegisterBinding

    //variable para uso de firebase
    private lateinit var auth: FirebaseAuth

    private val model = UserViewModel()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        val email = binding.etEmailRegister
        val password = binding.etPasswordRegister
        val passwordConfirm = binding.etPasswordConfirmRegister
        val botonRegistro = binding.btRegistrate
        val tilEmail = binding.tilEmailRegister
        val tilPassword = binding.tilPasswordRegister
        val tilPasswordConfirm = binding.tilPasswordConfirmRegister


        //borrado  de errores al coger foco
        email.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                tilEmail.error = ""
            }
        }
        password.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                tilPassword.error = ""
            }
        }
        passwordConfirm.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                tilPasswordConfirm.error = ""
            }
        }

        //borrado de errores al hacer click

        email.setOnClickListener {
            tilEmail.error=""
        }

        password.setOnClickListener{
            tilPassword.error = ""
        }

        passwordConfirm.setOnClickListener {
            tilPasswordConfirm.error=""
        }

        password.addTextChangedListener {
            val size = it!!.length
            if(size<8){
                tilPassword.error = getString(R.string.password_weak)
            }else
                tilPassword.error =""

        }

        //Informacion sobre errores al introducir texto en los campos
        passwordConfirm.addTextChangedListener {
            val size = it!!.length
            if(size<8){
                tilPasswordConfirm.error =getString(R.string.password_weak)
            }else
                tilPasswordConfirm.error =""

        }

        email.addTextChangedListener {
            if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
                tilEmail.setError(requireContext().getString(R.string.no_valid_mail))
            }else
                tilEmail.error =""

        }



        botonRegistro.setOnClickListener {

            if (validateEmail(email, tilEmail) && validatePassword(password, tilPassword) && validatePasswordConfirm(password,passwordConfirm, tilPasswordConfirm)) {
                //Observamos el livedata de tipo excepcion del viewmodel y si hay una excepcion actuamos en consecuencia
                  model.signUp(email.text.toString(), password.text.toString()).observe(requireActivity(),{exception->
                      // si no hay fallos navegamos a la pantalla de login
                      if(exception == null){
                          goLogin(this)
                      }else{
                          // si hay excepcion mostramos mensaje correspondiente
                          when(exception){
                              is FirebaseAuthUserCollisionException -> {
                                  tilEmail.error = getString(R.string.used_email)

                              }else->{
                                  showAlert(requireContext())
                              }
                          }
                      }
                  })

            }else {
                validatePassword(password, tilPassword)
                validatePasswordConfirm(password,passwordConfirm, tilPasswordConfirm)

            }
        }

        return binding.root
    }

    //validar email

    private fun  validateEmail(email: EditText, textInputLayout: TextInputLayout):Boolean {

        if(email.text.isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            textInputLayout.setError(requireContext().getString(R.string.no_valid_mail))
            return false
        }else{
            return true
        }
    }

    //validar contraseña
    fun validatePassword(password: EditText, textInputLayout: TextInputLayout):Boolean{

        if(password.text.isNullOrEmpty()){
            textInputLayout.error = getString(R.string.field_empty)
            return false
        }
        if(password.text.toString().length < 8){
            textInputLayout.error = getString(R.string.password_too_short)
            return false
        }

        return true
    }

    //validar  contraseña  de confirmación
    fun validatePasswordConfirm(password:EditText,passwordConfirm: EditText, textInputLayout: TextInputLayout):Boolean{

        if(passwordConfirm.text.isNullOrEmpty()){
            textInputLayout.error = getString(R.string.field_empty)
            return false
        }
        if(password.text.toString()!=passwordConfirm.text.toString()){
            textInputLayout.error = getString(R.string.password_must_match)
        }

        return true
    }

    // Navegar al LoginFragment pasando email y password al login
    private fun goLogin(fragment: Fragment){
        val userEmail = binding.etEmailRegister.text.toString()
        val userPassword = binding.etPasswordRegister.text.toString()
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(userEmail, userPassword)
        NavHostFragment.findNavController(fragment).navigate(action)
    }

    //mostrar alerta cuando firebase no puede registar al usuario
    private fun showAlert(context:Context){

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error al autenticarse")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}