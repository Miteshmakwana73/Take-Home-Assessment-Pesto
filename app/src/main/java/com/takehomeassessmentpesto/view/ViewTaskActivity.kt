package com.takehomeassessmentpesto.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.takehomeassessmentpesto.databinding.ActivityViewTaskBinding
import com.takehomeassessmentpesto.model.TaskModel
import com.takehomeassessmentpesto.utils.Constants
import com.takehomeassessmentpesto.utils.makeGone
import com.takehomeassessmentpesto.utils.makeVisible
import com.takehomeassessmentpesto.view.adapter.TaskAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewTaskBinding

    private var mList: ArrayList<TaskModel> = arrayListOf()
    private var taskModel = TaskModel()
    private var mPositionEdit = 0

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                //  you will get result here in result.data
                var mData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(Constants.INTENT_TASK_MODEL, TaskModel::class.java)
                } else {
                    result.data?.getParcelableExtra(Constants.INTENT_TASK_MODEL)
                }
                mList[mPositionEdit] = mData!!
                binding.rvBooking.adapter?.notifyItemChanged(mPositionEdit)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mList.add(TaskModel())
        mList.add(TaskModel())
        mList.add(TaskModel())
        mList.add(TaskModel())

        with(binding.rvBooking) {
            val mLayoutManager = LinearLayoutManager(context)
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = TaskAdapter(
                context, mList,
                object : TaskAdapter.ViewHolderClicks {
                    override fun onClickItem(
                        model: TaskModel,
                        position: Int
                    ) {
                        //click of recyclerview item
                        taskModel = model
                        mPositionEdit = position
                        launchActivity(true)
                    }
                }
            )
        }

        binding.fbAdd.setOnClickListener {
            launchActivity(false)
        }
        manageData()
    }

    /**
     * Launch Activity
     */
    private fun launchActivity(isEdit: Boolean) {
        startForResult.launch(
            Intent(this, AddEditTaskActivity::class.java)
                .putExtra(Constants.INTENT_ISEDIT, isEdit)
                .putExtra(Constants.INTENT_TASK_MODEL, taskModel)
        )
    }

    /**
     * Manage screen on empty list
     */
    private fun manageData() {
        if (mList.isEmpty()) {
            binding.rvBooking.makeGone()
            binding.tvNoDataFound.makeVisible()
        } else {
            binding.rvBooking.makeVisible()
            binding.tvNoDataFound.makeGone()
        }
    }
}