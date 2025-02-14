package com.example.plantilla

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.w3c.dom.Text

//TODO Guardar sesion de la posicion elegida
class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: activityMainAdapter
    private val institutos = arrayOf("IES Julian Marias", "Claudio Moyano", "Virgen Espino")

    private lateinit var imagengrande: ImageView
    private lateinit var telefonogrande: TextView
    private lateinit var nombreInstitutoElegido: TextView
    private val telefonos = arrayOf(
        "6566666666",
        "6566666666",
        "6566666666"
    )

    private val imagenes = intArrayOf(
        R.mipmap.ic_julian_marias,
        R.mipmap.ic_claudiomoyano,
        R.mipmap.ic_virgen_espino,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imagengrande = findViewById(R.id.imageView)
        telefonogrande = findViewById(R.id.tvTelefono)
        listView = findViewById(R.id.listView)
        nombreInstitutoElegido = findViewById(R.id.textViewNombreElegido)
        adapter = activityMainAdapter(this, institutos,telefonos,imagenes)
        listView.adapter = adapter

        imagengrande.setOnClickListener{
            if(nombreInstitutoElegido.text != "a"){
                val intent = Intent(this, activity2::class.java)
                intent.putExtra("nombre",nombreInstitutoElegido.text)
                startActivity(intent)
            }



        }


        telefonogrande.setOnClickListener{
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permiso concedido, realiza la llamada
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:${telefonogrande.text}")
                startActivity(intent)
            } else {
                // Solicita el permiso si no está concedido
                getPhonePermission()
            }


        }

    }


    private inner class activityMainAdapter(
        context: Context,
        private val institutos: Array<String>,
        private val telefonos: Array<String>,
        private val imagenes: IntArray
    ) : ArrayAdapter<String>(context, 0, institutos) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView

            // Inflar el layout si no existe vista reciclada
            if (view == null) {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.activity_item_list, parent, false)
            }

            // Obtener el objeto restaurant actual
            val instituto = institutos[position]
            val telefono = telefonos[position]
            val imagen = imagenes[position]
            // Referencias a los views
            val tvName = view?.findViewById<TextView>(R.id.tvNombre)
            val tvTelefono = view?.findViewById<TextView>(R.id.tvTelefono)
            val tvImagen = view?.findViewById<ImageView>(R.id.imageViewList)
            // Asignar valores
            tvName?.text = instituto
            tvTelefono?.text = telefono
            tvImagen?.setImageResource(imagen)
            view?.setOnClickListener {
                telefonogrande.text = telefono
                imagengrande.setImageResource(imagen)
                nombreInstitutoElegido.text = instituto

            }
            return view!!
        }
    }

    private fun getPhonePermission() {
        val context = this ?: return  // Prevenir errores si el fragment no tiene contexto

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicitar el permiso si no está concedido
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PERMISSION
            )
        } else {
            // El permiso ya está concedido
            Toast.makeText(context, "Permiso concedido", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, realiza la llamada
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:${telefonogrande.text}")
                startActivity(intent)
            } else {
                // Permiso denegado
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        private const val REQUEST_CALL_PERMISSION = 1
    }
}