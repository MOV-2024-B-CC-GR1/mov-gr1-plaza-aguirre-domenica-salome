package com.example.paisciudadapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private lateinit var ciudadNombre: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        latitud = intent.getDoubleExtra("LATITUD", 0.0)
        longitud = intent.getDoubleExtra("LONGITUD", 0.0)
        ciudadNombre = intent.getStringExtra("CIUDAD_NOMBRE") ?: "Ubicación"

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val ciudadUbicacion = LatLng(latitud, longitud)
        googleMap.addMarker(MarkerOptions().position(ciudadUbicacion).title(ciudadNombre))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudadUbicacion, 12f))
    }
}
