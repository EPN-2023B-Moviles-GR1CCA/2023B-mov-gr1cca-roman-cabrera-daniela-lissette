import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.exameniib.R
import com.example.exameniib.models.Artista
import com.example.exameniib.ui.theme.CreateArtistaActivity
import com.example.exameniib.ui.theme.EditArtistaActivity
import com.example.exameniib.ui.theme.VerAlbumesActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adaptador: ArrayAdapter<Artista>
    private lateinit var db: FirebaseFirestore
    private lateinit var arregloArtistas: MutableList<Artista>

    private var posicionItemSeleccionado = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()

        listView = findViewById<ListView>(R.id.lv_list_artista)
        arregloArtistas = mutableListOf()
        adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, arregloArtistas)
        listView.adapter = adaptador

        registerForContextMenu(listView)

        obtenerArtistas()

        val btnCrearArtista = findViewById<View>(R.id.btn_ir_crear_artista)
        btnCrearArtista.setOnClickListener {
            irActividad(CreateArtistaActivity::class.java)
        }
    }

    private fun obtenerArtistas() {
        db.collection("artistas")
            .get()
            .addOnSuccessListener { result ->
                arregloArtistas.clear()
                for (document in result) {
                    val artista = document.toObject(Artista::class.java)
                    arregloArtistas.add(artista)
                }
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                mostrarSnackbar("Error al obtener artistas: $exception")
            }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val selectedItem = arregloArtistas[posicionItemSeleccionado]
                val intent = Intent(this, EditArtistaActivity::class.java)
                intent.putExtra("id", selectedItem.id.toString())
                startActivity(intent)
                true
            }
            R.id.mi_eliminar -> {
                abrirDialogo(posicionItemSeleccionado)
                true
            }
            R.id.mi_ver_albumes -> {
                val selectedItem = arregloArtistas[posicionItemSeleccionado]
                val intent = Intent(this, VerAlbumesActivity::class.java)
                intent.putExtra("id", selectedItem.id.toString())
                startActivity(intent)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun mostrarSnackbar(texto: String) {
        Snackbar
            .make(
                findViewById(R.id.id_layout_main),
                texto,
                Snackbar.LENGTH_LONG
            )
            .show()
    }

    private fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    private fun eliminarArtista(id: Int) {
        val idArtistaAEliminar = arregloArtistas[id].id
        arregloArtistas.removeAt(id)
        adaptador.notifyDataSetChanged()

        // Eliminar el documento del artista en Firestore
        db.collection("artistas").document(idArtistaAEliminar.toString())
            .delete()
            .addOnSuccessListener {
                mostrarSnackbar("Artista eliminado correctamente")
            }
            .addOnFailureListener { exception ->
                mostrarSnackbar("Error al eliminar artista: $exception")
            }
    }


    private fun abrirDialogo(id: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton(
            "Aceptar"
        ) { dialog, which ->
            eliminarArtista(id)
            mostrarSnackbar("Artista eliminado")
        }

        builder.setNegativeButton(
            "Cancelar",
            null
        )

        val dialogo = builder.create()
        dialogo.show()
    }
}
