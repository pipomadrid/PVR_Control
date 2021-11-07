package com.pedrosaez.pvr_control.ui.adapter


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.pedrosaez.pvr_control.R
import com.pedrosaez.pvr_control.database.entities.OutGoins
import com.pedrosaez.pvr_control.ui.dialog.AddOutGoingDialog
import com.pedrosaez.pvr_control.ui.dialog.AddPvrDialogFragment
import com.pedrosaez.pvr_control.ui.listeners.OutGoingModificationListener
import com.pedrosaez.pvr_control.ui.listeners.PvrModificationListener
import java.text.SimpleDateFormat
import java.util.*


class OutGoinsAdapter(val context: Context, val outGoinsList: MutableList<OutGoins>,val outGoinListener:OutGoingModificationListener) : RecyclerView.Adapter<OutGoinsAdapter.MyViewHolder>() {


    // clase que se encargará de contener y gestionar las vistas o controles asociados a cada elemento individual de la lista
    //en este caso gestiona las vistas del texto y de la imagen
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val amount: TextView = view.findViewById(R.id.tv_amount)
        val date: TextView = view.findViewById(R.id.tv_date)
        val concept: TextView = view.findViewById(R.id.tv_concept)
        val btEdit: ImageButton = view.findViewById(R.id.bt_edit_out_goins)
        val btDelete: ImageButton = view.findViewById(R.id.bt_delete_out_goins)

    }

    // Llama a este método siempre que necesita crear una ViewHolder nueva.
    // El método crea y, luego, inicializa la ViewHolder y su View asociada,
    // pero no completa el contenido de la vista; aún no se vinculó la ViewHolder con datos específicos.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val adapterLayout = LayoutInflater.from(context).inflate(R.layout.list_outgoins, parent, false)
        return MyViewHolder(adapterLayout)
    }


    // vincula los datos de cada elemento del datasource(listas en este caso) con las vistas correspondientes
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = outGoinsList[position]
        bind(holder, item)

        holder.btDelete.setOnClickListener {

            //Creamos un alert dialog para confirmar la eliminacion
            val builder = AlertDialog.Builder(context)
            builder.setMessage("¿Estas seguro de eliminar a este PVR?")
                    .setPositiveButton("Eliminar",
                            DialogInterface.OnClickListener { dialog, id ->
                                outGoinListener.delete(item)
                            })
                    .setNegativeButton(("cancelar"),
                            DialogInterface.OnClickListener { dialog, id ->
                                dialog.cancel()
                            })
            // Create the AlertDialog object and return it
            builder.create()
            builder.show()
        }

        holder.btEdit.setOnClickListener {
            val updateDialog = AddOutGoingDialog(outGoinListener)
            val manager = (context as AppCompatActivity).supportFragmentManager
            updateDialog.show(manager,"updateDialog")

        }


    }

    override fun getItemCount() = outGoinsList.size

    private fun bind(holder: OutGoinsAdapter.MyViewHolder, outGoins: OutGoins) {

        var formatedDate:String? = null

        if (outGoins.date != null) {
            var calendar = Calendar.getInstance()
            //obtenemos la fecha del gasto
            calendar.time = outGoins.date!!
            //formatear fecha
            val outGoingDate = calendar.time
            val dateFormated = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formatedDate = dateFormated.format(outGoingDate)

        }

        holder.amount.text = outGoins.cost.toString()
        holder.concept.text = outGoins.description
        holder.date.text = formatedDate.toString()

}


    //Operaciones para notificar los cambios al recyclerView

    fun createOutgoing(outGoins: OutGoins) {
        outGoinsList.add(outGoins)
        notifyItemInserted(outGoinsList.size)
    }

    fun deleteOutGoing(outGoins: OutGoins) {
        val pos = outGoinsList.indexOf(outGoins)
        outGoinsList.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun updateOutGoing(outGoins: OutGoins) {
        val pos = outGoinsList.indexOf(outGoins)
        outGoinsList[pos] = outGoins
        notifyItemChanged(pos)
    }

}
