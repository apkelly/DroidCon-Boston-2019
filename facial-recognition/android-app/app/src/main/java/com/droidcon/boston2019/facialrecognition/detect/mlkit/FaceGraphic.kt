package com.droidcon.boston2019.facialrecognition.detect.mlkit

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import com.droidcon.boston2019.facialrecognition.detect.common.GraphicOverlay
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark


/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
class FaceGraphic(faceId: Int, graphicOverlay: GraphicOverlay) :
    GraphicOverlay.Graphic(faceId, graphicOverlay) {

    companion object {
        private const val DOT_RADIUS = 10.0f
        private const val TEXT_SIZE = 40.0f
    }

    private var face: FirebaseVisionFace? = null
    private val mFacePositionPaint = Paint()
    private var name: String? = null

    init {
        mFacePositionPaint.color = Color.WHITE
        mFacePositionPaint.textSize = TEXT_SIZE
        mFacePositionPaint.textAlign = Paint.Align.CENTER
    }

    private fun leftEyePosition(): PointF? {
        return PointF(
            face?.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)?.position?.x ?: 0f,
            face?.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)?.position?.y ?: 0f
        )
    }

    private fun rightEyePosition(): PointF? {
        return PointF(
            face?.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)?.position?.x ?: 0f,
            face?.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)?.position?.y ?: 0f
        )
    }

    private fun namePosition(): PointF? {
        return PointF(
            face?.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE)?.position?.x ?: 0f,
            face?.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE)?.position?.y ?: 0f
        )
    }

    fun updateFace(face: FirebaseVisionFace) {
        this.face = face
        postInvalidate()
    }

    fun setName(name: String) {
        this.name = name
        postInvalidate()
    }

    override fun draw(canvas: Canvas) {
        leftEyePosition()?.let { position ->
            canvas.drawCircle(
                translateX(position.x),
                translateY(position.y),
                DOT_RADIUS,
                mFacePositionPaint
            )
        }

        rightEyePosition()?.let { position ->
            canvas.drawCircle(
                translateX(position.x),
                translateY(position.y),
                DOT_RADIUS,
                mFacePositionPaint
            )
        }

        namePosition()?.let { position ->
            if (name != null) {
                canvas.drawText(
                    name!!,
                    translateX(position.x),
                    translateY(position.y),
                    mFacePositionPaint
                )
            }
        }
    }
}

