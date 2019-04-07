package com.droidcon.boston2019.facialrecognition.classify.common

data class FaceClassification(
    val faceId: Int,
    val name: String,
    val confidence: Double
)