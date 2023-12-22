package com.example.gr1accdlrc2023b


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.google.android.material.snackbar.Snackbar

class ACicloVida : AppCompatActivity() {

    var textoGLobal = ""

    fun mostrarSnackbar(texto: String){
        textoGLobal +=  "texto"
        Snackbar
            .make(
                findViewById(R.id.cl_ciclo_vida), //view
                textoGLobal, //texto
                Snackbar.LENGTH_INDEFINITE //tiempo
            )
            .show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aciclo_vida)
        mostrarSnackbar("OnCreate")
    }

    override fun onStart() {
        super.onStart()
        mostrarSnackbar("OnStart")
    }

    override fun onResume() {
        super.onResume()
        mostrarSnackbar("OnResume")
    }

    override fun onRestart() {
        super.onRestart()
        mostrarSnackbar("OnRestart")
    }

    override fun onPause() {
        super.onPause()
        mostrarSnackbar("OnPause")
    }

    override fun onStop() {
        super.onStop()
        mostrarSnackbar("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        mostrarSnackbar("onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run{
            //GUARDAR LAS VARIABLES PRIMITIVAS
            putString("textoGUardado", textoGLobal)
            //putIn("numeroGuardado", numero)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState( savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //RECUPERAR LAS VARIABLES
        //PRIMITIVAS
        val textoRecuperado:String? = savedInstanceState
            .getString("textoGuardado")
        //val textoRecuperado:Int = savedInstanceState.getIn("xx")
        if (textoRecuperado != null){
            mostrarSnackbar(textoRecuperado)
            textoGLobal = textoRecuperado
        }
    }

}