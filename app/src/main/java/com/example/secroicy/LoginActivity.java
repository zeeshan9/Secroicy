package com.example.secroicy;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText emailId, password;
    TextView btnsignup;
    Button btnsignip;
    FirebaseAuth mFirebaseAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.signinemailadress_id);
        password = findViewById(R.id.signinpassword_id);
        btnsignip = findViewById(R.id.btn_signin_id);
        btnsignup = findViewById(R.id.dont_ve_acnt_id);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser =  mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "Auth Token exist!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, LoginActivity.class ));
                }else {
                    Toast.makeText(LoginActivity.this, "Please Login!", Toast.LENGTH_SHORT).show();

                }
            }
        };

//        btnsignip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = emailId.getText().toString();
//
//                String pwd = password.getText().toString();
//                if (email.isEmpty()) {
//                    emailId.setError("Please enter email Id");
//                    emailId.requestFocus();
//                } else if (pwd.isEmpty()) {
//                    password.setError("Please enter your password");
//                    password.requestFocus();
//                }else if (email.isEmpty() && pwd.isEmpty()) {
//                    Toast.makeText(LoginActivity.this, "Fields are emtpy", Toast.LENGTH_SHORT).show();
//                }else if (!(email.isEmpty() && pwd.isEmpty())) {
//                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (!task.isSuccessful()) {
//                                Toast.makeText(LoginActivity.this, "Login Error, Please Login again", Toast.LENGTH_SHORT).show();
//                            }else {
//                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                            }
//                        }
//                    });
//                }
//                else{
//                    Toast.makeText(LoginActivity.this, "Error Occured!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
            }
        });

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth .addAuthStateListener(mAuthStateListener);

    }

    public void LoginRequest(View view){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://192.168.100.10:5000/api/auth";
            JSONObject jsonBody = new JSONObject();

            String email = emailId.getText().toString();
            String pwd = password.getText().toString();

            if (email.isEmpty()) {
                    emailId.setError("Please enter email Id");
                    emailId.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                }else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields are emtpy", Toast.LENGTH_SHORT).show();
                emailId.requestFocus();
                password.requestFocus();
                }else if (!(email.isEmpty() && pwd.isEmpty())) {
                jsonBody.put("email", email);
                jsonBody.put("password", pwd);
            }


            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
//                        Toast.makeText(getApplicationContext(),"Login Successfull",Toast.LENGTH_SHORT).show();
                            Log.i("Login==", response.toString());
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }



//        final TextView textView = (TextView) findViewById(R.id.text);
//// ...
//
//// Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="http://localhost:5000/api/auth";
//
//// Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
////                        textView.setText("Response is: "+ response.substring(0,500));
//                        startActivity(new Intent(LoginActivity.this,LoginActivity.class));
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
//            }
//        });
//
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);

    }


public void testing(View view){
        startActivity(new Intent(getApplicationContext(), HiddenImageList.class));
}

}
