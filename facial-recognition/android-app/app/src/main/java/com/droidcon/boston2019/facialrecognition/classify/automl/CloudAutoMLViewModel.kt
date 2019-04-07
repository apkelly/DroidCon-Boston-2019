package com.droidcon.boston2019.facialrecognition.classify.automl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.droidcon.boston2019.facialrecognition.classify.common.*
import com.google.auth.oauth2.AccessToken
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayInputStream
import java.nio.charset.Charset

class CloudAutoMLViewModel : AbstractViewModel() {

    companion object {
        private const val REST_CLASSIFIER =
            false // flag to decide if we should use REST (true) or SDK (false) classifier.

        private const val PROJECT = ""
        private const val LOCATION = ""
        private const val MODEL = ""
        private const val SERVICE_ACCOUNT_JSON = ""
    }

    private val mServiceCredentials = ServiceAccountCredentials
        .fromStream(ByteArrayInputStream(SERVICE_ACCOUNT_JSON.toByteArray(Charset.defaultCharset())))
        .createScoped(mutableListOf("https://www.googleapis.com/auth/cloud-platform"))

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        mResult.postValue(ErrorResource(throwable))
    }

    private val mResult = MutableLiveData<Resource<FaceClassification, Throwable>>()

    private var accessToken: AccessToken? = null

    init {
        Thread {
            accessToken = mServiceCredentials.accessToken
            if (accessToken == null) {
                accessToken = mServiceCredentials.refreshAccessToken()
            }
        }.start()
    }

    fun subscribeClassifications(): LiveData<Resource<FaceClassification, Throwable>> {
        return mResult
    }

    fun classify(faceId: Int, imageBytes: ByteArray) {
        if (REST_CLASSIFIER) {
            classifyUsingRetrofit(faceId, imageBytes)

        } else {
            classifyUsingCloudSDK(faceId, imageBytes)

        }
    }

    private fun classifyUsingRetrofit(faceId: Int, imageBytes: ByteArray) {
        mResult.postValue(SuccessResource(FaceClassification(faceId, "classifyUsingRetrofit", 100.0)))
    }

    private fun classifyUsingCloudSDK(faceId: Int, imageBytes: ByteArray) {
        mResult.postValue(SuccessResource(FaceClassification(faceId, "classifyUsingCloudSDK", 100.0)))
    }

    private fun getRESTService(): CloudAutoMLService {
        val gsonFactory = GsonConverterFactory
            .create(GsonBuilder().create())

        val networkClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return Retrofit.Builder()
            .baseUrl("https://automl.googleapis.com/")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(gsonFactory)
            .client(networkClient)
            .build()
            .create(CloudAutoMLService::class.java)
    }
}



