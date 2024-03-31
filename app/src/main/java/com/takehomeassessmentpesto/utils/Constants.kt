package com.takehomeassessmentpesto.utils

object Constants {

    const val VALIDATION_ERROR = "Oops Something went wrong.Please try again later"
    const val MSG_NO_INTERNET_CONNECTION = "The internet connection appears to be offline"

    const val MSG_TASK_ADDED_SUCCESSFUL = "Task added successfully"
    const val MSG_TASK_UPDATE_SUCCESSFUL = "Task updated successfully"
    const val MSG_TASK_DELETE_SUCCESSFUL = "Task deleted successfully"

    //status
    const val TO_DO_STATUS = "To Do"
    const val IN_PROGRESS_STATUS = "In Progress"
    const val DONE_STATUS = "Done"

    /*******Intents Key********/
    const val INTENT_ISEDIT = "isEdit"
    const val INTENT_TASK_MODEL = "taskmodel"

    /*******Table********/
    const val TABLE_TASK = "taskhistory"


    const val FIELD_TASK_ID = "taskid"
    const val FIELD_UID = "uid"
    const val FIELD_TITLE = "title"
    const val FIELD_DESCRIPTION= "description"
    const val FIELD_STATUS = "status"
    const val FIELD_GROUP_CREATED_AT = "createdat"




    const val ABOUT = """
•	Developed an Android task management app in android studi using Kotlin and MVVM, Dagger Hilt.
    
•	Utilized Firebase Firestore for seamless database integration.
    
•	Features:
    o	View tasks on the main screen.
    
    o	Add, update, and delete tasks on the secondary screen.
    
    o	Apply filters to customize task lists.
        
•	Continuously refining user experience, optimizing performance, and ensuring robust security measures.
    
•	Code is available in my repo: https://github.com/Miteshmakwana73/Take-Home-Assessment-Pesto
    """

    fun checkValidation(title:String): Boolean {
        return title.isBlank()
    }

}