package com.ufabc.docchain.data.models

import android.net.Uri

data class Exam(
    val examName: String,
    val patientId: String,
    val doctorId: String,
    val description: String,
    val pdfUri: Uri?
)
