package com.example.githubusersearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GitHubUserRepositoryListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.git_hub_user_repository_list_activity)

        // id 값 받기
        val id = intent.getStringExtra("userid")!!
        Log.d("mytag", id)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(
                GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(GitHubAPIService::class.java)
        val apiCallForData = apiService.getRepos(id, "token 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")
        apiCallForData.enqueue(object: Callback<List<GitHubUserRepo>> {
            override fun onResponse(
                call: Call<List<GitHubUserRepo>>,
                response: Response<List<GitHubUserRepo>>
            ) {
                val data = response.body()!!
                Log.d("mytag", data.toString())
                val listView = findViewById<RecyclerView>(R.id.user_Recycler)
                // setHasFixedSize의 기능은 RecyclerView의 크기 변경이 일정하다는 것을 사용자의 입력으로 확인한다.ㄴ
                listView.setHasFixedSize(true)
                val adaptor = GitHubAdaptor(data)
                listView.layoutManager = LinearLayoutManager(this@GitHubUserRepositoryListActivity)

                listView.adapter = adaptor

                listView.setHasFixedSize(true)
            }

            override fun onFailure(call: Call<List<GitHubUserRepo>>, t: Throwable) {

            }
        })
    }
}