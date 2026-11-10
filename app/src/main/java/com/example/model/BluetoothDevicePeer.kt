package com.example.model

data class BluetoothDevicePeer(
  val id: String,
  val name: String,
  val deviceAddress: String,
  val rssi: Int, // Signal strength in dBm (e.g. -45 dBm = Very Strong)
  val isBhakariUser: Boolean = true,
  val associatedUser: User? = null,
  val isConnected: Boolean = false,
  val transferStatus: TransferStatus = TransferStatus.IDLE
)

enum class TransferStatus {
  IDLE,
  CONNECTING,
  CONNECTED,
  SENDING,
  COMPLETED,
  FAILED
}
