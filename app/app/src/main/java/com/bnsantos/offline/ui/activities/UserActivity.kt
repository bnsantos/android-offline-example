package com.bnsantos.offline.ui.activities

import android.arch.lifecycle.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.bnsantos.offline.R
import com.bnsantos.offline.models.User
import com.bnsantos.offline.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_user.*
import javax.inject.Inject

class UserActivity : AppCompatActivity(), LifecycleRegistryOwner {
    val mLifecycleRegistry = LifecycleRegistry(this)
    @Inject lateinit var mViewModelFactory: ViewModelProvider.Factory
    lateinit var mViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        clear.setOnClickListener {
            newUser()
        }

        save.setOnClickListener {
            saveUser()
        }

        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(UserViewModel::class.java)
        mViewModel.read().observe(this, Observer<User> { user ->
            if (user != null) {
                id.setText(user.id)
                name.setText(user.name)
                email.setText(user.email)
            }else {
                Toast.makeText(this@UserActivity, R.string.please_create_user, Toast.LENGTH_LONG).show()
                newUser()
            }
        })
    }

    override fun getLifecycle(): LifecycleRegistry {
        return mLifecycleRegistry
    }

    private fun newUser(){
        id.setText("")
        name.setText("")
        email.setText("")

        id.isEnabled = true
        //Clear local user
    }

    private fun saveUser(){
        id.isEnabled = false
        mViewModel.save(id.text.toString(), name.text.toString(), email.text.toString())
    }
}