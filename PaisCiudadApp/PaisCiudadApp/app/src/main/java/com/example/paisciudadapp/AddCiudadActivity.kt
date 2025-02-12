package com.example.paisciudadapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AddCiudadActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var googleMap: GoogleMap
    private var ciudadId: Int = -1
    private var paisId: Int = -1
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ciudad)

        databaseHelper = DatabaseHelper(this)

        val nombreEditText = findViewById<EditText>(R.id.nombreEditText)
        val poblacionEditText = findViewById<EditText>(R.id.poblacionEditText)
        val anioFundacionEditText = findViewById<EditText>(R.id.anioFundacionEditText)
        val txtLatLong = findViewById<TextView>(R.id.txtLatLong)
        val guardarButton = findViewById<Button>(R.id.guardarButton)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        ciudadId = intent.getIntExtra("CIUDAD_ID", -1)
        paisId = intent.getIntExtra("PAIS_ID", -1)

        if (ciudadId != -1) {
            val ciudad = databaseHelper.getCiudadById(ciudadId)
            ciudad?.let {
                nombreEditText.setText(it.nombre)
                poblacionEditText.setText(it.poblacion?.toString() ?: "0")
                anioFundacionEditText.setText(it.anioFundacion?.toString() ?: "0")
                latitud = it.latitud ?: 0.0
                longitud = it.longitud ?: 0.0
                txtLatLong.text = "📍 Latitud: $latitud, Longitud: $longitud"
            }
        }

        guardarButton.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val poblacion = poblacionEditText.text.toString().toIntOrNull() ?: 0
            val anioFundacion = anioFundacionEditText.text.toString().toIntOrNull() ?: 0

            if (nombre.isNotEmpty() && paisId != -1) {
                if (ciudadId == -1) {
                    databaseHelper.insertCiudad(nombre, paisId, 0, poblacion, anioFundacion, latitud, longitud)
                    Toast.makeText(this, "✅ Ciudad agregada correctamente con ubicación", Toast.LENGTH_SHORT).show()
                } else {
                    databaseHelper.updateCiudad(ciudadId, nombre, poblacion, anioFundacion, latitud, longitud)
                    Toast.makeText(this, "✅ Ciudad actualizada con nueva ubicación", Toast.LENGTH_SHORT).show()
                }
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "⚠️ Complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

    }
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Si la ciudad ya existe, se coloca el marcador en la ubicación guardada
        if (ciudadId != -1) {
            val ciudadUbicacion = LatLng(latitud, longitud)
            googleMap.addMarker(MarkerOptions().position(ciudadUbicacion).title("Ubicación guardada"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudadUbicacion, 12f))
        }

        // Permitir seleccionar nueva ubicación
        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear() // Borra los marcadores anteriores
            googleMap.addMarker(MarkerOptions().position(latLng).title("Nueva ubicación seleccionada")) // Agrega nuevo marcador
            latitud = latLng.latitude
            longitud = latLng.longitude
            findViewById<TextView>(R.id.txtLatLong).text = "📍 Latitud: $latitud, Longitud: $longitud"
        }
    }
}