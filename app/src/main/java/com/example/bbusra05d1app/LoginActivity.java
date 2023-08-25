package com.example.bbusra05d1app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bbusra05d1app.functions.ProgressBarDialog;
import com.example.bbusra05d1app.functions.Sessions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    EditText txtusername, txtpassword;
    Button btnlogin;
    ProgressBarDialog dialog;

    StringBuffer result;

    Sessions sessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //create Sessions

        sessions = new Sessions(this);
        txtusername = findViewById(R.id.txtUserName);
        txtpassword = findViewById(R.id.txtUserPassword);
        btnlogin = findViewById(R.id.btnLoginUser);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strusername = txtusername.getText().toString().trim();
                String strpassword = txtpassword.getText().toString().trim();
                if (TextUtils.isEmpty(strusername)) {
                    txtusername.setError("Require Username");
                    txtusername.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(strpassword)) {
                    txtpassword.setError("Require Password");
                    txtpassword.requestFocus();
                    return;
                }

                dialog = new ProgressBarDialog(LoginActivity.this);
                dialog.setMessage("Logging in....");
                dialog.show();

                ExecutorService service = Executors.newSingleThreadExecutor();
               // Handler handler = new Handler(Looper.getMainLooper());
                Handler handler = new Handler();
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String strUrl = "http://10.0.2.2/BBUA05D1/login_user.php";

                            // Creating the URL
                            URL url = new URL(strUrl);

                            // Creating HttpURLConnection
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            // Configure the connection
                            conn.setConnectTimeout(15000);
                            conn.setReadTimeout(15000);
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            conn.setRequestMethod("POST");

                            // Building parameters for URL
                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("UserNameLogin", strusername)
                                    .appendQueryParameter("PasswordLogin", strpassword);
                            String Query = builder.build().getEncodedQuery();

                            // Open connection for sending data
                            OutputStream os = conn.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                            writer.write(Query);
                            writer.flush();
                            writer.close();
                            os.close();
                            conn.connect();

                            // Check connection and read data
                            int responseCode = conn.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                InputStream is = conn.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                                result = new StringBuffer();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    result.append(line).append("\n");
                                }
                                reader.close();
                                is.close();
                            } else {
                                // Handle HTTP error response here (if required)
                            }

                            conn.disconnect();

                            // Handle the response on the main thread
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.close();
                                    try {
                                        JSONObject object = new JSONObject(result.toString());
                                        if (object.getInt("success") == 1) {

                                            sessions.SetUserID(object.getInt("UserLoginID"));
                                            sessions.SetUserName(object.getString("UserLoginName"));
                                            sessions.SetUserPassword(object.getString("UserPassword"));
                                            sessions.SetUserFullName(object.getString("UserFullName"));
                                            sessions.SetUserType(object.getString("UserType"));
                                            sessions.SetUserEmail(object.getString("UserEmail"));
                                            sessions.SetUserImage(object.getString("UserImage"));

                                            Toast.makeText(LoginActivity.this, object.getString("msg_success"), Toast.LENGTH_LONG).show();
                                            // Navigate to MainActivity after successful login
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, object.getString("msg_error"), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void GoToSignUp(View view) {
        Intent signup = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(signup);
    }
}