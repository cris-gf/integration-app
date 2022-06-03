package com.cristiangonzalez.integrationapp.activities

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.cristiangonzalez.integrationapp.R
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
        //Mostrar progressBar al frente
        binding.loginProgressBar.progressBar.bringToFront()

        binding.buttonLogIn.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            //Validar credenciales
            if (isValidEmail(email) && isValidPassword(password)) {
                logIn(email, password)
            } else {
                toast(R.string.login_incorrect_data)
            }
        }
        //Validar campo email
        binding.editTextEmail.validate {
            binding.textInputEmail.error = if (isValidEmail(it)) null else getString(R.string.login_invalid_email)
        }
        //Ir a SignUp activity
        binding.buttonCreateAccount.setOnClickListener {
            goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }

    private fun logIn(email: String, password: String) {
        showProgressBar()
        //Acceder a DB de forma asincrona
        CoroutineScope(Dispatchers.Main).launch {
            try {
                //Consultar registro de usuario
                val user = userViewModel.findUser(email)
                if (user != null) {
                    if (user.password == password) {
                        toast(R.string.login_success)
                        //Ir a MainActivity
                        goToActivity<MainActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        hideProgressBar()
                    } else {
                        toast(R.string.login_incorrect_password)
                        hideProgressBar()
                    }
                } else {
                    toast(R.string.login_incorrect_user)
                    hideProgressBar()
                }
            } catch (e: SQLiteException) {
                toast(R.string.error_unexpected)
                hideProgressBar()
            }
        }
    }

    private fun showProgressBar() {
        binding.loginProgressBar.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.loginProgressBar.progressBar.visibility = View.GONE
    }

}