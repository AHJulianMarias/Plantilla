package com.example.plantilla

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView


class spinnerAdapter (context: Context, resource: Int, private val institutos: MutableList<String>,private val imagenes:MutableList<Int>) :
    ArrayAdapter<String>(context, resource, institutos) {

    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        return crearFilaPersonalizada(position, convertView, parent)
    }
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        return crearFilaPersonalizada(position, convertView, parent)
    }

    private fun crearFilaPersonalizada(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        val layoutInflater = LayoutInflater.from(context)
        val rowView = convertView ?: layoutInflater.inflate(R.layout.spinner_item, parent, false)
        val instituto = institutos[position]
        val imagen = imagenes[position]
        val imagenSpinner = rowView.findViewById<ImageView>(R.id.imageViewSpinner)
        val tvSpinner = rowView.findViewById<TextView>(R.id.tvNombreSpinner)
        tvSpinner.text = instituto
        imagenSpinner.setImageResource(imagen)

        return rowView
    }
}