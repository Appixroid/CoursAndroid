package edu.pokemon.iut.tuttidex.ui.model

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

fun edu.pokemon.iut.tuttidex.data.Pokemon.asUI(): Pokemon {
    return Pokemon(
        number = number,
        name = name,
        imageUrl = imageUrl,
        types = types,
        baseExp = baseExp,
        height = height,
        weight = weight,
        isCaptured = isCaptured
    )
}