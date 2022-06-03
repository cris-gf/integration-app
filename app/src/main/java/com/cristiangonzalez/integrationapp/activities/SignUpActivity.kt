package com.cristiangonzalez.integrationapp.activities

import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.cristiangonzalez.integrationapp.R
import com.cristiangonzalez.integrationapp.database.entities.UserEntity
import com.cristiangonzalez.integrationapp.databinding.ActivitySignUpBinding
import com.cristiangonzalez.integrationapp.ui.UserViewModel
import com.cristiangonzalez.integrationapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Mostrar progressBar al frente
        binding.signUpProgressBar.progressBar.bringToFront()

        binding.buttonSignUp.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val confirmPassword = binding.editTextConfirmPassword.text.toString()
            //Validar credenciales ingresadas
            if (isValidEmail(email) && isValidPassword(password) && isValidConfirmPassword(password, confirmPassword)) {
                signUp(email, password)
                goToActivity<LoginActivity> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else {
                toast(R.string.login_incorrect_data)
            }
        }
        //Regresar a LoginActivity
        binding.buttonGoLogIn.setOnClickListener {
            goToActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        binding.editTextEmail.validate {
            binding.textInputEmail.error = if (isValidEmail(it)) null else getString(R.string.login_invalid_email)
        }

        binding.editTextPassword.validate {
            binding.textInputPassword.error = if (isValidPassword(it)) null else getString(R.string.login_invalid_password)
        }

        binding.editTextConfirmPassword.validate {
            binding.textInputConfirmPassword.error = if (isValidConfirmPassword(binding.editTextPassword.text.toString(), it)) null else getString(R.string.login_invalid_confirm_password)
        }
    }

    private fun signUp(email: String, password: String) {
        showProgressBar()
        //Acceso a db asincrona
        CoroutineScope(Dispatchers.Main).launch {
            try {
                //Registrar usuario
                val user = UserEntity(email = email, password = password)
                userViewModel.insertUser(user)
                showMessage(R.string.login_success_signup)
                hideProgressBar()
            } catch (e: SQLiteException) { //Excepcion campo email unique
                showMessage(R.string.login_email_unique)
                hideProgressBar()
            }
        }
    }

    //Mostrar toast y enviar a LoginActivity
    private fun showMessage(text: Int) {
        toast(text)
        goToActivity<LoginActivity> {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun showProgressBar() {
        binding.signUpProgressBar.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.signUpProgressBar.progressBar.visibility = View.GONE
    }

}