package com.pedrosaez.pvr_control.ui.view.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.User
import com.pedrosaez.pvr_control.databinding.FragmentLoginBinding
import com.pedrosaez.pvr_control.ui.view.HomeActivity
import com.pedrosaez.pvr_control.ui.viewmodel.UserViewModel


class LoginFragment : Fragment() {

    private val args: LoginFragmentArgs by navArgs()

    // variable para crear el binding en los fragment
    private  var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    //variable para uso de firebase
    private lateinit var auth:FirebaseAuth

    val model:UserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater)

        // setup
        val email = binding.etEmailLogin
        val password = binding.etPasswordLogin
        val authLayout = binding.authlayout
        email.setText(args.userEmail)
        password.setText(args.userPassword)
        session(authLayout)


        binding.btAccederLogin.setOnClickListener {
            //comprobar que el email y la contraseña son correcta
            //ir a siguiente fragment
            if(email.text.isNullOrEmpty() || password.text.isNullOrEmpty()){
               Toast.makeText(requireContext(),"Los campos no pueden estar vacios",Toast.LENGTH_SHORT).show()
            } else {
                model.signIn(email.text.toString(), password.text.toString()).observe(requireActivity(), {
                    if (it.isSuccessful) {
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val userEmail = currentUser?.email
                        var userToken: String? = ""
                        var uid = currentUser?.uid

                        model.save(User(userEmail!!, uid!!))

                        setPrefs(email.text.toString())
                        goHome(this)
                    } else {
                        showAlert(requireContext(), "Error", "Error al iniciar sesión")
                    }
                })
            }
        }

        binding.btRegistroLogin.setOnClickListener {

            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_registerFragment)

        }

        return binding.root
    }


    //funcion para navegar al homefragment
    private fun goHome(fragment: Fragment){
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
    }
    //Funcion que muestra un dialogo de alerta
    private fun showAlert(context:Context,tittle:String,message:String,){

        val builder = AlertDialog.Builder(context)
        builder.setTitle(tittle)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    //Funcion para guardar las Sharepreferences de emil y contraseña para mantener el login del usuario
    private fun setPrefs(email:String){
        val prefs= requireContext().getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        prefsEdit.putString("email",email)
        prefsEdit.apply()

    }

    //funcion para comprobar si hay un usuario logeado, ocultar el layout del login  y mantener la sesion
    private fun session(layout:ConstraintLayout){
        val prefs= requireContext().getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE)
        val email = prefs.getString("email",null)
        if(email != null){
            layout.visibility = View.INVISIBLE
            goHome(this)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}

