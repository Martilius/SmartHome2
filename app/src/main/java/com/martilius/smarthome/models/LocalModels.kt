package com.martilius.smarthome.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.martilius.smarthome.R

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

data class DeviceSettings(
    val id: Int,
    val name : String,
    val ip : String,
    val room : String,
    var roomsList : List<Rooms>?
)

data class findDevicesConfigurationResponse(
    val devices: List<Configuration>
)

data class UserSettingsRespond(
    @PrimaryKey
    @SerializedName("login") val login:String,
    @SerializedName("admin") val admin:Boolean
)

data class Respond(
    @PrimaryKey
    @SerializedName("respond") val respond:String,
    @SerializedName("admin") val admin:Boolean
)

data class Rooms(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("room") val roomName: String,
    @SerializedName("room_type") val roomType:RoomTypes,
    @SerializedName("admin") val admin:Boolean
)

data class DeviceType(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("device_type") val deviceType: String
)

data class NewDevice(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @SerializedName("ip") val ip: String
)

data class RoomsResponse(
    val bla : List<Rooms>
)

data class RoomModelRespond(
    val room:String,
    val room_type:String
)

enum class RoomTypes(val res:Int){
    @SerializedName("BATHROOM")
    BATHROOM(R.drawable.bathroom),
    @SerializedName("LIVING_ROOM")
    LIVING_ROOM(R.drawable.living_room),
    @SerializedName("BEDROOM")
    BEDROOM(R.drawable.bedroom),
    @SerializedName("GARAGE")
    GARAGE(R.drawable.garage),
    @SerializedName("KITCHEN")
    KITCHEN(R.drawable.kitchen),
    @SerializedName("LIBRARY")
    LIBRARY(R.drawable.library)
}