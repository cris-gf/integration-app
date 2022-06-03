package com.cristiangonzalez.integrationapp.models

data class Donut (
    var id:String,
    var type:String,
    var name:String,
    var ppu:Double,
    var batters:Batters,
    var topping:ArrayList<Topping>
)