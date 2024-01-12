package com.example.gr1accdlrc2023b

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class FRecyclerView : AppCompatActivity() {
    var totalLikes = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frecycler_view)
        inicializarRecyclerView()
    }
    fun inicializarRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.rv_entrenadores)
        val adaptador = FRecyclerViewAdaptadorNombreDescripcion(
            this, // Contexto
            BBaseDatosMemoria.arregloBEntrenador, // Arreglo datos
            recyclerView // Recycler view
        )
        recyclerView.adapter = adaptador
        recyclerView.itemAnimator = androidx.recyclerview.widget
            .DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget
            .LinearLayoutManager(this)
        adaptador.notifyDataSetChanged()
    }
}