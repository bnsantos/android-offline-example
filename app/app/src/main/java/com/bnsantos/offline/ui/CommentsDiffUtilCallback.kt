package com.bnsantos.offline.ui

import android.os.Bundle
import android.support.v7.util.DiffUtil
import com.bnsantos.offline.models.Comment

class CommentsDiffUtilCallback (val mOld: List<Comment>, val mNew: List<Comment>): DiffUtil.Callback() {
    override fun getOldListSize(): Int = mOld.size

    override fun getNewListSize(): Int = mNew.size

    override fun areItemsTheSame(p0: Int, p1: Int): Boolean = mOld[p0].id == mNew[p1].id

    override fun areContentsTheSame(p0: Int, p1: Int): Boolean = mOld[p0] == mNew[p1]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldC = mOld[oldItemPosition]
        val newC = mNew[newItemPosition]

        val bundle = Bundle()

        if (oldC.text != newC.text) {
            bundle.putString("text", newC.text)
        }

        if (oldC.user.name != newC.user.name){
            bundle.putString("user", newC.user.name)
        }

        if (oldC.createdAt != newC.createdAt){
            bundle.putLong("date", newC.createdAt.time)
        }
        return bundle
    }
}