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
import androidx.appcompat.app.AppCompatActivity
import com.takehomeassessmentpesto.R
import com.takehomeassessmentpesto.databinding.ActivityAddEditTaskBinding
import com.takehomeassessmentpesto.model.TaskModel
import com.takehomeassessmentpesto.utils.Constants
import com.takehomeassessmentpesto.utils.hideKeyboard
import com.takehomeassessmentpesto.utils.showSnackBarToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditTaskBinding

    var taskModel: TaskModel = TaskModel()
    var isEdit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getIntentData()
        manageStatus()
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
                setResult()
            }
        }
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