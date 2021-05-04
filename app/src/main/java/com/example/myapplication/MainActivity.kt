package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.myapplication.api.CatJson
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        loadData()
        binding.constlayout.setOnClickListener {
            loadData()
        }
    }

    private fun loadData(){
        binding.tv.visibility = View.INVISIBLE
        binding.tv1.visibility = View.INVISIBLE
        binding.pgb.visibility = View.VISIBLE


        val api = Retrofit.Builder()
            .baseUrl(Companion.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val response = api.getCatFacts().awaitResponse()
            if (response.isSuccessful){
                val data = response.body()
                data?.text?.let { Log.d(TAG, it) }

                withContext(Dispatchers.Main){
                    binding.tv.visibility = View.VISIBLE
                    binding.tv1.visibility = View.VISIBLE
                    binding.pgb.visibility = View.GONE

                    binding.catjson = data
                }
            }
        }
    }

    companion object {
        const val BASE_URL = "https://cat-fact.herokuapp.com/"
    }
}