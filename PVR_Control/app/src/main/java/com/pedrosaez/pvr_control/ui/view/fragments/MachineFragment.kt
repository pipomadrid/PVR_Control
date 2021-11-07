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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.PvrMachine
import com.pedrosaez.pvr_control.databinding.FragmentMachineBinding
import com.pedrosaez.pvr_control.ui.dialog.AddMachineDialog
import com.pedrosaez.pvr_control.ui.viewmodel.MachineViewModel


class MachineFragment : Fragment(),AddMachineDialog.MachineDialogListener {


    private var actualMachine: PvrMachine? = null
    private var _binding: FragmentMachineBinding? = null
    private val binding get() = _binding!!
    private var cardIsvisible:Boolean = false
    private var addBtIsvisible:Boolean = true

    private val model: MachineViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMachineBinding.inflate(layoutInflater)


        val addMachineDialog = AddMachineDialog(this)



        //Setup

        //obtenemos los datos del Pvr que vamos a usar
        val prefs = requireActivity().getSharedPreferences((getString(R.string.prefs_file)), Context.MODE_PRIVATE)
        val pvrNamePrefs: String? = prefs.getString("pvrName", "error ")
        val pvrId = prefs.getLong("pvrId", -1)
        val addButton = binding.btNewMachine
        val pvrName = binding.tvPvrName
        val deleteButton = binding.btDelete
        val editButton = binding.btEdit
        val cardView = binding.cardView

        cardView.isVisible = cardIsvisible
        addButton.isVisible = addBtIsvisible

        pvrName.text = pvrNamePrefs.toString()


        // observamos  los cambios del livedata y actualizamos los datos en consecuencia
        model.getMachine.observe(viewLifecycleOwner, {

            val brand = binding.tvBrand
            val model = binding.tvModel
            val serialNumber = binding.tvSerialNumber
            val railsNumber = binding.tvRailsNumbers

            for (i in it) {
                if (it != null) {
                    if (i.pvrId == pvrId) {
                        actualMachine = i
                        cardIsvisible = true
                        cardView.isVisible = cardIsvisible
                        brand.text = i.brand
                        model.text = i.model
                        serialNumber.text = i.serialNumber
                        railsNumber.text = i.railsNumber.toString()
                    } else {
                        binding.cardView.isVisible = false
                    }
                }
            }
            addButton.isVisible = !cardView.isVisible


        })

        // al pulsar boton de eliminar  creamos un dialogo para confirmar
        deleteButton.setOnClickListener {
            view
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
        addButton.setOnClickListener {
            addMachineDialog.show(childFragmentManager, "addMachine")
        }

        // Al pulsar sobre añadir  se crea un dialogo custom para actualizar los datos de la maquina
        editButton.setOnClickListener {
            addMachineDialog.show(childFragmentManager, "updateMachine")
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
        binding.btNewMachine.isVisible = false
    }
}

