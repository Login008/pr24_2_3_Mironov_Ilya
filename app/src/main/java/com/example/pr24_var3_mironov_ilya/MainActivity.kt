package com.example.pr24_var3_mironov_ilya

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    lateinit var login: EditText
    lateinit var pass: EditText
    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        var pref = getPreferences(MODE_PRIVATE)

        if (pref.getBoolean("IsAuthorized", false) == true) {
            val intent = Intent(this, DataActivity::class.java)
            startActivity(intent)
        }

        login=findViewById(R.id.login)
        pass=findViewById(R.id.pass)


    }
    fun reg(view: View) {

        if (!login.text.isNullOrEmpty() && !pass.text.isNullOrEmpty()) {
            pref = getPreferences(MODE_PRIVATE)
            val ed = pref.edit()
            ed.putString("login", login.text.toString())
            ed.putString("password", pass.text.toString())
            ed.apply()
            val snackbar = Snackbar.make(view, "Вы теперь зарегистрированы в системе", Snackbar.LENGTH_LONG)
            snackbar.setActionTextColor(android.graphics.Color.RED)
            snackbar.show()

        } else {
            val snackbar = Snackbar.make(view, "Логин или пароль не заполнены", Snackbar.LENGTH_LONG)
            snackbar.setActionTextColor(android.graphics.Color.RED)
            snackbar.show()
        }
    }

    fun entry(view: View) {
        pref = getPreferences(MODE_PRIVATE)
        if(!login.text.toString().isNullOrEmpty() && !pass.text.toString().isNullOrEmpty())
        {
            if (login.text.toString()==(pref.getString("login",""))&&pass.text.toString()==(pref.getString("password","")))
            {
                pref.edit().putBoolean("IsAuthorized", true).commit()

                val intent = Intent(this, DataActivity::class.java)
                startActivity(intent)
            }
            else {

                var snackbar = Snackbar.make(findViewById(android.R.id.content),"Неверный логин или пароль", Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(android.graphics.Color.RED)
                snackbar.show()
            }

        }
        else
        {
            var snackbar = Snackbar.make(findViewById(android.R.id.content),"Вы не заполнили логин или пароль", Snackbar.LENGTH_LONG)
            snackbar.setActionTextColor(android.graphics.Color.RED)
            snackbar.show()
        }
    }

}