package com.martilius.smarthome.repository

import com.martilius.smarthome.repository.remote.ConfigurationService
import com.martilius.smarthome.repository.remote.UserService

class Repository(
        private val configurationService: ConfigurationService,
        private val userService: UserService
) {

    suspend fun findConfigurationByRoom(room:String) = configurationService.findDevicesConfigurationByRoom(room)

    suspend fun changeState(ip:String,state:String) = configurationService.changeState(ip,state)

    suspend fun registerRequest(login:String, password:String) = userService.register(login,password)

    suspend fun loginRequest(login: String, password: String) = userService.login(login,password)

    suspend fun findRooms() = configurationService.findRoomsList()

    suspend fun addRoom(roomName:String) = configurationService.addRoom(roomName)

    suspend fun findDeviceType() = configurationService.findDeviceType()
}
