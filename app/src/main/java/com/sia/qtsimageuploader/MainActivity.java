package com.sia.qtsimageuploader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private String imgByteString = null;
    Toast toast;
    Snackbar snackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toast = new Toast(this);
        snackBar = Snackbar.make(findViewById(R.id.clMainActivity), "", Snackbar.LENGTH_LONG);
        Globals.InitSettings(this);
    }

    /// Display the message sent to this function in the snackbar
    private void DisplaySnackbarMessage(String message){
        snackBar.setText(message);
        snackBar.show();
    }

    /// Display the message sent to this function in the snackbar
    private void DisplayToastMessage(String message, int duration){
        Toast toast = Toast.makeText(this, message, duration);
        toast.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            Bitmap pic = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            pic.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            byte[] imgByteArray = bytes.toByteArray();
            imgByteString = Base64.encodeToString(imgByteArray, Base64.DEFAULT);
            //Bitmap bmp = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.length);
            ImageView image = (ImageView) findViewById(R.id.imgPreview);
            //image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(), image.getHeight(), false));
            //image.setImageBitmap(Bitmap.createBitmap(bmp));
            image.setImageBitmap(Bitmap.createBitmap(pic));
            Button btn = findViewById(R.id.btnUpload);
            btn.setEnabled(true);
        }
    }

    public void btnTakePicture_Click(View view) {
        Button btn = findViewById(R.id.btnUpload);
        btn.setEnabled(false);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public void btnUploadPicture_Click(View view) {
        if(imgByteString == null || imgByteString.length() == 0){
            DisplayToastMessage("NO PICTURE TO UPLOAD", Toast.LENGTH_SHORT);
        }
        else{
            DisplayToastMessage("UPLOADING PICTURE",Toast.LENGTH_LONG);
            UploadImageTask uploadImageTask = new UploadImageTask();
            uploadImageTask.execute();
        }
        Button btn = findViewById(R.id.btnUpload);
        btn.setEnabled(false);
        ImageView image = (ImageView) findViewById(R.id.imgPreview);
        image.setImageBitmap(null);
    }

    public void btnSettings_Click(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void cbSQLStatus_Click(View view) {
    }

    private class UploadImageTask extends AsyncTask<String, String, Boolean> {
        private Exception exception;
        private ProgressDialog progressDialog;
        TrustManager[] trustAllCertificates = new TrustManager[] {
            new X509TrustManager()
            {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }
        };

        private HostnameVerifier trustAllHostNames = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };

        protected Boolean doInBackground(String... params) {
            boolean result = true;
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = "http://ccrtoolsweb.ccrnt.ccr/pictureupload/upload";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //DisplayToastMessage("Response is " + response.substring(0,500), Toast.LENGTH_LONG);
                            imgByteString = null;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //DisplayToastMessage("That didnt work", Toast.LENGTH_LONG);
                    imgByteString = null;
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("image", imgByteString);
                    return params;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
            return result;
        }

        /*protected Boolean doInBackground(String... params) {
            boolean result = false;
            if(imgByteString != null){
                HttpURLConnection client = null;
                try {
                    // SSL
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCertificates, new SecureRandom());
                    //HttpURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                    //HttpURLConnection.setDefaultHostnameVerifier(trustAllHostNames);

                    String _url = "http://ccrtoolsweb.ccrnt.ccr/pictureupload/upload";
                    //String _url = Globals._Settings.URLSetting.getValue();
                    //String filename = Globals._Settings.FileNameSetting.getValue() + Instant.now();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("image", imgByteString);
                    URL url = new URL(_url);
                    client =  (HttpURLConnection) url.openConnection();
                    //SetAuthorization(client);
                    client.setRequestMethod("POST");
                    client.setRequestProperty("Content-Type", "application/json; utf-8");
                    client.setDoOutput(true);
                    OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
                    byte[] jsonBytes = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                    outputPost.write(jsonBytes, 0, jsonBytes.length);
                    //outputPost.wait(10000);
                    outputPost.flush();
                    outputPost.close();
                    result = true;
                    String msg = client.getResponseMessage();

                    int code = client.getResponseCode();
                    Log.d("HTTPRESPONSE", msg + code);
                    if(msg != ""){
                        result = false;
                    }
                } catch (MalformedURLException e) {
                    result = false;
                    e.printStackTrace();
                } catch (IOException e) {
                    result = false;
                    e.printStackTrace();
                } catch (JSONException e) {
                    result = false;
                    e.printStackTrace();
                } catch(Exception e){
                    result = false;
                    e.printStackTrace();
                }
                finally {
                    if(client != null) // Make sure the connection is not null.
                        client.disconnect();
                    imgByteString = null;
                }
            }
            return result;
        }*/

        /*private void SetAuthorization(HttpsURLConnection client) {
            String user = "23781";
            String password = "BR030213!123";
            String auth = user + ":" + password;
            byte[] authBytes = auth.getBytes(StandardCharsets.UTF_8);
            byte[] encodedAuth = Base64.encode(authBytes, 0);
            String authHeaderValue = "Windows " + new String(encodedAuth);
            client.setRequestProperty("Authorization", authHeaderValue);
        }*/

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                //DisplayToastMessage("UPLOADED PICTURE TO SERVER", Toast.LENGTH_LONG);
            }else{
                //DisplayToastMessage("UPLOAD FAILED", Toast.LENGTH_LONG);
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);
            TextView tvResult = findViewById(R.id.tvResult);
            tvResult.setText(text[0]);
        }
    }

}

