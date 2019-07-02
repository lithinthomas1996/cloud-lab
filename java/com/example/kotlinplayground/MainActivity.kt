package com.example.kotlinplayground

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        link_signup.setOnClickListener {
            // your code to perform when the user clicks on the TextView
            val intent = Intent(this, RegistrationPage::class.java)
            // start your next activity
            startActivity(intent)
        }
        forgot_password.setOnClickListener {
            // your code to perform when the user clicks on the TextView
            val intent = Intent(this, ForgotPassword::class.java)
            // start your next activity
            startActivity(intent)
        }


        btn_login.setOnClickListener() {
            val username: String = input_username.text.toString()
            val password: String = input_password.text.toString()

            //val credentials = "postman:password"
            val url = "http://192.168.48.135:8080/api/logins/"
            //textView6.text = ""

            // Post parameters
            // Form fields and values
            val params = HashMap<String,String>()
            params["username"] = username
            params["password"] = password
            val jsonObject = JSONObject(params)

            // Volley post request with parameters
            val request = JsonObjectRequest(
                Request.Method.POST,url,jsonObject,
                Response.Listener { response ->
                    // Process the json
                    try {
                        val name = response.getString("message")

                        Toast.makeText(this, name,Toast.LENGTH_LONG).show()
                        val intent = Intent(this, AddItem::class.java)
                        // start your next activity
                        startActivity(intent)

                    }catch (e:Exception){
                        Toast.makeText(this,"$e",Toast.LENGTH_LONG).show()
                    }

                }, Response.ErrorListener{
                    // Error in request
                    Toast.makeText(this,"$it",Toast.LENGTH_LONG).show()
                })


            // Volley request policy, only one time request to avoid duplicate transaction
            request.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                // 0 means no retry
                0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

            // Add the volley post request to the request queue
            VolleySingleton.getInstance(this).addToRequestQueue(request)
        }


    }
}

