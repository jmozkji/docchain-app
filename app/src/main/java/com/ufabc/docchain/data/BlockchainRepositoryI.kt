package com.ufabc.docchain.data

import android.net.Uri
import android.content.Context

interface BlockchainRepositoryI {
    suspend fun postExam(
        context: Context,
        patientId: String,
        doctorId: String,
        examName: String,
        description: String,
        pdfUri: Uri?
    ): Boolean

    suspend fun getExams(context: Context, userId: String): List<Exam>
}