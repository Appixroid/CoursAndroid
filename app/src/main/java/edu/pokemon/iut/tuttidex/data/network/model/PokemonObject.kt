package edu.pokemon.iut.tuttidex.data.network.model

import edu.pokemon.iut.tuttidex.data.database.entity.PokemonEntity
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class PokemonObject(
    val id: Int,
    val name: String,
    val base_experience: Int,
    val height: Int,
    val weight: Int,
    val types: List<Type>
) {
    fun typesToString(separator: String): String {
        var typesString = ""
        if (types.isNotEmpty()) {
            if (types.size > 1) {
                for (i in types.indices) {
                    typesString = if (i != types.size - 1) {
                        typesString.plus(types[i].type.name.plus(separator))
                    } else {
                        typesString.plus(types[i].type.name)
                    }
                }
            } else {
                typesString = typesString.plus(types[0].type.name)
            }
        }
        return typesString
    }
}

@JsonSerializable
data class SubType(
    val name: String
)

@JsonSerializable
data class Type(
    val slot: Int,
    val type: SubType
)

fun PokemonObject.asEntity(imageUri: String): PokemonEntity {
    return PokemonEntity(
        id = id,
        name = name,
        baseExperience = base_experience,
        height = height,
        weight = weight,
        types = typesToString(","),
        image = imageUri
    )
}
