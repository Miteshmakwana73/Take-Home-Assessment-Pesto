package com.takehomeassessmentpesto.view

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.RadioGroup
import android.view.MotionEvent
import androidx.activity.viewModels
import com.takehomeassessmentpesto.R
import com.takehomeassessmentpesto.core.BaseActivity
import com.takehomeassessmentpesto.databinding.ActivityAddEditTaskBinding
import com.takehomeassessmentpesto.model.TaskModel
import com.takehomeassessmentpesto.network.Status
import com.takehomeassessmentpesto.utils.Constants
import com.takehomeassessmentpesto.utils.hideKeyboard
import com.takehomeassessmentpesto.utils.makeVisible
import com.takehomeassessmentpesto.utils.showSnackBarToast
import com.takehomeassessmentpesto.utils.toast
import com.takehomeassessmentpesto.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditTaskActivity : BaseActivity() {
    private lateinit var binding: ActivityAddEditTaskBinding

    private var taskModel: TaskModel = TaskModel()
    private var isEdit: Boolean = false

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        manageStatus()
        setObserver()
        setClickListener()
    }

    /**
     * get Intent Data
     */
    private fun getIntentData() {
        isEdit = intent.getBooleanExtra(Constants.INTENT_ISEDIT, false)
        if (isEdit) {
            taskModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(Constants.INTENT_TASK_MODEL, TaskModel::class.java)!!
            } else {
                intent.getParcelableExtra(Constants.INTENT_TASK_MODEL)!!
            }
            binding.apply {
                imgDelete.makeVisible()
                tvTitle.text = getString(R.string.edit_task)
                edTitle.setText(taskModel.title)
                edDetails.setText(taskModel.description)
                tvStatus.text = taskModel.status
            }
        }
    }

    /**
     * manage click listener of view
     */
    private fun setClickListener() {
        binding.imgClose.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.tvStatus.setOnClickListener {
            showStatusDialog()
        }

        binding.btnSave.setOnClickListener {
            hideKeyboard()
            if (checkValidation()) {
                // call api and set result
                addUpdateEvent()
            }
        }

        // this will allow inside scroll on multiline edit text if you have parent nested scrollview
        binding.edDetails.setOnTouchListener { v, event ->
            if (v.id == R.id.edDetails) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }

        binding.imgDelete.setOnClickListener {
            taskViewModel.deleteTask(taskModel.id)
        }
    }

    /**
     * show Notification dialog
     */
    private fun showStatusDialog() {
        val mDialog = Dialog(this)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.dialog_status)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(mDialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.window!!.attributes = lp
        mDialog.show()

        val rgPaymentMode = mDialog.findViewById(R.id.rgStatus) as RadioGroup

        rgPaymentMode.setOnCheckedChangeListener { radioGroup, checkedId ->
            val radioButton = radioGroup.findViewById(checkedId) as RadioButton
            binding.tvStatus.text = radioButton.text.toString()
            manageStatus()
            mDialog.dismiss()
        }
    }

    /**
     * Manage Status color
     */
    private fun manageStatus() {
        var mColor = when (binding.tvStatus.text) {
            getString(R.string.to_do) -> getColor(R.color.to_do_color)
            getString(R.string.in_progress) -> getColor(R.color.in_progress_color)
            getString(R.string.done) -> getColor(R.color.done_color)
            else -> getColor(R.color.to_do_color)
        }
        binding.tvStatus.setTextColor(mColor)
    }

    /**
     * Check Validation
     */
    private fun checkValidation(): Boolean {
        return if (TextUtils.isEmpty(binding.edTitle.text.toString().trim())) {
            binding.root.showSnackBarToast(getString(R.string.please_enter_title))
            false
        } else if (TextUtils.isEmpty(binding.edDetails.text.toString().trim())) {
            binding.root.showSnackBarToast(getString(R.string.please_enter_details))
            false
        } else {
            true
        }
    }

    /**
     * Add/ Update task
     */
    private fun addUpdateEvent() {
        taskModel.title = binding.edTitle.text.toString().trim()
        taskModel.description = binding.edDetails.text.toString().trim()
        taskModel.status = binding.tvStatus.text.toString().trim()
        taskModel.userId = deviceId

        taskViewModel.addUpdateBookingData(taskModel, isEdit)
    }

    /**
     * set observer
     */
    private fun setObserver() {

        taskViewModel.taskAddUpdateResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(this)
                }

                Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let { result ->
                        toast(result)
//                        taskModel = result
                        setResult()

                    }
                }

                Status.ERROR -> {
                    hideProgress()
                    it.message?.let { it1 -> binding.root.showSnackBarToast(it1) }
                }
            }
        }

        taskViewModel.taskDeleteResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(this)
                }

                Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let { result ->
                        toast(result)
                        taskModel = TaskModel()
                        setResult()
                    }
                }

                Status.ERROR -> {
                    hideProgress()
                    it.message?.let { it1 -> binding.root.showSnackBarToast(it1) }
                }
            }
        }
    }

    /**
     * set result and pass data to previous activity
     */
    private fun setResult() {
        setResult(
            Activity.RESULT_OK,
            Intent().putExtra(Constants.INTENT_TASK_MODEL, taskModel)
        )
        onBackPressedDispatcher.onBackPressed()
    }
}