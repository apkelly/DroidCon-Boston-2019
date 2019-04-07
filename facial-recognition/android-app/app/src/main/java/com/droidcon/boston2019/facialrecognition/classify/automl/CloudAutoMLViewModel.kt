package com.droidcon.boston2019.facialrecognition.classify.automl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.droidcon.boston2019.facialrecognition.classify.common.AbstractViewModel
import com.droidcon.boston2019.facialrecognition.classify.common.ErrorResource
import com.droidcon.boston2019.facialrecognition.classify.common.FaceClassification
import com.droidcon.boston2019.facialrecognition.classify.common.Resource
import kotlinx.coroutines.CoroutineExceptionHandler

class CloudAutoMLViewModel : AbstractViewModel() {

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        mResult.postValue(ErrorResource(throwable))
    }

    private val mResult = MutableLiveData<Resource<FaceClassification, Throwable>>()

    fun subscribeClassifications(): LiveData<Resource<FaceClassification, Throwable>> {
        return mResult
    }

    fun classify(faceId: Int, imageBytes: ByteArray) {
    }

}



