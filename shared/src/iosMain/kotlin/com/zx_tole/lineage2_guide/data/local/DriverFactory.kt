package com.zx_tole.lineage2_guide.data.local

actual fun createDriver(): Any? {
    // iOS uses API-only — no local database
    return null
}
