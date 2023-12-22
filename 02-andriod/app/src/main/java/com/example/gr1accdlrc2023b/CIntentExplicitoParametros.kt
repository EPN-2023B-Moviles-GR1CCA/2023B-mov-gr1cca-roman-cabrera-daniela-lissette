package com.example.gr1accdlrc2023b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.snackbar.Snackbar

class CIntentExplicitoParametros : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cintent_explicito_parametros)
        val nombre = intent.getStringArrayExtra("nombre")
        val apellido = intent.getStringArrayExtra("apellido")
        val edad = intent.getIntExtra("edad", 0)
        mostrarSnackbar("${nombre} ${apellido} ${edad}")
        val boton = findViewById<Button>(R.id.btn_devolver_respuesta)
        boton
            .setOnClickListener { devolverRespuesta() }
    }

    fun devolverRespuesta() {
        val intentDevolverParametros = Intent()
        intentDevolverParametros.putExtra("nombreModificado", "Vicente")
        intentDevolverParametros.putExtra("edadModificada", 33)
        setResult(
            RESULT_OK, //resultado ok
            intentDevolverParametros // variables de intent
        ) // ponemos resultado ok y opcional retornamos variables de intent
        finish() //cerramos la actividad
    }

    fun mostrarSnackbar(texto: String) {
        Snackbar
            .make(
                findViewById(R.id.id_layout_intents), // view
                texto, // texto
                Snackbar.LENGTH_LONG // tiempo
            )
            .show()
    }

}