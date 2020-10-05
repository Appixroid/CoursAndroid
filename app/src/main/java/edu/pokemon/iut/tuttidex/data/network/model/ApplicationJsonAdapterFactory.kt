package edu.pokemon.iut.tuttidex.data.network.model

import com.squareup.moshi.JsonAdapter
import se.ansman.kotshi.KotshiJsonAdapterFactory

@KotshiJsonAdapterFactory
abstract class ApplicationJsonAdapterFactory : JsonAdapter.Factory {
  @Suppress("unused")
  val instance: ApplicationJsonAdapterFactory = KotshiApplicationJsonAdapterFactory
}