package com.example.bbusra05d1app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bbusra05d1app.functions.ProgressBarDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.entity.UrlEncodedFormEntity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText txtfullName, txtuserName, txtpassword, txtconfirmPwd, txtemail;
    Button btnSave;
    ProgressBarDialog dailog;

    StringBuffer result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);

        txtfullName = findViewById(R.id.txtFullNameReg);
        txtuserName = findViewById(R.id.txtUserNameReg);
        txtpassword = findViewById(R.id.txtPasswordReg);
        txtconfirmPwd = findViewById(R.id.txtConfirmPasswordReg);
        txtemail = findViewById(R.id.txtEmailReg);
        btnSave = findViewById(R.id.btnSignUpUser);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strfullName = txtfullName.getText().toString().trim();
                String struserName = txtuserName.getText().toString().trim();
                String strpassword = txtpassword.getText().toString().trim();
                String strconfirm = txtconfirmPwd.getText().toString().trim();
                String stremail = txtemail.getText().toString().trim();
                if(strfullName.isEmpty()){
                    txtfullName.setError("Required Full Name!");
                    txtfullName.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(struserName)){
                    txtuserName.setError("Required UserName!");
                    txtuserName.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(stremail)){
                    txtemail.setError("Email Password!");
                    txtemail.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(strpassword)){
                    txtpassword.setError("Required Password!");
                    txtpassword.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(strconfirm)){
                    txtconfirmPwd.setError("Required Confirm Password!");
                    txtconfirmPwd.requestFocus();
                    return;
                }
                if(strpassword.equals(strconfirm)){
                    // register
                    // Toast.makeText(SignUpActivity.this, "Password does not match!", Toast.LENGTH_LONG).show();
                   /* AlertDialog.Builder ad = new AlertDialog.Builder( SignUpActivity.this);
                    ad.setTitle("Not Match...");
                    ad.setIcon(R.drawable.baseline_error_24);
                    ad.setMessage("Password does not match!");
                    ad.setPositiveButton(  "OK", null);
                    ad.show();*/

                    ExecutorService service = Executors.newSingleThreadExecutor();
                    // Use Executors instead of Executor
                    Handler handler = new Handler();
                    service.execute(new Runnable() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dailog = new ProgressBarDialog(SignUpActivity.this);
                                    dailog.setMessage("Signing Up....");
                                    dailog.show();

                                }
                            });
                            try {
                                String url="http://10.0.2.2/BBUA05D1/register_user.php";

                                //create httpclient
                                HttpClient client = new DefaultHttpClient();
                                //create httppost
                                HttpPost post = new HttpPost(url);

                                //building post param
                                List<NameValuePair> param = new ArrayList<NameValuePair>();
                                param.add(new BasicNameValuePair("FullNameRegister",strfullName));
                                param.add(new BasicNameValuePair("UserNameRegister",struserName));
                                param.add(new BasicNameValuePair("PasswordRegister",strpassword));
                                param.add(new BasicNameValuePair("EmailRegister",stremail));

                                //Url encoding post data
                                post.setEntity(new UrlEncodedFormEntity(param,"UTF-8"));

                                //finally working http request

                                HttpResponse response = client.execute(post);
                                ///read data sent from server
                                InputStream is = response.getEntity().getContent();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                                result = new StringBuffer();
                                String line = "";
                                while ((line = reader.readLine())!=null){
                                    result.append(line + "\n");
                                }

                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                            //on post execute
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Code to be executed after the delay
                                    dailog.close();
                                    try {
                                        JSONObject object = new JSONObject(result.toString());
                                        if (object.getInt("success") == 1) {
                                            Toast.makeText(SignUpActivity.this, object.getString("msg_success"), Toast.LENGTH_LONG).show();
                                            // back to login

                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(SignUpActivity.this, object.getString("msg_error"), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }, 1000);
                            /*runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dailog.close();
                                    try {
                                        JSONObject object = new JSONObject(result.toString());
                                        if (object.getInt("success") == 1) {
                                            Toast.makeText(SignUpActivity.this, object.getString("msg_success"), Toast.LENGTH_LONG).show();
                                            //back to login

                                            Intent intent =new Intent(SignUpActivity.this,LoginActivity.class);

                                             finish();


                                        } else{
                                            Toast.makeText(SignUpActivity.this, object.getString("msg_error"), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });*/
                        }
                    });
                }
                else{
                    // Toast.makeText(SignUpActivity.this,"Password does not match!",Toast.LENGTH_LONG).show();

                    // Passwords do not match
                    AlertDialog.Builder ad = new AlertDialog.Builder(SignUpActivity.this);
                    ad.setTitle("Not Match...");
                    ad.setIcon(R.drawable.baseline_error_24);
                    ad.setMessage("Password does not match!");
                    ad.setPositiveButton("OK", null);
                    ad.show();
                }
            }
        });
    }

}