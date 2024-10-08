package com.ufabc.docchain.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ufabc.docchain.R
import com.ufabc.docchain.databinding.LoginBinding
import com.ufabc.docchain.presentation.shared.ActivityStatus
import com.ufabc.docchain.presentation.shared.ActivityStatus.LOADING
import com.ufabc.docchain.presentation.shared.ActivityStatus.NORMAL
import com.ufabc.docchain.presentation.login.LoginViewModelAction.ShowEmptyEmailInputToast
import com.ufabc.docchain.presentation.login.LoginViewModelAction.ShowEmptyPasswordInputToast
import com.ufabc.docchain.presentation.login.LoginViewModelAction.ShowFailAuthenticationToast
import com.ufabc.docchain.presentation.login.LoginViewModelAction.StartMenuActivity
import com.ufabc.docchain.presentation.menu.MenuActivity
import com.ufabc.docchain.presentation.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var binding: LoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.login)

        setupViews()
        setupViewModelEvents()
    }

    private fun setupViews() {
        binding.loginRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)

            startActivity(intent)
        }

        binding.loginLoginButton.setOnClickListener {
            val email = binding.loginUserTextInputEditText.text.toString()
            val password = binding.loginPasswordTextInputEditText.text.toString()

            viewModel.submitLogin(email, password)
        }
    }

    private fun setupViewModelEvents() {
        viewModel.state.observe(this) { state ->
            handleViewModelState(state)
        }

        viewModel.action.observe(this) { action ->
            handleViewModelAction(action)
        }
    }

    private fun handleViewModelState(state: LoginViewModelState) {
        updateLoginActivityStatus(state.loginStatus)
    }

    private fun handleViewModelAction(action: LoginViewModelAction) {
        when (action) {
            is ShowEmptyEmailInputToast -> {
                Toast.makeText(
                    this,
                    getString(R.string.login_activity_email_validation_empty_message),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ShowEmptyPasswordInputToast -> {
                Toast.makeText(
                    this,
                    getString(R.string.login_activity_password_validation_empty_message),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ShowFailAuthenticationToast -> {
                Toast.makeText(
                    this,
                    getString(R.string.login_activity_fail_authentication_text),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is StartMenuActivity -> {
                Toast.makeText(
                    this,
                    getString(R.string.login_activity_authentication_success),
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this, MenuActivity::class.java)

                intent.putExtra(MENU_ACTIVITY_INTENT_TAG, action.loggedInUserName)

                startActivity(intent)
            }
        }
    }

    private fun updateLoginActivityStatus(status: ActivityStatus) {
        when (status) {
            NORMAL -> {
                binding.loginLoginButton.visibility = VISIBLE
                binding.loginProgressBar.visibility = GONE
            }

            LOADING -> {
                binding.loginLoginButton.visibility = GONE
                binding.loginProgressBar.visibility = VISIBLE
            }
        }
    }

    companion object {
        const val MENU_ACTIVITY_INTENT_TAG = "LoggedInUserName"
    }
}