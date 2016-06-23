package m.testinguplaod;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    InputStream is=null;
    String result=null;
    String line=null;


    String url = "YOUR API HERE!!";



    private TextView messageText;
    private Button uploadButton, btnselectpic;
    private EditText etxtUpload;
    private ImageView imageview;
    private ProgressDialog dialog = null;
    private JSONObject jsonObject;
    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    String file_path = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        uploadButton = (Button)findViewById(R.id.uploadButton);
        btnselectpic = (Button)findViewById(R.id.button_selectpic);
        messageText  = (TextView)findViewById(R.id.messageText);
        imageview = (ImageView)findViewById(R.id.imageView_pic);
        etxtUpload = (EditText)findViewById(R.id.etxtUpload);

        btnselectpic.setOnClickListener(this);
        uploadButton.setOnClickListener(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading File...");
        dialog.setCancelable(false);

        jsonObject = new JSONObject();


        try
        {
            BufferedReader br=new BufferedReader
                    (new InputStreamReader(is,"iso-8859-8"),8);
            StringBuilder sb=new StringBuilder();

            while((line=br.readLine())!=null)
            {
                sb.append(line+"\n");
            }

            is.close();
            result=sb.toString();
            Log.e("Pass 2", "Success");
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }



    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user\
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE


            );
        }
        else Log.d("no permission","permission granted");
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_selectpic:
                showFileChooser();
                break;
            case R.id.uploadButton:

                dialog = ProgressDialog.show(MainActivity.this, "", "Uploading file...", true);


                Thread thread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {


                        try
                        {

                            File file = new File(
                                    file_path);
                            Log.d("path",file.toString());

                            Log.d("file size", String.valueOf(file.length()));
                            HttpResponse response = null;
                            try {

                                HttpClient httpclient = new DefaultHttpClient();

                                HttpPost httppost = new HttpPost(url);
                                httppost.setHeader("Accept", "application/json");
                                InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), file.length());
                                reqEntity.setContentType("binary/octet-stream");



                                httppost.setEntity(reqEntity);
                                Log.d("upload","starting file upload");
                                response = httpclient.execute(httppost);


                                Log.d("response",response.getStatusLine().toString());
                                dialog.dismiss();




                            }
                            catch (FileNotFoundException e) {

                                e.printStackTrace();
                            }
                            catch (ClientProtocolException e) {


                                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            } catch (IOException e) {

                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });


                thread.start();
                //dialog.dismiss();



//
        }
    }







    private void showFileChooser() {
        verifyStoragePermissions(this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");   //you can give any type of file here!!
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a file"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }


//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();

                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
//
                    File file = new File(uri.getPath());


                    String str = file.toString().replace(":","/");
                    Log.d("new path",str);
                    int indexofprimary = str.indexOf("/primary"); //for internal storage
                    if(indexofprimary != -1)
                    {
                      str=  str.substring(0,indexofprimary)+str.substring(indexofprimary+8);
                        file_path = str.substring(9);

                        File f = new File(Environment.getExternalStorageDirectory(),
                                file_path);
                        file_path=f.toString();


                    }
                    //for external storage..
                    else {
                        str=str.substring(9);

                        File f =new File("/storage",str);
                        file_path = f.toString();
                    }


                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




}
