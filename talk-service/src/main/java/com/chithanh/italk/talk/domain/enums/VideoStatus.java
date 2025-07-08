package com.chithanh.italk.talk.domain.enums;

public enum VideoStatus {
    UPLOADED,      // Video has been uploaded but not processed
    TRANSCODING,  // Video is being transcoded
    READY,        // Video is ready for playback
    FAILED        // Video processing failed
}