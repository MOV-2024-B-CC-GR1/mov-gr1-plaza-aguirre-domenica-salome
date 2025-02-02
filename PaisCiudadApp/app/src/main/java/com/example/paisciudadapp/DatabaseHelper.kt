import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "PaisCiudad.db"
        private const val DATABASE_VERSION = 1

        // Tabla Pais
        const val TABLE_PAIS = "Pais"
        const val COLUMN_PAIS_ID = "id"
        const val COLUMN_PAIS_NOMBRE = "nombre"

        // Tabla Ciudad
        const val TABLE_CIUDAD = "Ciudad"
        const val COLUMN_CIUDAD_ID = "id"
        const val COLUMN_CIUDAD_NOMBRE = "nombre"
        const val COLUMN_CIUDAD_PAIS_ID = "pais_id"
        const val COLUMN_CIUDAD_ES_CAPITAL = "es_capital"
        const val COLUMN_CIUDAD_POBLACION = "poblacion"
        const val COLUMN_CIUDAD_ANIO_FUNDACION = "anio_fundacion"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear la tabla Pais
        val createPaisTable = """
            CREATE TABLE $TABLE_PAIS (
                $COLUMN_PAIS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PAIS_NOMBRE TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createPaisTable)

        // Crear la tabla Ciudad
        val createCiudadTable = """
            CREATE TABLE $TABLE_CIUDAD (
                $COLUMN_CIUDAD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CIUDAD_NOMBRE TEXT NOT NULL,
                $COLUMN_CIUDAD_PAIS_ID INTEGER,
                $COLUMN_CIUDAD_ES_CAPITAL INTEGER DEFAULT 0,
                $COLUMN_CIUDAD_POBLACION INTEGER,
                $COLUMN_CIUDAD_ANIO_FUNDACION INTEGER,
                FOREIGN KEY ($COLUMN_CIUDAD_PAIS_ID) REFERENCES $TABLE_PAIS($COLUMN_PAIS_ID)
            )
        """.trimIndent()
        db?.execSQL(createCiudadTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PAIS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CIUDAD")
        onCreate(db)
    }

    // Método para insertar un país
    fun insertPais(nombre: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_PAIS_NOMBRE, nombre)
        return db.insert(TABLE_PAIS, null, values)
    }

    // Método para obtener todos los países
    fun getAllPaises(): List<String> {
        val paises = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PAIS", null)

        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAIS_NOMBRE))
                paises.add(nombre)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return paises
    }

    // Método para obtener ciudades por país
    fun getCiudadesByPais(paisId: Int): List<String> {
        val ciudades = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CIUDAD WHERE $COLUMN_CIUDAD_PAIS_ID = $paisId", null)

        if (cursor.moveToFirst()) {
            do {
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CIUDAD_NOMBRE))
                val esCapital = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CIUDAD_ES_CAPITAL))
                val poblacion = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CIUDAD_POBLACION))
                val anioFundacion = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CIUDAD_ANIO_FUNDACION))
                val ciudadInfo = "$nombre (Población: $poblacion, Año: $anioFundacion) ${if (esCapital == 1) "[Capital]" else ""}"
                ciudades.add(ciudadInfo)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return ciudades
    }

    // Método para insertar una ciudad
    fun insertCiudad(nombre: String, paisId: Int, esCapital: Int, poblacion: Int, anioFundacion: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CIUDAD_NOMBRE, nombre)
            put(COLUMN_CIUDAD_PAIS_ID, paisId)
            put(COLUMN_CIUDAD_ES_CAPITAL, esCapital)
            put(COLUMN_CIUDAD_POBLACION, poblacion)
            put(COLUMN_CIUDAD_ANIO_FUNDACION, anioFundacion)
        }
        return db.insert(TABLE_CIUDAD, null, values)
    }
}