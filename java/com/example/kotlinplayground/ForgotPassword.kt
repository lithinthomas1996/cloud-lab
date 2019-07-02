package com.example.kotlinplayground

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {
    private var myPreferences = "myAppPreference"
    private var EMPTY = "";
    private var NAME = "name";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)

        btn_recover.setOnClickListener() {
            val username: String = input_username.text.toString()
            val email: String = input_email.text.toString()

            if (email == "" || username == "" )
            {
                Toast.makeText(this,"please input data, cannot be blank",Toast.LENGTH_LONG).show()
            }else {

                //val credentials = "postman:password"
                val url = "http://192.168.137.208:8080/api/frgtpswd/"
                //textView6.text = ""


                // Post parameters
                // Form fields and values
                val params = HashMap<String, String>()
                params["username"] = username
                params["email"] = email
                val jsonObject = JSONObject(params)

                // Volley post request with parameters
                val request = JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    Response.Listener { response ->
                        // Process the json
                        try {
                           val name = response.getString("status")
                            Toast.makeText(this, name, Toast.LENGTH_LONG).show()
                            val token = response.getString("token")
                            if(name == "success")
                            {
                                val editor = sharedPreferences.edit()
                                editor.putString(NAME, token)
                                editor.apply()
                                val Token = sharedPreferences.getString(NAME, EMPTY)
                                Toast.makeText(this, Token, Toast.LENGTH_LONG).show()

                                val intent = Intent(this, PasswordOtp::class.java)
                                // start your next activity
                                startActivity(intent)
                            }
                            else
                            {
                                Toast.makeText(this, "sorry", Toast.LENGTH_LONG).show()

                            }


                        } catch (e: Exception) {
                            Toast.makeText(this, "dgf,$e", Toast.LENGTH_LONG).show()

                        }

                    }, Response.ErrorListener {
                        // Error in request
                        Toast.makeText(this, "gfg,$it", Toast.LENGTH_LONG).show()

                    })

                val socketTimeout = 30000
                // Volley request policy, only one time request to avoid duplicate transaction
                request.retryPolicy = DefaultRetryPolicy(
                    socketTimeout,
                    // 0 means no retry
                    0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                    1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT

                )

                // Add the volley post request to the request queue
                VolleySingleton.getInstance(this).addToRequestQueue(request)


            }
        }
    }
}
