import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.paisciudadapp.R

class CiudadActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ciudad)

        databaseHelper = DatabaseHelper(this)

        // Obtener el ID del país desde el Intent
        val paisId = intent.getIntExtra("PAIS_ID", -1)

        // Obtener las ciudades del país
        val ciudades = databaseHelper.getCiudadesByPais(paisId)

        // Configurar el ListView
        val ciudadListView = findViewById<ListView>(R.id.ciudadListView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ciudades)
        ciudadListView.adapter = adapter

        // Botón para agregar ciudad
        val agregarCiudadButton = findViewById<Button>(R.id.agregarCiudadButton)
        agregarCiudadButton.setOnClickListener {
            val intent = Intent(this, AddCiudadActivity::class.java)
            intent.putExtra("PAIS_ID", paisId)
            startActivity(intent)
        }
    }
}