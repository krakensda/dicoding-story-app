package com.bangkit.intermediate.dicodingstoryapp.ui.auth.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import com.bangkit.intermediate.dicodingstoryapp.data.remote.request.RegisterRequest
import com.bangkit.intermediate.dicodingstoryapp.data.repository.Result
import com.bangkit.intermediate.dicodingstoryapp.databinding.ActivityRegisterBinding
import com.bangkit.intermediate.dicodingstoryapp.ui.auth.RegisterViewModel
import com.bangkit.intermediate.dicodingstoryapp.ui.auth.login.LoginActivity
import com.bangkit.intermediate.dicodingstoryapp.ui.component.CustomEmailEditText
import com.bangkit.intermediate.dicodingstoryapp.ui.component.CustomPasswordEditText
import com.bangkit.intermediate.dicodingstoryapp.ui.helper.BaseActivity
import com.bangkit.intermediate.dicodingstoryapp.ui.helper.FormValidator
import com.bangkit.intermediate.dicodingstoryapp.ui.helper.ViewModelFactory

class RegisterActivity : BaseActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: CustomEmailEditText
    private lateinit var passwordEditText: CustomPasswordEditText
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar
    private var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.savedInstanceState = savedInstanceState
        setupView(binding)
        setupViewModel()
        setupAction()
    }

    override fun setupView(viewBinding: Any) {
        val binding = viewBinding as ActivityRegisterBinding
        nameEditText = binding.nameEditText
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText
        registerButton = binding.registerButton
        progressBar = binding.progressBar
        supportActionBar?.hide()
        setRegisterButtonEnable()

        if (savedInstanceState != null) {
            val savedInstanceState = savedInstanceState as Bundle
            val emailText = savedInstanceState.getString(LoginActivity.STATE_EMAIL)
            val passwordText = savedInstanceState.getString(LoginActivity.STATE_PASSWORD)
            val nameText = savedInstanceState.getString(STATE_NAME)
            if (emailText != null) emailEditText.setText("$emailText")
            if (passwordText != null) passwordEditText.setText("$passwordText")
            if (nameText != null) nameEditText.setText("$nameText")
        }
    }

    override fun setupViewModel() {
        val factory = ViewModelFactory.getAuthInstance(this)
        val viewModel: RegisterViewModel by viewModels { factory }
        this.viewModel = viewModel
    }

    override fun setupAction() {
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setRegisterButtonEnable()
            }
        })

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setRegisterButtonEnable()
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setRegisterButtonEnable()
            }
        })

        registerButton.setOnClickListener {
            val name = nameEditText.text.trim().toString()
            val email = emailEditText.text?.trim().toString()
            val password = passwordEditText.text?.trim().toString()
            val request = RegisterRequest(name, email, password)

            (viewModel as RegisterViewModel).register(request).observe(this) { result ->
                if (result == null) return@observe

                when (result) {
                    is Result.Loading -> showLoading(registerButton, progressBar)
                    is Result.Error -> showError(registerButton, progressBar, result.error)
                    is Result.Success -> {
                        finishLoading(registerButton, progressBar)
                        Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun setRegisterButtonEnable() {
        val name = nameEditText.text
        val email = emailEditText.text
        val password = passwordEditText.text

        registerButton.isEnabled =
            name != null && name.toString().isNotEmpty() && email != null && email.toString()
                .isNotEmpty() && FormValidator.validateEmail(email.toString()) &&
                    password != null && password.toString()
                .isNotEmpty() && FormValidator.validatePassword(
                passwordEditText.toString())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val emailText = emailEditText.text
        val passwordText = passwordEditText.text
        val nameText = nameEditText.text

        if (!(emailText.isNullOrBlank() || emailText.isNullOrEmpty()))
            outState.putString(LoginActivity.STATE_EMAIL, "$emailText")

        if (!(passwordText.isNullOrBlank() || passwordText.isNullOrEmpty()))
            outState.putString(LoginActivity.STATE_PASSWORD, "$passwordText")

        if (!(nameText.isNullOrBlank() || nameText.isNullOrEmpty()))
            outState.putString(STATE_NAME, "$nameText")
    }

    companion object {
        private const val STATE_NAME = "state_name"
    }
}