package com.cristiangonzalez.integrationapp.activities

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.cristiangonzalez.integrationapp.R
import com.cristiangonzalez.integrationapp.database.entities.UserEntity
import com.cristiangonzalez.integrationapp.databinding.ActivityLoginBinding
import com.cristiangonzalez.integrationapp.ui.UserViewModel
import com.cristiangonzalez.integrationapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginProgressBar.progressBar.bringToFront()

        binding.buttonLogIn.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (isValidEmail(email) && isValidPassword(password)) {
                logIn(email, password)
            } else {
                toast(R.string.login_incorrect_data)
            }
        }

        binding.editTextEmail.validate {
            binding.textInputEmail.error =
                if (isValidEmail(it)) null else getString(R.string.login_invalid_email)
        }

        binding.buttonCreateAccount.setOnClickListener {
            goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }

    private fun logIn(email: String, password: String) {
        showProgressBar()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val user = userViewModel.findUser(email)
                if (user != null) {
                    if (user.password == password) {
                        showMessage("Sesión iniciada correctamente")
                        goToActivity<MainActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        hideProgressBar()
                    } else {
                        showMessage("La contraseña es incorrecta")
                        hideProgressBar()
                    }
                } else {
                    showMessage("El usuario no está registrado")
                    hideProgressBar()
                }
            } catch (e: SQLiteException) {
                showMessage("Ocurrió un error inesperado")
                hideProgressBar()
            }
        }
    }

    private fun showMessage(text: String) {
        toast(text)
    }

    private fun showProgressBar() {
        binding.loginProgressBar.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.loginProgressBar.progressBar.visibility = View.GONE
    }
}