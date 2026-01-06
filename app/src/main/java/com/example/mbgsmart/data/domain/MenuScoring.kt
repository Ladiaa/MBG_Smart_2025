package com.example.mbgsmart.data.domain

/**
 * Hitung skor menu MBG berdasarkan kelengkapan komponen gizi
 * AI hanya mendeteksi, scoring tetap rule-based
 */
fun analyzeMenuScore(
    karbo: Boolean,
    protein: Boolean,
    sayur: Boolean,
    buah: Boolean,
    minuman: Boolean
): Int {
    var score = 0

    if (karbo) score += 20
    if (protein) score += 25
    if (sayur) score += 20
    if (buah) score += 20
    if (minuman) score += 15

    return score
}
