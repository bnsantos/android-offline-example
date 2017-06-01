package com.bnsantos.offline.ui

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bnsantos.offline.R
import com.bnsantos.offline.models.Comment
import java.util.*


class CommentsAdapter(var mComments: List<Comment> = listOf()) : RecyclerView.Adapter<CommentsAdapter.CommentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CommentHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_comment, parent, false)
        return CommentHolder(view)
    }

    override fun onBindViewHolder(holder: CommentHolder?, pos: Int) {
        val comment = mComments[pos]
        if (holder != null) {
            holder.mName.text = comment.user?.name
            holder.mText.text = comment.text
            holder.mDate.text = comment.createdAt.toString()
        }
    }

    override fun onBindViewHolder(holder: CommentHolder?, position: Int, payloads: MutableList<Any>?) {
        if (payloads?.isEmpty() as Boolean) {
            onBindViewHolder(holder, position)
        }else {
            val bundle = payloads[0] as Bundle
            for (k in bundle.keySet()) {
                if (k == "text") {
                    holder?.mText?.text = bundle.getString(k)
                }else if(k == "user"){
                    holder?.mName?.text = bundle.getString(k)
                }else if(k == "date"){
                    holder?.mDate?.text = Date(bundle.getLong(k)).toString()
                }
            }
        }
    }

    override fun getItemCount(): Int = mComments.size

    fun swap(comments: List<Comment>?) {
        if (comments != null) {
            mComments = comments
        }
    }

    inner class CommentHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val mName: TextView = itemView?.findViewById(R.id.user) as TextView
        val mText: TextView = itemView?.findViewById(R.id.text) as TextView
        val mDate: TextView = itemView?.findViewById(R.id.date) as TextView
    }
}