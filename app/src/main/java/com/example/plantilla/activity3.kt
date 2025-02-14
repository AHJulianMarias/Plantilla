package com.example.plantilla

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class activity3 : AppCompatActivity()  {
    private lateinit var dbHandler: dbHelper
    private lateinit var listViewAct3 : ListView
    private lateinit var btnModif: Button
    private lateinit var btnElim:Button
    private lateinit var btnVolver:Button
    private var id_elegido:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el diseño de la interfaz de usuario definido en activity_main.xml.
        setContentView(R.layout.activity3_layout)


        // Inicializa el controlador de la base de datos.
        dbHandler = dbHelper(this)
        listViewAct3 = findViewById(R.id.listViewAct3)
        btnElim = findViewById(R.id.btnDel)
        btnModif = findViewById(R.id.btnModif)
        btnVolver = findViewById(R.id.btnVolver)
        var listaViajes = dbHandler.getAllViajes()
        val arrayViajes:Array<Viaje> = listaViajes.toArray() as Array<Viaje>
        val adapterPerso = persoAdapter(this,arrayViajes)
        listViewAct3.adapter = adapterPerso
        // Configura los eventos de clic para los botones.
        btnElim.setOnClickListener {
            deleteViaje()
        }
        btnModif.setOnClickListener {

        }
        btnVolver.setOnClickListener {
            val intent = Intent(this, activity2::class.java)
            startActivity(intent)
        }

        // Muestra la lista de Gatos al iniciar la actividad.
    }
        private inner class persoAdapter(
            context: Context,
            private val listaViajes: Array<Viaje>,
        ) : ArrayAdapter<Viaje>(context, 0, listaViajes) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                var view = convertView

                // Inflar el layout si no existe vista reciclada
                if (view == null) {
                    view = LayoutInflater.from(context)
                        .inflate(R.layout.item_list_act3, parent, false)
                }
                val viaje = listaViajes[position]
                val origen = viaje.origen
                val fecha = viaje.fecha
                val destino = viaje.destino
                // Referencias a los views
                val tvFecha = view?.findViewById<TextView>(R.id.tvFecha)
                val tvOrigen = view?.findViewById<TextView>(R.id.tvOrigen)
                val tvDestino = view?.findViewById<TextView>(R.id.tvDestino)
                // Asignar valores
                tvOrigen?.text = origen
                tvFecha?.text = fecha
                tvDestino?.text = destino
                view?.setOnClickListener {
                    id_elegido = viaje.id
                }
                return view!!
            }
        }

    private fun deleteViaje(){
        //Sin corrutinas quitas lo de alrededor y ya
        if(id_elegido!=null){
            val status = dbHandler.deleteViaje(id_elegido!!)
            if (status > -1) {
                Toast.makeText(this, "Viaje eliminado", Toast.LENGTH_LONG).show()

            }
        }else{
            Toast.makeText(this, "Viaje no seleccionado", Toast.LENGTH_LONG).show()
        }


    }
    }
