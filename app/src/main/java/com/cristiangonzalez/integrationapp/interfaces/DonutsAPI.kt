package com.cristiangonzalez.integrationapp.interfaces

import com.cristiangonzalez.integrationapp.models.Donut
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface DonutsAPI {
    @GET
    suspend fun getDonuts(@Url url:String): Response<ArrayList<Donut>>
}