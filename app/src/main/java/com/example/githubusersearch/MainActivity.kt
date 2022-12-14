package com.example.githubusersearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
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

        val userName = findViewById<EditText>(R.id.userName)
        val resultText = findViewById<TextView>(R.id.result)
        val profileImage = findViewById<ImageView>(R.id.avatar_url)
        // 바깥에서 만드는 것이 좋음
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            // 선언을 하면 역직렬화 할 때에 Gson을 사용함 (역직렬화 방법)
            .addConverterFactory(
                GsonConverterFactory.create(
                    /*
                    GsonBuilder().registerTypeAdapter(
                        // 역직렬화할 클래스를 지정
                        GitHubResponseGSON::class.java,
                        GitHubResponseDeserializerGSON()
                    ).create()
                     */
                )
            ).build()

        // 클래스 정보를 읽어서 메서드의 정보나 파라미터 등을 알 수 있음
        // Class<GitHubAPIService> = GitHubAPIService::class.java
        val apiService = retrofit.create(GitHubAPIService::class.java)
        // 호출할 수 있는 준비를 끝냄
        findViewById<Button>(R.id.search_btn).setOnClickListener {
            val id = userName.text.toString()
            if(id.isBlank()){
                Toast.makeText(this, "유저 아이디를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
            val apiCallForData = apiService.getGitHubInfo(id, "token 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")
            apiCallForData.enqueue(object : Callback<GitHubResponseGSON> {
                override fun onResponse(
                    call: Call<GitHubResponseGSON>,
                    response: Response<GitHubResponseGSON>
                ) {
                    // statsWith -> 문자열의 첫 글자를 찾는 것
                    if (response.code().toString().startsWith("4")){
                        Toast.makeText(this@MainActivity,
                        "유저가 없습니다",
                        Toast.LENGTH_SHORT).show()
                    }else{
                        Log.d("mytag",response.code().toString())
                        // 데이터 가져옴
                        // 숨긴 것을 보여주게 하는 기능을 함
                        findViewById<Button>(R.id.to_user_repo_search).visibility = View.VISIBLE
                        val data = response.body()!!
                        Log.d("mytag", data.toString())

                        // "${}"이 자체가 String이기 때문에 간단하게 작성 가능
                        resultText.text = "login: ${data.login}\nid: ${data.id}\nname: ${data.name}\nfollowers: ${data.followers}\nfollowing: ${data.following}"
                        // 역직렬화로 가져온 이미지 url 표시
                        // this@MainActivity -> 바깥에 MainActivity의 this를 가리키기 위함
                        // 이 안의 this는 object의 Callback 받기 때문에 this@MainActivity 이렇게 작성
                        Glide.with(this@MainActivity).load(data.avatarUrl).into(profileImage);
                    }
                }

                override fun onFailure(call: Call<GitHubResponseGSON>, t: Throwable) {
                    Toast.makeText(this@MainActivity,
                        "에러 발생 : ${t.message}",
                        Toast.LENGTH_SHORT).show()
                }
            })


        }

        findViewById<Button>(R.id.to_user_repo_search).setOnClickListener {
            // intent -> 다른 액티비티로 이동하도록 해줌
            // 액티비티로 이동해야하기 때문에 이동할 액티비티의 이름을 넣음
            val intent = Intent(this, GitHubUserRepositoryListActivity::class.java)
            // id의 값을 넘겨줌
            // id 값을 가져오는 EditText를 가져옴
            intent.putExtra("userid", userName.text.toString())
            startActivity(intent)
        }
    }
}