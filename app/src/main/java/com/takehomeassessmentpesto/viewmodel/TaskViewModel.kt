package com.takehomeassessmentpesto.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.takehomeassessmentpesto.repository.TaskRepository
import com.takehomeassessmentpesto.network.NetworkHelper
import com.takehomeassessmentpesto.network.Resource
import com.google.firebase.Timestamp
import com.takehomeassessmentpesto.model.TaskList
import com.takehomeassessmentpesto.model.TaskModel
import com.takehomeassessmentpesto.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _taskAddUpdateResponse: MutableLiveData<Resource<String>> = MutableLiveData()
    val taskAddUpdateResponse: LiveData<Resource<String>> get() = _taskAddUpdateResponse

    private val _bookingExtendResponse: MutableLiveData<Resource<String>> = MutableLiveData()
    val bookingExtendResponse: LiveData<Resource<String>> get() = _bookingExtendResponse

    private val _taskDeleteResponse: MutableLiveData<Resource<String>> = MutableLiveData()
    val taskDeleteResponse: LiveData<Resource<String>> get() = _taskDeleteResponse

    var bookingList: MutableLiveData<ArrayList<TaskModel>> = MutableLiveData()

    private val _getTaskListDataResponse: MutableLiveData<Resource<ArrayList<TaskModel>>> =
        MutableLiveData()
    val getTaskListDataResponse: LiveData<Resource<ArrayList<TaskModel>>> get() = _getTaskListDataResponse

    private val _getBookingDetailResponse: MutableLiveData<Resource<TaskModel>> =
        MutableLiveData()
    val getBookingDetailResponse: LiveData<Resource<TaskModel>> get() = _getBookingDetailResponse

    private val _bookingUploadPhotoResponse: MutableLiveData<Resource<String>> = MutableLiveData()
    val bookingUploadPhotoResponse: LiveData<Resource<String>> get() = _bookingUploadPhotoResponse

    /**
     * uploading profile picture to firebase storage
     */
    fun addUpdateBookingData(user: TaskModel, isForUpdate: Boolean) {

        _taskAddUpdateResponse.value = Resource.loading(null)
        if (networkHelper.isNetworkConnected()) {
            if (isForUpdate) {
                updateTaskData(user)
            } else {
                addTaskData(user)
            }
        } else {
            _taskAddUpdateResponse.value = Resource.error(Constants.MSG_NO_INTERNET_CONNECTION, null)
        }

    }

    /**
     * Adding task info in firebase
     */
    private fun addTaskData(user: TaskModel) {
        _taskAddUpdateResponse.value = Resource.loading(null)
        val ref = taskRepository.getTaskAddRepository()
        user.id = ref.id
        user.createdAt = Timestamp.now()

        val data = hashMapOf(
            Constants.FIELD_TASK_ID to user.id,
            Constants.FIELD_UID to user.userId,
            Constants.FIELD_TITLE to user.title,
            Constants.FIELD_DESCRIPTION to user.description,
            Constants.FIELD_STATUS to user.status,
            Constants.FIELD_GROUP_CREATED_AT to user.createdAt,
        )

        ref.set(data)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    _taskAddUpdateResponse.postValue(
                        Resource.success(
                            "${Constants.MSG_TASK_ADDED_SUCCESSFUL}",
                        )
                    )
                }

            }.addOnFailureListener {
                _taskAddUpdateResponse.postValue(
                    Resource.error(
                        Constants.VALIDATION_ERROR,
                        null
                    )
                )
            }
    }

    /**
     * Updating booking info in firebase
     */
    private fun updateTaskData(task: TaskModel) {

        _taskAddUpdateResponse.value = Resource.loading(null)
//        task.createdAt = Timestamp.now()

        var map = HashMap<String, Any>()
        task.title.let { map.put(Constants.FIELD_TITLE, it) }
        task.description.let { map.put(Constants.FIELD_DESCRIPTION, it) }
        task.status.let { map.put(Constants.FIELD_STATUS, it) }


        taskRepository.getTaskUpdateRepository(task)
            .update(map)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    _taskAddUpdateResponse.postValue(
                        Resource.success(
                            "update ${Constants.MSG_TASK_UPDATE_SUCCESSFUL}",
                        )
                    )
                }
            }.addOnFailureListener {
                _taskAddUpdateResponse.postValue(
                    Resource.error(
                        Constants.VALIDATION_ERROR,
                        null
                    )
                )
            }
    }

    /**
     * delete time slot
     */
    fun deleteTask(taskId: String) {

        if (networkHelper.isNetworkConnected()) {
            _taskDeleteResponse.value = Resource.loading(null)
            val ref = taskRepository.getBookingAll()

            ref.document(taskId).delete()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _taskDeleteResponse.value = Resource.success(Constants.MSG_BOOKING_DELETE_SUCCESSFUL)
                    }
                }
                .addOnFailureListener {
                    _taskDeleteResponse.value = Resource.error(it.localizedMessage, null)
                }

        } else {
            _taskDeleteResponse.value = Resource.error(Constants.MSG_NO_INTERNET_CONNECTION, null)
        }

    }

    /**
     * Get all task list user wise
     */
    fun getUserAllTaskList(userId: String) {

        _getTaskListDataResponse.value = Resource.loading(null)

        taskRepository.getAllTaskByUserRepository(userId).get()
            .addOnSuccessListener {
                val mList = TaskList.getUserBookingArrayList(it, userId)
                _getTaskListDataResponse.postValue(
                    Resource.success(
                        mList
                    )
                )
            }.addOnFailureListener {

                _getTaskListDataResponse.postValue(
                    Resource.error(
                        Constants.VALIDATION_ERROR,
                        null
                    )
                )
            }
    }

    /**
     * Get all task list filter wise
     */
    fun getUserFilteredTaskList(userId: String, filter: String) {

        _getTaskListDataResponse.value = Resource.loading(null)

        taskRepository.getAllUserTaskByFilterRepository(userId,filter).get()
            .addOnSuccessListener {
                val mList = TaskList.getUserBookingArrayList(it, userId)
                _getTaskListDataResponse.postValue(
                    Resource.success(
                        mList
                    )
                )
            }.addOnFailureListener {

                _getTaskListDataResponse.postValue(
                    Resource.error(
                        Constants.VALIDATION_ERROR,
                        null
                    )
                )
            }
    }

    /**
     * Get all pending booking info
     */
    fun getTaskDetail(taskId: String) {
        if (networkHelper.isNetworkConnected()) {
            _getBookingDetailResponse.value = Resource.loading(null)

            taskRepository.getBookingDetail(taskId)
                .addSnapshotListener { value, error ->
                    if (error!=null) {
                        _getBookingDetailResponse.postValue(
                            Resource.error(
                                error.message.toString(),
                                null
                            )
                        )

                    }
//                    var bookingDetail = BookingList.getBookingDetail(value!!)
//                    _getBookingDetailResponse.postValue(Resource.success(bookingDetail))
                }

        } else {
            _getBookingDetailResponse.postValue(
                Resource.error(
                    Constants.MSG_NO_INTERNET_CONNECTION,
                    null
                )
            )
        }
    }
}