package com.example.bbusra05d1app;

import static com.example.bbusra05d1app.R.id.btnSelectImage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryActivity extends AppCompatActivity {

    TextInputEditText txtCategoryName, txtDescription;
    Button btnSave, btnSelectImage;
    ProgressBarDialog dialog;
    ImageView imgCategoryImage;
    Bitmap selectedImageBitmap;
    StringBuffer result;

    private static final int IMAGE_PICK_CODE = 1000;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        txtCategoryName = findViewById(R.id.txtCategoryName);
        txtDescription = findViewById(R.id.txtDescription);
        btnSave = findViewById(R.id.btnSave);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imgCategoryImage = findViewById(R.id.imgCategoryImage);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCname = txtCategoryName.getText().toString().trim();
                String strDesc = txtDescription.getText().toString().trim();
                if (TextUtils.isEmpty(strCname)) {
                    txtCategoryName.setError("Required Category Name!");
                    txtCategoryName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(strDesc)) {
                    txtDescription.setError("Required Description!");
                    txtDescription.requestFocus();
                    return;
                }

                ExecutorService service = Executors.newSingleThreadExecutor();
                Handler handler = new Handler();
                service.execute(new Runnable() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog = new ProgressBarDialog(CategoryActivity.this);
                                dialog.setMessage("Creating....");
                                dialog.show();
                            }
                        });

                        try {
                            String url = "http://10.0.2.2/BBUA05D1/store_category.php";

                            // Create httpclient
                            HttpClient client = new DefaultHttpClient();
                            // Create httppost
                            HttpPost post = new HttpPost(url);

                            // Build post params
                            List<NameValuePair> param = new ArrayList<>();
                            param.add(new BasicNameValuePair("CategoryName", strCname));
                            param.add(new BasicNameValuePair("Description", strDesc));

                            // Encode image to base64
                            if (selectedImageBitmap != null) {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                param.add(new BasicNameValuePair("Image", encodedImage));
                            }

                            // Url encode post data
                            post.setEntity(new UrlEncodedFormEntity(param, "UTF-8"));

                            // Finally working http request
                            HttpResponse response = client.execute(post);
                            // Read data sent from the server
                            InputStream is = response.getEntity().getContent();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                            result = new StringBuffer();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                result.append(line + "\n");
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.close();
                                try {
                                    JSONObject object = new JSONObject(result.toString());
                                    if (object.getInt("success") == 1) {
                                        Toast.makeText(CategoryActivity.this, object.getString("msg_success"), Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(CategoryActivity.this, CategoryActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(CategoryActivity.this, object.getString("msg_error"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 1000);
                    }
                });
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    imgCategoryImage.setImageBitmap(selectedImageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
