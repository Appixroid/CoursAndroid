package edu.pokemon.iut.tuttidex.data

data class Pokemon(
    var number: Int,
    val name: String?,
    val imageUrl: String?,
    val types: String?,
    val baseExp: Int?,
    val height: Float?,
    val weight: Float?,
    val isCaptured: Boolean
)