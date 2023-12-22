package com.example.gr1accdlrc2023b

class BBaseDatosMemoria {
    //COMPANION OBJECT

    companion object{
        val arregloBEntrenador = arrayListOf<BEntrenador>()

        init{
            arregloBEntrenador
                .add(
                    BEntrenador(1, "Daniela", "a@a.com")
                )
            arregloBEntrenador
                .add(
                    BEntrenador(2, "Camila", "b@b.com")
                )
            arregloBEntrenador
                .add(
                    BEntrenador(3, "Lorena", "c@c.com")
                )
        }
    }
}