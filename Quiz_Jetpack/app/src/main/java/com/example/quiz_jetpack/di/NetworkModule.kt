package com.example.quiz_jetpack.di

import android.content.Context
import com.example.quiz_jetpack.data.api.QuizApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://example.com/") // Base URL không quan trọng vì chúng ta sẽ mock response
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideQuizApiService(
        retrofit: Retrofit,
        @ApplicationContext context: Context
    ): QuizApiService {
        return object : QuizApiService {
            override suspend fun getQuizQuestions(): com.example.quiz_jetpack.data.model.QuizResponse {
                return try {
                    val jsonString = context.assets.open("quiz.json").bufferedReader().use { it.readText() }
                    com.google.gson.Gson().fromJson(jsonString, com.example.quiz_jetpack.data.model.QuizResponse::class.java)
                } catch (e: IOException) {
                    throw RuntimeException("Could not read quiz.json from assets", e)
                }
            }
        }
    }
} 