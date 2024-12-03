package com.rizki.produkappapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rizki.produkappapi.adapter.ProdukAdapter
import com.rizki.produkappapi.model.ModelProduk
import com.rizki.produkappapi.model.ResponseProduk
import com.rizki.produkappapi.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefresh : SwipeRefreshLayout
    private lateinit var recycleView : RecyclerView
    private lateinit var call : Call<ResponseProduk>
    private lateinit var productAdapter : ProdukAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        swipeRefresh = findViewById(R.id.refresh_layout)
        recycleView = findViewById(R.id.rv_products)

        productAdapter = ProdukAdapter{modelProduk: ModelProduk ->  productOnClick(modelProduk)}
        recycleView.adapter = productAdapter
        recycleView.layoutManager = LinearLayoutManager(
            applicationContext, LinearLayoutManager.VERTICAL,
            false
        )

        swipeRefresh.setOnRefreshListener {
            getData()
        }
        getData()
    }

    //https://dummyjson.com/products
    //https://dummyjson.com/docs/products
    private fun productOnClick(produk: ModelProduk){
        Toast.makeText(applicationContext, produk.description, Toast.LENGTH_SHORT).show()
    }

    private fun  getData(){
        swipeRefresh.isRefreshing = true
        call = ApiClient.produkService.getAllProduk()
        call.enqueue(object : Callback<ResponseProduk>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseProduk>,
                response: Response<ResponseProduk>
            ) {
               swipeRefresh.isRefreshing = false
                if(response.isSuccessful){
                    productAdapter.submitList(response.body()?.products)
                    productAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ResponseProduk>, t: Throwable) {
               swipeRefresh.isRefreshing = false
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

}