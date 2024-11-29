package com.rizki.produkappapi.service

import com.rizki.produkappapi.model.ResponseProduk
import retrofit2.Call
import retrofit2.http.GET

interface ProdukService {
    @GET("products") //end point
    fun getAllProduk() : Call<ResponseProduk>
}