package com.sms.smsforwarder.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity
data class Param(

    @PrimaryKey var uid: Int,
    @ColumnInfo(name = "key") var key: String,
    @ColumnInfo(name = "value") var value: String,
    @ColumnInfo(name = "sim") var sim: Int,
    @ColumnInfo(name="date") var date:Long=Calendar.getInstance().timeInMillis



):java.io.Serializable