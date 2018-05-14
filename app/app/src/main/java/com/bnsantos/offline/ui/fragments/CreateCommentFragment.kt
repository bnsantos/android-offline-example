package com.bnsantos.offline.ui.fragments

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bnsantos.offline.R
import com.bnsantos.offline.di.Injectable
import com.bnsantos.offline.models.Comment
import com.bnsantos.offline.viewmodel.CreateCommentViewModel
import com.bnsantos.offline.vo.Resource
import kotlinx.android.synthetic.main.fragment_create.*
import javax.inject.Inject

class CreateCommentFragment : LifecycleFragment(), Injectable{
    @Inject lateinit var mViewModelFactory: ViewModelProvider.Factory
    private lateinit var mViewModel: CreateCommentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_create, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CreateCommentViewModel::class.java)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        send.setOnClickListener {
            mViewModel.create(input.text.toString()).observe(this@CreateCommentFragment, Observer<Resource<Comment>> {
                when(it){
                    is Resource.Success -> Toast.makeText(context, "Uhuuu", Toast.LENGTH_SHORT).show()
                    is Error -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            })
            input.setText("")
        }
    }
}