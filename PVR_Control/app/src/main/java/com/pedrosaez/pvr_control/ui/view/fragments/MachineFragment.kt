package com.pedrosaez.pvr_control.ui.view.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.PvrMachine
import com.pedrosaez.pvr_control.databinding.FragmentMachineBinding
import com.pedrosaez.pvr_control.ui.dialog.AddMachineDialog
import com.pedrosaez.pvr_control.ui.viewmodel.MachineViewModel


class MachineFragment : Fragment(),AddMachineDialog.MachineAddListener {




    private var actualMachine: PvrMachine? = null
    private var _binding: FragmentMachineBinding? = null
    private val binding get() = _binding!!

    private val model:MachineViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMachineBinding.inflate(layoutInflater)


        val addMachineDialog = AddMachineDialog(this)

        //obtenemos los datos del Pvr que vamos a usar
        val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)),Context.MODE_PRIVATE)
        val pvrNamePrefs: String? = prefs.getString("pvrName","error ")
        val pvrId=  prefs.getLong("pvrId",-1)


        //Setup
        val addButton = binding.btNewMachine
        val pvrName = binding.tvPvrName
        val deleteButton = binding.btDelete
        val editButton= binding.btEdit

        pvrName.text = pvrNamePrefs.toString()




        // observamos  los cambios del livedata y actualizamos los datos en consecuencia
        model.getMachine.observe(viewLifecycleOwner,{

            val brand = binding.tvBrand
            val model = binding.tvModel
            val serialNumber = binding.tvSerialNumber
            val railsNumber = binding.tvRailsNumbers

            for(i in it){
                 if(i.pvrId==pvrId){
                     if(it != null) {
                         actualMachine = i
                         binding.cardView.isVisible = true
                         brand.text = i.brand
                         model.text = i.model
                         serialNumber.text = i.serialNumber
                         railsNumber.text = i.railsNumber.toString()
                     }else{
                         binding.cardView.isVisible = false
                     }
                 }
            }

        })

        // al pulsar boton de eliminar  creamos un dialogo para confirmar
        deleteButton.setOnClickListener {view
            val builder = AlertDialog.Builder(context)
            builder.setMessage("¿Estas seguro de eliminar los datos de la maquina?")
                    //al confirmar , se elimina el registro de la maquina
                    .setPositiveButton("Eliminar",
                            DialogInterface.OnClickListener { dialog, id ->
                                actualMachine?.let { it1 -> deleteMachine(it1) }
                            })
                    .setNegativeButton(("cancelar"),
                            DialogInterface.OnClickListener { dialog, id ->
                                dialog.cancel()
                            })
            // Create the AlertDialog object and return it
            builder.create()
            builder.show()
        }

        // Al pulsar sobre añadir  se crea un dialogo custom para añadir los datos de la maquina
        addButton.setOnClickListener{
            addMachineDialog.show(childFragmentManager,"addMachine")
        }

        // Al pulsar sobre añadir  se crea un dialogo custom para actualizar los datos de la maquina
        editButton.setOnClickListener {
            addMachineDialog.show(childFragmentManager,"updateMachine")
        }


        return binding.root


   }

    private fun deleteMachine(machine: PvrMachine) {
        model.delete(machine)
        binding.cardView.isVisible = false
        binding.btNewMachine.isVisible = true
    }


    // al pulsar boton de guardar  con exito en  el dialogo se muestra el cardview y
    // se oculta el boton de añadir
    override fun onPositiveClick() {
        binding.cardView.isVisible = true
        binding.btNewMachine.isVisible =false

    }


    override fun saveMachine(machine: PvrMachine) {
        model.save(machine)

        val brand = binding.tvBrand
        val model = binding.tvModel
        val serialNumber = binding.tvSerialNumber
        val railsNumber = binding.tvRailsNumbers

        brand.text = machine.brand
        model.text = machine.model
        serialNumber.text = machine.serialNumber
        railsNumber.text = machine.railsNumber.toString()

    }

    override fun updateMachine(machine: PvrMachine) {

        var updateSomeField = false

        //comprobamos los campos que se van a actualizar
        if (machine.brand.isNotEmpty()) {
            actualMachine!!.brand = machine.brand
            updateSomeField = true
        }
        if (machine.model.isNotEmpty()) {
            actualMachine!!.model = machine.model
            updateSomeField = true
        }
        if (machine.serialNumber.isNotEmpty()) {
            actualMachine!!.serialNumber = machine.serialNumber
            updateSomeField = true
        }
        if (machine.railsNumber !=0) {
            actualMachine!!.railsNumber = machine.railsNumber
            updateSomeField = true
        }

            //Si todos los campos están vacios se muestra el snackbar, si hay datos se actualiza el PVR con los mismos
            if (updateSomeField) {
                model.update(actualMachine!!)

            } else {
                Snackbar.make(requireView(), getString(R.string.no_data_found_to_update), Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.bt_new_pvr)//mostramos en snackbar encima del floating button
                        .show()
            }
        }

}