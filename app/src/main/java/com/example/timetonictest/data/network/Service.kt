package com.example.timetonictest.data.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type

object Service {
    private const val BASE_URL = "https://timetonic.com/"

    fun create(): ApiService {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(ResponseInterceptor())
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(CustomGsonConverterFactory.create(gson))
            .build()

        return retrofit.create(ApiService::class.java)
    }
}

class ResponseInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val response = chain.proceed(chain.request())
        val responseBody = response.body?.string() ?: ""
        // Log the raw response body
        Log.d("ResponseInterceptor", "Raw response: $responseBody")
        // Create a new response with the same response body for further processing
        return response.newBuilder()
            .body(ResponseBody.create(response.body?.contentType(), responseBody))
            .build()
    }
}

class CustomGsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {
    companion object {
        fun create(gson: Gson): CustomGsonConverterFactory {
            return CustomGsonConverterFactory(gson)
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CustomGsonResponseBodyConverter(gson, adapter)
    }
}

class CustomGsonResponseBodyConverter<T>(
    private val gson: Gson,
    private val adapter: com.google.gson.TypeAdapter<T>
) : Converter<ResponseBody, T?> {

    override fun convert(value: ResponseBody): T? {
        val responseBodyString = value.string()
        return try {
            // Attempt to parse as JSON
            adapter.fromJson(responseBodyString)
        } catch (e: Exception) {
            // If parsing fails, return null or handle as needed
            Log.e("CustomGsonConverter", "Failed to parse JSON: $responseBodyString", e)
            null
        } finally {
            value.close()
        }
    }
}