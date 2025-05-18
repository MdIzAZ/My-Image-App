package com.example.myimage.data.repo

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.myimage.domain.model.NetworkStatus
import com.example.myimage.domain.repo.NetworkConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

class NetworkConnectivityObserverImp(context: Context, scope: CoroutineScope) : NetworkConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _networkStatus = connectivityObserver().stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NetworkStatus.DISCONNECTED
    )

    override val networkStatus: StateFlow<NetworkStatus>
        get() = _networkStatus


    private fun connectivityObserver(): Flow<NetworkStatus> {

        return callbackFlow {

            val connectivityCallback = object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(NetworkStatus.CONNECTED)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(NetworkStatus.DISCONNECTED)
                }
            }

            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()

            connectivityManager.registerNetworkCallback(request, connectivityCallback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(connectivityCallback)
            }


        }.distinctUntilChanged()

    }


}