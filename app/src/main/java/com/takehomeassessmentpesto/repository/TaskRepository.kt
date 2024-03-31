package com.takehomeassessmentpesto.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.takehomeassessmentpesto.model.TaskModel
import com.takehomeassessmentpesto.utils.Constants
import com.google.firebase.firestore.Query
import javax.inject.Inject

class TaskRepository @Inject constructor() {

    private var firestoreDB = FirebaseFirestore.getInstance()

    //booking
    fun getBookingAll(): CollectionReference {
        return firestoreDB.collection(Constants.TABLE_TASK)
    }

    fun getAllTaskByUserRepository(
        userId: String,
    ): Query {
        return firestoreDB.collection(Constants.TABLE_TASK)
            .whereEqualTo(Constants.FIELD_UID, userId)
            .orderBy(Constants.FIELD_GROUP_CREATED_AT, Query.Direction.DESCENDING)
    }
    fun getAllUserTaskByFilterRepository(
        userId: String,
        filter: String,
    ): Query {
        return firestoreDB.collection(Constants.TABLE_TASK)
            .whereEqualTo(Constants.FIELD_UID, userId)
            .whereEqualTo(Constants.FIELD_STATUS, filter)
            .orderBy(Constants.FIELD_GROUP_CREATED_AT, Query.Direction.DESCENDING)
    }

    fun getTaskAddRepository(): DocumentReference {
        return firestoreDB.collection(Constants.TABLE_TASK).document()
    }

    fun getTaskUpdateRepository(
        task: TaskModel
    ): DocumentReference {
        return firestoreDB.collection(Constants.TABLE_TASK).document(task.id)
    }

    fun getBookingDetail(
        bookingId:String
    ): DocumentReference {
        return firestoreDB.collection(Constants.TABLE_TASK).document(bookingId)
    }

}