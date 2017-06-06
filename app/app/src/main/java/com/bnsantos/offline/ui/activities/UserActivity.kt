package com.bnsantos.offline.ui.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bnsantos.offline.R
import com.bnsantos.offline.models.User
import com.bnsantos.offline.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : BaseActivity<UserViewModel>(UserViewModel::class.java){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        save.setOnClickListener {
            saveUser()
        }

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

    private fun newUser(){
        id.setText("")
        name.setText("")
        email.setText("")

        id.isEnabled = true
    }

    private fun saveUser(){
        id.isEnabled = false
        mViewModel.save(id.text.toString(), name.text.toString(), email.text.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.user, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.newUser){
            newUser()
            return true
        }else {
            return super.onOptionsItemSelected(item)
        }
    }
}