package com.pedrosaez.pvr_control.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.data.entities.DatosPvr
import com.pedrosaez.pvr_control.ui.dialog.AddPvrDialogFragment
import com.pedrosaez.pvr_control.ui.view.UpdateRecyclerView
import java.text.SimpleDateFormat
import java.util.*


class PvrAdapter(val context: Context, val pvr_list: MutableList<DatosPvr>,val updateRecyclerView: UpdateRecyclerView):RecyclerView.Adapter<PvrAdapter.MyViewHolder>(){



    // clase que se encargará de contener y gestionar las vistas o controles asociados a cada elemento individual de la lista
    //en este caso gestiona las vistas del texto y de la imagen
    class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){

        val pvrName: TextView = view.findViewById(R.id.tv_pvr_name)
        val nameSurname: TextView = view.findViewById(R.id.tv_name_surname)
        val address: TextView = view.findViewById(R.id.tv_address)
        val expirationDate: TextView = view.findViewById(R.id.tv_date_expiration)
        val phone : TextView = view.findViewById(R.id.tv_phone)
        val delete_button : ImageButton = view.findViewById(R.id.bt_delete)
        val edit_button : ImageButton = view.findViewById(R.id.bt_edit)


    }

    // Llama a este método siempre que necesita crear una ViewHolder nueva.
    // El método crea y, luego, inicializa la ViewHolder y su View asociada,
    // pero no completa el contenido de la vista; aún no se vinculó la ViewHolder con datos específicos.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val adapterLayout = LayoutInflater.from(context).inflate(R.layout.list_pvr, parent, false)
        return  MyViewHolder(adapterLayout)
    }


    // vincula los datos de cada elemento del datasource(listas en este caso) con las vistas correspondientes
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = pvr_list[position]
        bind(holder, item)
        holder.delete_button.setOnClickListener {

            //Creamos un alert dialog para confirmar la eliminacion
            val builder = AlertDialog.Builder(context)
            builder.setMessage("¿Estas seguro de eliminar a este PVR?")
                    .setPositiveButton("Eliminar",
                            DialogInterface.OnClickListener { dialog, id ->
                                updateRecyclerView.delete(item)
                            })
                    .setNegativeButton(("cancelar"),
                            DialogInterface.OnClickListener { dialog, id ->
                                dialog.cancel()
                            })
            // Create the AlertDialog object and return it
            builder.create()
            builder.show()
        }

        holder.edit_button.setOnClickListener {
            val actualPvr = pvr_list[position]
            updateRecyclerView.sendActualPvr(actualPvr)
            val updateDialog= AddPvrDialogFragment(updateRecyclerView)
            val manager= (context as AppCompatActivity).supportFragmentManager
            updateDialog.show(manager, "updateDialog")

        }
    }


        /*     var calendar = Calendar.getInstance()
             calendar.time=item.authDate
             calendar.add(Calendar.YEAR,3)
             val expirationDate = calendar.time


             val dateFormated= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
             val formatedDate = dateFormated.format(expirationDate)

             holder.pvrName.text = item.pvrName
             holder.nameSurname.text = item.nameSurname
             holder.address.text = item.address
             holder.expirationDate.text = formatedDate
             holder.phone.text = item.phone.toString()*/


    override fun getItemCount()= pvr_list.size



    private fun bind(holder: PvrAdapter.MyViewHolder, pvr: DatosPvr){


        var authExpirationDate:String = " "

        if (pvr.authDate != null) {
            var calendar = Calendar.getInstance()
            //obtenemos la fecha de inicio de la autorizacion
            calendar.time = pvr.authDate!!
            //añadimos 3 años a la fecha para obtener la caducidad y la asignamos a una variable
            calendar.add(Calendar.YEAR, 3)
            val expirationDate = calendar.time
            //damos formato deseado a la fecha
            val dateFormated = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formatedDate = dateFormated.format(expirationDate)
            authExpirationDate=formatedDate
        }

        holder.pvrName.text = pvr.pvrName
        holder.nameSurname.text = pvr.nameSurname
        holder.address.text = pvr.address
        holder.expirationDate.text = authExpirationDate
        holder.phone.text = pvr.phone

    }

    fun createPvr(pvr: DatosPvr) {
        pvr_list.add(pvr)
        notifyItemInserted(pvr_list.size)
    }

     fun deletePvr(pvr: DatosPvr) {
        val pos = pvr_list.indexOf(pvr)
        pvr_list.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun updatePvr(pvr: DatosPvr) {
        val pos = pvr_list.indexOf(pvr)
        pvr_list[pos] = pvr
        notifyItemChanged(pos)
    }

}