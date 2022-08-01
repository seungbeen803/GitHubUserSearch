package com.example.githubusersearch

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.reflect.Type

interface GitHubAPIService {
// https://api.github.com/users/seungbeen803
    @GET("/users/{id}")
    fun getGitHubInfo(
        @Path("id") id: String
    ) : Call<GitHubResponseGSON>
}

data class GitHubResponseGSON(
    val login: String,
    val id: Int
)

class GitHubResponseDeserializerGSON : JsonDeserializer<GitHubResponseGSON> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GitHubResponseGSON {
        val root = json?.asJsonObject
//      getter를 속성처럼 접근할 수 있도록 바꿔준다
        // 어떤 타입인지 모르기 때문에 직접적으로 타입을 지정해준다
        val id = root?.getAsJsonPrimitive("id")?.asInt
        val login = root?.getAsJsonPrimitive("login")?.asString

        // 값이 받드시 있다는 걸 가정하고 있기 때문에 !!
        return GitHubResponseGSON(login!!, id!!)
    }

}