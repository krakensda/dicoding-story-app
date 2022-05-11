package com.bangkit.intermediate.dicodingstoryapp.data.remote.retrofit

import com.bangkit.intermediate.dicodingstoryapp.data.remote.response.LoginResponse
import com.bangkit.intermediate.dicodingstoryapp.data.remote.response.BaseResponse
import com.bangkit.intermediate.dicodingstoryapp.data.remote.response.StoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): BaseResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryListResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") authorization: String,
        @Query("location") location: Int = 1
    ): StoryListResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : BaseResponse

}