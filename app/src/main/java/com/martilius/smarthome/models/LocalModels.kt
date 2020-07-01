package com.martilius.smarthome.models

data class Cupboard(
    val name:String,
    val message:String
)

fun compoundList():MutableList<Cupboard>{
    var list:MutableList<Cupboard> = arrayListOf()
    list.add(Cupboard("Szafka Lewa","alcupboardsalon1"))
    list.add(Cupboard("Szafka prawa","alcupboardsalon2"))
    list.add(Cupboard("Szafka tył","alcupboardsalon3"))
    return list
}

fun simpleList():MutableList<Cupboard>{
    val list:MutableList<Cupboard> = arrayListOf()
    list.add(Cupboard("Szafki","alcupboardsalon"))
    return list
}