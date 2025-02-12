package com.example.paisciudadapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class CiudadActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private var paisId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ciudad)

        databaseHelper = DatabaseHelper(this)

        paisId = intent.getIntExtra("PAIS_ID", -1)
        val listView = findViewById<ListView>(R.id.ciudadListView)
        val agregarCiudadButton = findViewById<Button>(R.id.agregarCiudadButton)

        actualizarListaCiudades()

        agregarCiudadButton.setOnClickListener {
            val intent = Intent(this, AddCiudadActivity::class.java)
            intent.putExtra("PAIS_ID", paisId)
            startActivityForResult(intent, 1)
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val ciudad = databaseHelper.getCiudadesByPais(paisId)[position]
            val intent = Intent(this, AddCiudadActivity::class.java)
            intent.putExtra("CIUDAD_ID", ciudad.id)
            intent.putExtra("PAIS_ID", paisId)
            startActivityForResult(intent, 1)
        }
    }

    private fun actualizarListaCiudades() {
        val ciudades = databaseHelper.getCiudadesByPais(paisId)
        val listView = findViewById<ListView>(R.id.ciudadListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ciudades.map { it.nombre })
        listView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            actualizarListaCiudades()
        }
    }
}
