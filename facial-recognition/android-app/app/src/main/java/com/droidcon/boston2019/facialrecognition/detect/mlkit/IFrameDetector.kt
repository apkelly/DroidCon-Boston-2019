package com.droidcon.boston2019.facialrecognition.detect.mlkit

import java.nio.ByteBuffer

interface IFrameDetector {

    fun process(data: ByteBuffer, frameMetadata: FrameMetadata)

    fun stop()

}