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
import kotlinx.android.synthetic.main.activity_reset_password.*
import org.json.JSONObject

class ResetPassword : AppCompatActivity() {
    private var myPreferences = "myAppPreference"
    private var NAME = "name";
    private var EMPTY = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        btn_recover.setOnClickListener() {
            val pass1: String = input_pass1.text.toString()
            val pass2: String = input_pass2.text.toString()


            val sharedPref = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
            val token = sharedPref.getString(NAME, EMPTY)
            Toast.makeText(this, token, Toast.LENGTH_LONG).show()

            if (pass1 == "" || pass2 == "")
            {
                Toast.makeText(this,"please input data, cannot be blank", Toast.LENGTH_LONG).show()
            }else {

                Toast.makeText(this, token, Toast.LENGTH_LONG).show()


                val url = "http://192.168.137.208:8080/api/rstpwd/"
                //textView6.text = ""

                // Post parameters
                // Form fields and values
                val params = HashMap<String, String>()
                params["to"] = token
                params["p1"] = pass1
                params["p2"] = pass2

                val jsonObject = JSONObject(params)

                // Volley post request with parameters
                val request = JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    Response.Listener { response ->
                        // Process the json
                        try {
                            val name = response.getString("status")
                            Toast.makeText(this, name, Toast.LENGTH_LONG).show()

                            if(name == "success")
                            {
                                val editor = sharedPref.edit()
                                editor.putString(NAME, token)
                                editor.clear()

                                val intent = Intent(this, MainActivity::class.java)
                                // start your next activity
                                startActivity(intent)
                            }
                            else
                            {
                                Toast.makeText(this, name, Toast.LENGTH_LONG).show()

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
