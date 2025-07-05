package com.chithanh.italk.ai.utils;

public final class JsonCleaningUtils {

    public static String cleanJson(String resultText) {
        if (resultText == null) {
            return null;
        }
        // Xóa tất cả các dấu ```json hoặc ``` xuất hiện bất kỳ vị trí nào
        return resultText
                .replaceAll("(?s)```json\\s*", "") // (?s) cho phép . match \n
                .replaceAll("(?s)```\\s*", "")
                .trim();
    }
}