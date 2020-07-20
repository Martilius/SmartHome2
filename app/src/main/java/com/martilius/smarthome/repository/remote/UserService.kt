package com.martilius.smarthome.repository.remote

import androidx.room.FtsOptions
import com.martilius.smarthome.models.Configuration
import com.martilius.smarthome.models.Respond
import com.martilius.smarthome.models.findDevicesConfigurationResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("register")
    suspend fun register(
        @Query("login") login:String,
        @Query("password") password:String
    ):Respond

    @GET("login")
    suspend fun login(
        @Query("login")login: String,
        @Query("password")password: String
    ):Respond

//    @GET("volumes/{volumeId}")
//    suspend fun findBookByID(
//        @Path("volumeId") VolumeId: String
//    ): Books
}
