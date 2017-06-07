package com.bnsantos.offline.ui.activities

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.bnsantos.offline.R
import com.bnsantos.offline.models.User
import com.bnsantos.offline.viewmodel.UserViewModel
import com.bnsantos.offline.vo.Resource
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : BaseActivity<UserViewModel>(UserViewModel::class.java){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        showLoading(true)

        save.setOnClickListener {
            saveUser()
        }

        mViewModel.read().observe(this, observer(false))
    }

    private fun observer(saving: Boolean): Observer<Resource<User>> {
        return Observer {
            showLoading(false)
            when (it) {
                is Resource.Error -> showError(it.message)
                is Resource.Loading -> showUser(it.data)
                is Resource.Success -> showUser(it.data)
            }
            if (saving){
                finish()
            }
        }
    }

    private fun showUser(user: User){
        id.setText(user.id)
        name.setText(user.name)
        email.setText(user.email)
    }

    private fun newUser(){
        id.setText("")
        name.setText("")
        email.setText("")

        id.isEnabled = true
    }

    private fun saveUser(){
        id.isEnabled = false
        showLoading(true)
        mViewModel.save(id.text.toString(), name.text.toString(), email.text.toString()).observe(this, observer(true))
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

    private fun showLoading(show: Boolean){
        if (show) {
            progressBar.visibility = VISIBLE
            idInputLayout.visibility = GONE
            nameInputLayout.visibility = GONE
            emailInputLayout.visibility = GONE
            save.visibility = GONE
        }else {
            progressBar.visibility = GONE
            idInputLayout.visibility = VISIBLE
            nameInputLayout.visibility = VISIBLE
            emailInputLayout.visibility = VISIBLE
            save.visibility = VISIBLE
        }
    }

    private fun showError(error: String){
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }
}