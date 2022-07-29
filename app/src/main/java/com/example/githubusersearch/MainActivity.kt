package com.example.githubusersearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultText = findViewById<TextView>(R.id.result)
        val userName = findViewById<EditText>(R.id.userName)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            // 선언을 하면 역직렬화 할 때에 Gson을 사용함 (역직렬화 방법).
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().registerTypeAdapter(
                        GitHubResponseGSON::class.java,
                        GitHubResponseDeserializerGSON()
                    ).create()
                )
            ).build()

        // 호출할 수 있는 준비를 끝냄
        findViewById<Button>(R.id.search_btn).setOnClickListener {
            val id = userName.text.toString()
            val apiService = retrofit.create(GitHubAPIService::class.java)
            val apiCallForData = apiService.getGitHubInfo(id!!)
            apiCallForData.enqueue(object : Callback<GitHubResponseGSON> {
                override fun onResponse(
                    call: Call<GitHubResponseGSON>,
                    response: Response<GitHubResponseGSON>
                ) {
                    val data = response.body()!!
                    Log.d("mytag", data.toString())
                    
                    // "${}"이 자체가 String이기 때문에 간단하게 작성 가능
                    resultText.text = "${data.login}\n${data.id}"
                }

                override fun onFailure(call: Call<GitHubResponseGSON>, t: Throwable) {
                    Toast.makeText(this@MainActivity,
                        "에러 발생 : ${t.message}",
                        Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}