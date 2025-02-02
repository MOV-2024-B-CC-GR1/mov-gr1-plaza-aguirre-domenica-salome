import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.paisciudadapp.R

class AddCiudadActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ciudad)

        databaseHelper = DatabaseHelper(this)

        val nombreEditText = findViewById<EditText>(R.id.nombreEditText)
        val esCapitalCheckBox = findViewById<CheckBox>(R.id.esCapitalCheckBox)
        val poblacionEditText = findViewById<EditText>(R.id.poblacionEditText)
        val anioFundacionEditText = findViewById<EditText>(R.id.anioFundacionEditText)
        val guardarButton = findViewById<Button>(R.id.guardarButton)

        // Obtener el ID del país desde el Intent
        val paisId = intent.getIntExtra("PAIS_ID", -1)

        guardarButton.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val esCapital = if (esCapitalCheckBox.isChecked) 1 else 0
            val poblacion = poblacionEditText.text.toString().toIntOrNull() ?: 0
            val anioFundacion = anioFundacionEditText.text.toString().toIntOrNull() ?: 0

            if (nombre.isNotEmpty() && paisId != -1) {
                // Insertar la ciudad en la base de datos
                val ciudadId = databaseHelper.insertCiudad(nombre, paisId, esCapital, poblacion, anioFundacion)
                if (ciudadId != -1L) {
                    Toast.makeText(this, "Ciudad agregada correctamente", Toast.LENGTH_SHORT).show()
                    finish() // Cerrar la actividad después de guardar
                } else {
                    Toast.makeText(this, "Error al agregar la ciudad", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}