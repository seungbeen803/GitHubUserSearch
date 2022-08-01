package com.example.githubusersearch

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.lang.reflect.Type
interface GitHubAPIService {
// https://api.github.com/users/seungbeen803
    @GET("/users/{id}")
    fun getGitHubInfo(
        @Path("id") id: String,
        @Header("Authorization") pat: String
    ) : Call<GitHubResponseGSON>
}

data class GitHubResponseGSON(
    val login: String,
    val id: Int,
    // 값이 없을 수도 있기 때문
    val name: String?,
    val followers: Int,
    val following: Int,
    // _를 쓰기 위해서 SerializedName을 사용
    @SerializedName("avatar_url")
    val avatarUrl: String
)

/*
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
        val name = root?.getAsJsonPrimitive("name")?.asString
        val followers = root?.getAsJsonPrimitive("followers")?.asInt
        val following = root?.getAsJsonPrimitive("following")?.asInt

        // 값이 받드시 있다는 걸 가정하고 있기 때문에 !!
        return GitHubResponseGSON(login!!, id!!, name!!, followers!!, following!!)
    }

}*/