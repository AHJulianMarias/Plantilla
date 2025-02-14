package com.example.plantilla

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class activity2 : AppCompatActivity(), AdapterView.OnItemSelectedListener   {
    lateinit var nombreElegido: String
    private val institutos =mutableListOf("IES Julian Marias", "Claudio Moyano", "Virgen Espino")
    private val imagenes = mutableListOf(
        R.mipmap.ic_julian_marias,
        R.mipmap.ic_claudiomoyano,
        R.mipmap.ic_virgen_espino,
    )
    private val coordenadas = mutableListOf("40.000,-6",
        "40.000,-5",
        "40.000,-4",
        )
    private lateinit var dbHandler: dbHelper
    lateinit var spinner: Spinner
    private lateinit var coordenadasActuales:String
    private lateinit var coordenadasDestino: String
    lateinit var buttonVerViajes:Button
    lateinit var buttonGuardar:Button
    lateinit var tvDistancia:TextView
    lateinit var destino:String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity2_layout)
        nombreElegido = intent.getStringExtra("nombre").toString()
        var indexEliminar: Int = 0

        institutos.forEachIndexed { index,position ->
            if(position == nombreElegido){
                indexEliminar = index
            }

        }
        dbHandler = dbHelper(this)
        tvDistancia = findViewById(R.id.textViewDistancia)
        coordenadasActuales = coordenadas[indexEliminar]
        institutos.removeAt(indexEliminar)
        imagenes.removeAt(indexEliminar)
        coordenadas.removeAt(indexEliminar)
        spinner = findViewById(R.id.spinnerArriba)
        val adapterPerso = spinnerAdapter(this,R.layout.spinner_item,institutos,imagenes)
        buttonGuardar = findViewById(R.id.buttonGuardar)
        buttonVerViajes = findViewById(R.id.buttonVerViajes)
        spinner.adapter = adapterPerso
        spinner.onItemSelectedListener = this

        this.supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer2, FragmentMap())
            .addToBackStack(null)
            .commit()


        buttonGuardar.setOnClickListener{
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val currentDate = LocalDateTime.now().format(formatter)
            val viaje = Viaje(
                id = null,
                fecha= currentDate,
                origen = nombreElegido,
                destino = destino
            )
            dbHandler.addViaje(viaje)
            Toast.makeText(this,"Viaje añadido", Toast.LENGTH_LONG).show()

        }
        buttonVerViajes.setOnClickListener {
            val intent = Intent(this, activity3::class.java)
            startActivity(intent)
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        coordenadasDestino = coordenadas[position]
        val tvDestino = view?.findViewById<TextView>(R.id.tvNombreSpinner)
        destino = tvDestino?.text.toString()
        val latitudDestino = coordenadasDestino.split(",")[0].toDouble()
        val longitudDestino = coordenadasDestino.split(",")[1].toDouble()
        var distancia = "0"
        CoroutineScope(Dispatchers.Main).launch {
            distancia = haversine(coordenadasActuales.split(",")[0].toDouble(),coordenadasActuales.split(",")[1].toDouble(),latitudDestino,longitudDestino).toString()
            tvDistancia.text = "Distancia: ${distancia.toString()}"
        }
        tvDistancia.text = "Distancia: ${distancia.toString()}"
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
    fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0 // Radio de la Tierra en kilómetros
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c // Distancia en kilómetros
    }

}