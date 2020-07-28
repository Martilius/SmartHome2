package com.martilius.smarthome.repository.remote

import androidx.room.FtsOptions
import com.martilius.smarthome.models.*
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

    @PUT("changeLedState")
    suspend fun changeLedState(
        @Query("ip")ip: String,
        @Query("red")red:Int,
        @Query("green")green:Int,
        @Query("blue")blue:Int
    )

    @GET("roomsList")
    suspend fun findRoomsList(
    ):List<Rooms>

    @GET("addRoom")
    suspend fun addRoom(
        @Query("roomName")roomName:String
    ):Respond

    @GET("findDeviceTypes")
    suspend fun findDeviceType():List<DeviceType>


//    @GET("volumes/{volumeId}")
//    suspend fun findBookByID(
//        @Path("volumeId") VolumeId: String
//    ): Books
}
