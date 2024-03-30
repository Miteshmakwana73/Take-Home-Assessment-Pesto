package com.takehomeassessmentpesto.model

import android.os.Parcelable
//import com.google.firebase.Timestamp
import com.takehomeassessmentpesto.utils.Constants
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskModel(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var status: String = Constants.TO_DO_STATUS,
//    var createdAt: Timestamp? = null,
    ) : Parcelable