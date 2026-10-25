package com.zx_tole.lineage2_guide.common

actual object Logger {
    actual fun d(message: String) {
        println("[DEBUG] $message")
    }

    actual fun e(message: String, throwable: Throwable?) {
        if (throwable != null) {
            println("[ERROR] $message: ${throwable.message}")
            throwable.printStackTrace()
        } else {
            println("[ERROR] $message")
        }
    }

    actual fun i(message: String) {
        println("[INFO] $message")
    }

    actual fun w(message: String) {
        println("[WARN] $message")
    }
}
