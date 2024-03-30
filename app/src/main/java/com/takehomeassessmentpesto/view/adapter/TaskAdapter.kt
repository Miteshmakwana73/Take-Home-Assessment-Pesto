package com.takehomeassessmentpesto.view.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.takehomeassessmentpesto.R
import com.takehomeassessmentpesto.model.TaskModel
import com.takehomeassessmentpesto.utils.Constants

/**
 * TaskAdapter list adapter : Show Task list.
 */
class TaskAdapter(
    private val mContext: Context, var mList: ArrayList<TaskModel>,
    private val viewHolderClicks: ViewHolderClicks,
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    var mTimeFormat: String = "hh:mm a"
    var mDateFormat: String = "d\nMMM"
    var mInflater: LayoutInflater =
        mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            mInflater.inflate(R.layout.raw_task_list, parent, false)
        return ViewHolder(
            v
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val model = mList[holder.adapterPosition]
        try {
            holder.apply {
                tvTitle.text = model.title
                tvDescription.text = mContext.getString(
                    R.string.descritpion,
                    model.description
                )
                tvTaskId.text = model.id

                var mColor = mContext.getColor(R.color.to_do_color)
                var mStatus = mContext.getString(R.string.to_do)
                when (model.status) {
                    Constants.DONE_STATUS -> {
                        mStatus = mContext.getString(R.string.done)
                        mColor = mContext.getColor(R.color.done_color)
                    }
                    Constants.IN_PROGRESS_STATUS -> {
                        mStatus = mContext.getString(R.string.in_progress)
                        mColor = mContext.getColor(R.color.in_progress_color)
                    }
                    Constants.TO_DO_STATUS -> {
                        mStatus = mContext.getString(R.string.to_do)
                        mColor = mContext.getColor(R.color.to_do_color)
                    }
                }
                tvStatus.text = mStatus
                imgColor.setBackgroundColor(mColor)
                tvStatus.setTextColor(mColor)
            }

            holder.itemView.setOnClickListener {
                viewHolderClicks.onClickItem(model, holder.adapterPosition)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgColor: ImageView = itemView.findViewById(R.id.imgColor)
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)
        var tvTaskId: TextView = itemView.findViewById(R.id.tvTaskId)
        var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    interface ViewHolderClicks {
        fun onClickItem(model: TaskModel, position: Int)
    }
}