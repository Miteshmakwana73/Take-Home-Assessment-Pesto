package com.takehomeassessmentpesto.utils

import androidx.lifecycle.MutableLiveData

object Constants {

    const val BASE_URL = "http://app.topsdemo.co.in/webservices/dose_of_society/ws/"  //use this next time

    const val VALIDATION_ERROR = "Oops Something went wrong.Please try again later"
    const val MSG_NO_INTERNET_CONNECTION = "The internet connection appears to be offline"
    const val MSG_SOMETHING_WENT_WRONG = "Something went wrong"

    const val MSG_UPDATE_SUCCESSFULL = "Profile updated successfully"
    const val MSG_BOOKING_UPDATE_SUCCESSFUL = "Booking updated successfully"
    const val MSG_BOOKING_DELETE_SUCCESSFUL = "Booking deleted successfully"
    const val MSG_RATING_SUCCESSFUL = "Rating added successfully"

    //status
    const val TO_DO_STATUS = "To Do"
    const val IN_PROGRESS_STATUS = "In Progress"
    const val DONE_STATUS = "Done"

    /*******Pref Key********/
    const val PREF_FILE = "pref_astrology"
    const val PREF_FCM_TOKEN="fcmtoken"

    /*******Intents Key********/
    const val INTENT_ISEDIT = "isEdit"
    const val INTENT_TASK_MODEL = "taskmodel"
}