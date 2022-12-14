package com.sms.smsforwarder.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(

    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "sender") val sender: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "status") var status: Int,
):java.io.Serializable