package com.pedrosaez.pvr_control.ui.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.databinding.FragmentLoginBinding
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
        email.setText(args.userEmail)
        password.setText(args.userPassword)
        session()


        binding.btAccederLogin.setOnClickListener {
            //comprobar que el email y la contraseña son correcta
            //ir a siguiente fragment
            if(email.text.isNullOrEmpty() || password.text.isNullOrEmpty()){
               Toast.makeText(requireContext(),"Los campos no pueden estar vacios",Toast.LENGTH_SHORT).show()
            }else {
                model.signIn(email.text.toString(), password.text.toString()).observe(requireActivity(), {
                    if (it.isSuccessful) {
                        setPrefs(email.text.toString())
                        goHome(this)
                    } else {
                        showAlert(requireContext(),"Error","Error al iniciar sesión")
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

    //funcion para comprobar si hay un usuario logeado  y mantener la sesion
    private fun session(){
        val prefs= requireContext().getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE)
        val email = prefs.getString("email",null)
        if(email != null){
            goHome(this)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}