package it.unimi.mobidev.homearound;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NewAccountActivity extends Activity {
EditText firstname, lastname, email, password, checkpassword;
ProgressBar pb;
ImageButton pick;
private final int SELECT_PHOTO = 1;
private ImageView imageView;
private Bitmap selectedImage; 
private Uri imageUri; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        firstname = (EditText)findViewById(R.id.editText);
        lastname = (EditText)findViewById(R.id.editText2);
        email = (EditText)findViewById(R.id.editText3);
        password = (EditText)findViewById(R.id.editText4);
        checkpassword = (EditText)findViewById(R.id.editText5);
        pb = (ProgressBar)findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        imageView = (ImageView)findViewById(R.id.imageView);
        pick = (ImageButton)findViewById(R.id.pick);
        pick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        selectedImage = BitmapFactory.decodeStream(imageStream);
                        
                        imageView.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel:
                Intent j = new Intent(NewAccountActivity.this, LoginActivity.class);
                NewAccountActivity.this.startActivity(j);
                finish();
                return true;
            case R.id.create:
                CheckData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void CheckData(){
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        if(cd.isConnectingToInternet()){
            if (firstname.getText().toString().length()>0 && lastname.getText().toString().length()>0 &&
                    email.getText().toString().length()>0 && password.getText().toString().length()>0 && checkpassword.getText().toString().length()>0){
                if (password.getText().toString().equals(checkpassword.getText().toString())){
                	
                    ArrayList<NameValuePair> user = new ArrayList<NameValuePair>();
                    user.add(new BasicNameValuePair(getResources().getString(R.string.FIRSTNAME_USER), firstname.getText().toString()));
                    user.add(new BasicNameValuePair(getResources().getString(R.string.LASTNAME_USER), lastname.getText().toString()));
                    user.add(new BasicNameValuePair(getResources().getString(R.string.EMAIL_USER), email.getText().toString()));
                    user.add(new BasicNameValuePair(getResources().getString(R.string.PSW_USER), password.getText().toString()));

                    String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.NEW_ACCOUNT);

                    try {
                        pb.setVisibility(View.VISIBLE);
                        if (selectedImage!=null){
                        	String host = getResources().getString(R.string.HOST) + getResources().getString(R.string.IMAGE_INSERT);
                        	String res2 = new UploadImage().execute(new DataHolders(host, getRealPathFromURI(this, imageUri))).get();
                        	//String res2 = new UploadImage().upload(new DataHolders(host, imageUri.toString()));
                        	user.add(new BasicNameValuePair(getResources().getString(R.string.AVATAR_USER), res2));
                        	
                        }
                        String res = new Parser().execute(new DataHolders(url, user)).get();
                        pb.setVisibility(View.GONE);
                        JSONObject msg = new JSONObject(res);
                        if (!msg.getBoolean("stato")){
                            Toast toast = Toast.makeText(this, getResources().getString(R.string.error_reg_msg), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(this, getResources().getString(R.string.ok_reg_msg), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                            Intent i = new Intent(NewAccountActivity.this, LoginActivity.class);
                            NewAccountActivity.this.startActivity(i);
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast toast = Toast.makeText(this, getResources().getString(R.string.PASSWORD_ERROR), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }

            } else {
                Toast toast = Toast.makeText(this, getResources().getString(R.string.EMPTY_REG_MSG), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(this, getResources().getString(R.string.NO_INTERNET), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
        
       
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
  	  Cursor cursor = null;
  	  try { 
  		  String[] proj = { MediaStore.Images.Media.DATA };
  		  cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
  		  int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
  		  cursor.moveToFirst();
  		  return cursor.getString(column_index);
  	  } finally {
  		  if (cursor != null) {
  			  cursor.close();
  		  }
  	  }
  }
   
   
}
