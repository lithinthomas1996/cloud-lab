package com.example.kotlinplayground

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_registration_page.*
import org.json.JSONObject

class RegistrationPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_page)
        btn_reg.setOnClickListener(){


            val firstname: String = editText.text.toString()
            val password: String = editText7.text.toString()
            val email: String = editText4.text.toString()
            val phone: String = editText5.text.toString()
            val lastname: String = editText2.text.toString()
            val street: String = editText3.text.toString()
            val username: String = editText6.text.toString()

            if (firstname == "" || password=="" || email == "" || phone=="" ||
                lastname == "" || street=="" || username == "" )
            {
                Toast.makeText(this,"please input data, cannot be blank",Toast.LENGTH_LONG).show()
            }else{

                Toast.makeText(this, "cb", Toast.LENGTH_LONG).show()

                //val credentials = "postman:password"
                val url = "http://192.168.42.162:8080/api/register/"
                //textView3.text = ""

                // Post parameters
                // Form fields and values
                val params = HashMap<String,String>()
                params["username"] = username
                params["password"] = password
                params["firstname"] = firstname
                params["lastname"] = lastname
                params["dob"] = "15-08-1996"
                params["street"] = street
                params["ph"] = phone
                params["email"] = password
                val jsonObject = JSONObject(params)

                // Volley post request with parameters
                val request = JsonObjectRequest(
                    Request.Method.POST,url,jsonObject,
                    Response.Listener { response ->
                        // Process the json
                        try {
                            textView3.text = "Response: $response"
                        }catch (e:Exception){
                            textView3.text = "Exception: $e"
                        }

                    }, Response.ErrorListener{
                        // Error in request
                        textView3.text = "Volley error: $it"
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
                //if( $response == "suc")


                val intent = Intent(this, MainActivity::class.java)
                // start your next activity
                startActivity(intent)
                Toast.makeText(this,"Sucessfully Registred !",Toast.LENGTH_LONG).show()


            }
        }


    }



}
