package edu.pokemon.iut.tuttidex.common.di

import com.squareup.moshi.Moshi
import edu.pokemon.iut.tuttidex.BuildConfig
import edu.pokemon.iut.tuttidex.data.network.model.KotshiApplicationJsonAdapterFactory
import edu.pokemon.iut.tuttidex.data.network.service.PokeApiImageService
import edu.pokemon.iut.tuttidex.data.network.service.PokeApiService
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val remoteDataSourceModule = module {
    single {
        createWebService<PokeApiService>(
            BuildConfig.URL,
            get()
        )
    }
    single {
        createWebService<PokeApiImageService>(
            BuildConfig.URL_IMAGE,
            get()
        )
    }
    single { createConverter() }
}

fun createConverter(): Converter.Factory {
    return MoshiConverterFactory.create(
        Moshi.Builder()
            .add(KotshiApplicationJsonAdapterFactory)
            .build()
    )
}

inline fun <reified T> createWebService(url: String, converter: Converter.Factory): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(converter)
        .build()

    return retrofit.create(T::class.java)
}