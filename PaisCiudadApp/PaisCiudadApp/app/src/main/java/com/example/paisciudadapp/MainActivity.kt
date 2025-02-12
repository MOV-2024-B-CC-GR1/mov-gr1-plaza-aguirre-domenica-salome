package com.example.paisciudadapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        if (databaseHelper.getAllPaises().isEmpty()) {
            databaseHelper.insertPais("Colombia")
            databaseHelper.insertPais("Argentina")
        }

        val paises = databaseHelper.getAllPaises()
        val listView = findViewById<ListView>(R.id.paisListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, paises.map { it.nombre })
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val paisId = paises[position].id
            val intent = Intent(this, CiudadActivity::class.java)
            intent.putExtra("PAIS_ID", paisId)
            startActivity(intent)
        }
    }
}
