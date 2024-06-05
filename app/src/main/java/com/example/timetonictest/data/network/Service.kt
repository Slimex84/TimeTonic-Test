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

// Singleton object to create and configure the Retrofit service instance.
object Service {
    private const val BASE_URL = "https://timetonic.com/"

    // Creates an instance of ApiService with custom configurations.
    fun create(): ApiService {
        // Create and configure the logging interceptor to log request and response bodies.
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        // Create and configure the OkHttpClient with logging and custom response interceptors.
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(ResponseInterceptor())
            .build()

        // Configure Gson with lenient parsing settings.
        val gson = GsonBuilder()
            .setLenient()
            .create()

        // Build and configure the Retrofit instance with the base URL, OkHttpClient, and custom Gson converter factory.
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(CustomGsonConverterFactory.create(gson))
            .build()

        // Create and return the ApiService instance.
        return retrofit.create(ApiService::class.java)
    }
}

// Interceptor to log the raw response body and proceed with the same response for further processing.
class ResponseInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        // Proceed with the original request and obtain the response.
        val response = chain.proceed(chain.request())
        val responseBody = response.body?.string() ?: ""
        // Log the raw response body
        Log.d("ResponseInterceptor", "Raw response: $responseBody")
        // Create a new response with the same response body for further processing.
        return response.newBuilder()
            .body(ResponseBody.create(response.body?.contentType(), responseBody))
            .build()
    }
}

// Custom Gson converter factory to handle response body conversion using a specified Gson instance.
class CustomGsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {
    companion object {
        // Creates an instance of CustomGsonConverterFactory with the provided Gson instance.
        fun create(gson: Gson): CustomGsonConverterFactory {
            return CustomGsonConverterFactory(gson)
        }
    }

    // Returns a converter for converting the response body to the specified type using Gson.
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return CustomGsonResponseBodyConverter(gson, adapter)
    }
}

// Custom Gson response body converter to handle the conversion of the response body to the specified type.
class CustomGsonResponseBodyConverter<T>(
    private val gson: Gson,
    private val adapter: com.google.gson.TypeAdapter<T>
) : Converter<ResponseBody, T?> {

    // Converts the response body to the specified type using Gson.
    override fun convert(value: ResponseBody): T? {
        val responseBodyString = value.string()
        return try {
            // Attempt to parse as JSON
            adapter.fromJson(responseBodyString)
        } catch (e: Exception) {
            // If parsing fails, return null or handle as needed.
            Log.e("CustomGsonConverter", "Failed to parse JSON: $responseBodyString", e)
            null
        } finally {
            value.close()
        }
    }
}