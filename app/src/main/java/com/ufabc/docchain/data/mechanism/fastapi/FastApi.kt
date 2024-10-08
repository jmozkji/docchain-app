package com.ufabc.docchain.data.mechanism.fastapi

import com.ufabc.docchain.data.models.FastApiResponse
import com.ufabc.docchain.data.models.FastApiResponsePost
import com.ufabc.docchain.data.models.RawExam
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FastApi {
    @POST("/exams/register-exam")
    suspend fun postExam(@Body exam: RawExam): Response<FastApiResponsePost>

    @GET("/exams/search-exams")
    suspend fun getExams(@Query("docchain_anti_csrf") userId: String): Response<FastApiResponse>
}