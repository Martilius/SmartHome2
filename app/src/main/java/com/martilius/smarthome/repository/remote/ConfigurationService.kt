package com.martilius.smarthome.repository.remote

import androidx.room.FtsOptions
import com.martilius.smarthome.models.Configuration
import com.martilius.smarthome.models.findDevicesConfigurationResponse
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ConfigurationService {

    @GET("findByRoom")
    suspend fun findDevicesConfigurationByRoom(
            @Query("room") room: String
    ): List<Configuration>

    @PUT("changeState")
    suspend fun changeState(
        @Query("ip") ip:String,
        @Query("state") state:String
    )


//    @GET("volumes/{volumeId}")
//    suspend fun findBookByID(
//        @Path("volumeId") VolumeId: String
//    ): Books
}