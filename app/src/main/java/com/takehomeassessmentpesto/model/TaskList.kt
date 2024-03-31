package com.takehomeassessmentpesto.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import com.takehomeassessmentpesto.utils.Constants

object TaskList {

    /**
     * make array list of from firestore
     */
    fun getUserTaskArrayList(
        querySnapshot: QuerySnapshot,
        userId: String
    ): ArrayList<TaskModel> {
        val taskArrayList = ArrayList<TaskModel>()
        for (doc in querySnapshot.documents) {
            val taskModel = TaskModel()

            doc.get(Constants.FIELD_TASK_ID)?.let {
                taskModel.id = it.toString()
            }
            doc.get(Constants.FIELD_TITLE)?.let {
                taskModel.title = it.toString()
            }

            doc.get(Constants.FIELD_DESCRIPTION)?.let {
                taskModel.description = it.toString()
            }
            doc.get(Constants.FIELD_STATUS)?.let {
                taskModel.status = it.toString()
            }
            doc.get(Constants.FIELD_UID)?.let {
                taskModel.userId = it.toString()
            }

            doc.get(Constants.FIELD_GROUP_CREATED_AT)?.let {
                val tm = it as Timestamp
                taskModel.createdAt = tm
            }

            if (doc.get(Constants.FIELD_UID) == userId) {
                taskArrayList.add(taskModel)
            }

        }
        return taskArrayList
    }
}