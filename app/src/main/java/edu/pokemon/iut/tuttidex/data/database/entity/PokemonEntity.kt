package edu.pokemon.iut.tuttidex.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_table")
data class PokemonEntity constructor(
    @PrimaryKey
    var id: Int,

    @ColumnInfo(name = "pokemon_name")
    var name: String,

    @ColumnInfo(name = "pokemon_base_exp")
    var baseExperience: Int,

    @ColumnInfo(name = "pokemon_height")
    var height: Int,

    @ColumnInfo(name = "pokemon_weight")
    var weight: Int,

    @ColumnInfo(name = "pokemon_types")
    var types: String,

    @ColumnInfo(name = "pokemon_image_path")
    var image: String,

    @ColumnInfo(name = "pokemon_is_captured")
    var isCaptured: Boolean = false
)

