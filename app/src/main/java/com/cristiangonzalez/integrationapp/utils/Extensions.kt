package com.cristiangonzalez.integrationapp.utils

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern

//Generar toast en activity
fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, message, duration).show()

fun Activity.toast(resourceId: Int, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, resourceId, duration).show()

//Ir a Activity
inline fun <reified T : Activity> Activity.goToActivity(noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.init()
    startActivity(intent)
}

//Validar textInput al cambiar texto
fun TextInputEditText.validate(validation: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            validation(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    })
}

//Validar email con pattern
fun isValidEmail(email: String): Boolean {
    val pattern = Patterns.EMAIL_ADDRESS

    return pattern.matcher(email).matches()
}

//Validar contraseña con regex
fun isValidPassword(password: String): Boolean {
    val passwordPattern = "^.{8,}\$"
    val pattern = Pattern.compile(passwordPattern)
    return pattern.matcher(password).matches()
}

//Validar coincidencia de contraseñas
fun isValidConfirmPassword(password: String, confirmPassword: String) = (password == confirmPassword)