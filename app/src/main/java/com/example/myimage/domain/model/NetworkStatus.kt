package com.example.myimage.domain.model


sealed class NetworkStatus {
    data object CONNECTED : NetworkStatus()
    data object DISCONNECTED : NetworkStatus()
}
