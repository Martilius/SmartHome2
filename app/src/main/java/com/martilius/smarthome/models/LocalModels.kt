package com.martilius.smarthome.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Cupboard(
    val name:String,
    val message:String
)

data class CupboardwithState(
    val cupboard: Cupboard,
    val turnedOn: Boolean
)




@Entity(tableName = "configuration")
data class Configuration(
    @PrimaryKey
    @SerializedName("id") val id:Int,
    @SerializedName("ip") val ip:String,
    @SerializedName("deviceName") val name:String,
    @SerializedName("red") val red:Int,
    @SerializedName("green") val green:Int,
    @SerializedName("blue") val blue:Int,
    @SerializedName("deviceState") val state:String,
    @SerializedName("room") val room:String,
    @SerializedName("deviceType") val deviceType:String
)

data class findDevicesConfigurationResponse(
    val devices: List<Configuration>
)

data class Respond(
    @PrimaryKey
    @SerializedName("respond") val respond:String
)

data class Rooms(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("room") val roomName: String
)