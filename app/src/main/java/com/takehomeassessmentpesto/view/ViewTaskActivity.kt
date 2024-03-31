package com.takehomeassessmentpesto.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.takehomeassessmentpesto.R
import com.takehomeassessmentpesto.core.BaseActivity
import com.takehomeassessmentpesto.databinding.ActivityViewTaskBinding
import com.takehomeassessmentpesto.model.TaskModel
import com.takehomeassessmentpesto.network.Status
import com.takehomeassessmentpesto.utils.Constants
import com.takehomeassessmentpesto.utils.makeGone
import com.takehomeassessmentpesto.utils.makeVisible
import com.takehomeassessmentpesto.utils.showSnackBarToast
import com.takehomeassessmentpesto.view.adapter.TaskAdapter
import com.takehomeassessmentpesto.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewTaskActivity : BaseActivity() {
    private lateinit var binding: ActivityViewTaskBinding

    private var mList: ArrayList<TaskModel> = arrayListOf()
    private var taskModel = TaskModel()
    private val DEFAULT_POSITION_VALUE = -1
    private var mPositionEdit = DEFAULT_POSITION_VALUE

    private val taskViewModel: TaskViewModel by viewModels()

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                //  you will get result here in result.data
                var mData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(Constants.INTENT_TASK_MODEL, TaskModel::class.java)
                } else {
                    result.data?.getParcelableExtra(Constants.INTENT_TASK_MODEL)
                }
                if (mPositionEdit >= 0) {
                    if(mData!!.id.isBlank()) {
                        mList.removeAt(mPositionEdit)
                        binding.rvBooking.adapter?.notifyItemRemoved(mPositionEdit)
                    } else{
                        mList[mPositionEdit] = mData
                        binding.rvBooking.adapter?.notifyItemChanged(mPositionEdit)
                    }
                    mPositionEdit = DEFAULT_POSITION_VALUE
                    manageData()
                } else {
                    startFromFresh()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setObserver()
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

        binding.imgMore.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.filter, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.title) {
                    getString(R.string.about) -> {
                        startActivity(Intent(this, AboutActivity::class.java))
                    }

                    getString(R.string.all) -> {
                        startFromFresh()
                    }

                    else -> {
                        taskViewModel.getUserFilteredTaskList(
                            deviceId,
                            item.title?.trim().toString()
                        )
                    }
                }
                true
            }
            popupMenu.show()
        }
        startFromFresh()

    }

    private fun startFromFresh() {
        taskViewModel.getUserAllTaskList(deviceId)
    }

    /**
     * set observer
     */
    private fun setObserver() {
        taskViewModel.getTaskListDataResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(this)
                }

                Status.SUCCESS -> {
                    hideProgress()
                    it.data?.let { result ->
                        mList.clear()
                        mList.addAll(result)
                        binding.rvBooking.adapter?.notifyDataSetChanged()
                        manageData()
                    }
                }

                Status.ERROR -> {
                    hideProgress()
                    manageData()
                    it.message?.let { it1 -> binding.root.showSnackBarToast(it1) }
                }
            }
        }


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