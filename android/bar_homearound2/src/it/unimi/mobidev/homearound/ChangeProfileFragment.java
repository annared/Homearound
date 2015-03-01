package it.unimi.mobidev.homearound;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ChangeProfileFragment extends Fragment {
	private ImageView avatar;
	public static final String PREFS_NAME = "HomeAround";
	private ImageButton pick;
	private EditText password, newpassword, confirm;
	private ProgressBar pb;
	private SharedPreferences userData;
	private Button button1;
	private final int SELECT_PHOTO = 1;
	private Bitmap selectedImage; 
	private Uri imageUri; 
	private Bitmap old;
	private Globals g;
	private boolean checkImage = false;
	public ChangeProfileFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_changeprofile, container, false);
		g = ((Globals)getActivity().getApplicationContext());
		userData = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		avatar = (ImageView)rootView.findViewById(R.id.imageView);
		button1 = (Button)rootView.findViewById(R.id.button1);
		pick = (ImageButton)rootView.findViewById(R.id.pick);
		password = (EditText)rootView.findViewById(R.id.editText1);
		newpassword = (EditText)rootView.findViewById(R.id.editText2);
		confirm = (EditText)rootView.findViewById(R.id.editText3);
		pb=(ProgressBar)rootView.findViewById(R.id.progressBar1);
		pb.setVisibility(View.GONE);
		
		avatar.setImageBitmap(g.getAvatar());
		old=avatar.getDrawingCache();
	    button1.setOnClickListener(new OnClickListener() {
        	  public void onClick(View view) {
        	    checkData();
        	  }
        	});
	    
	    pick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
	    
		return rootView;
	}
	
		@Override
	    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
	        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

	        switch(requestCode) {
	            case SELECT_PHOTO:
	                if(resultCode == getActivity().RESULT_OK){
	                    try {
	                        imageUri = imageReturnedIntent.getData();
	                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
	                        selectedImage = BitmapFactory.decodeStream(imageStream);
	                        
	                        avatar.setImageBitmap(selectedImage);
	                    } catch (FileNotFoundException e) {
	                        e.printStackTrace();
	                    }

	                }
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
	private void checkData(){
		boolean check = false;
		JsonObject o = new JsonObject();
		if (password.getText().toString().replace(" ", "").length()==0&&
				newpassword.getText().toString().replace(" ", "").length()==0&&
				confirm.getText().toString().replace(" ", "").length()==0&&old==selectedImage){
			 Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.no_change), Toast.LENGTH_SHORT);
             toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
             toast.show();
		} else {
			if(old!=selectedImage){
				String host = getResources().getString(R.string.HOST) + getResources().getString(R.string.IMAGE_INSERT);
            	try {
					String res2 = new UploadImage().execute(new DataHolders(host, getRealPathFromURI(getActivity().getApplicationContext(), imageUri))).get();
					o.addProperty(getResources().getString(R.string.AVATAR_USER), res2);
					check=true;
					checkImage = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				
			}
			if (newpassword.getText().toString().replace(" ", "").length()>0&&
					confirm.getText().toString().replace(" ", "").length()>0){
				if (newpassword.getText().toString().equals(confirm.getText().toString())){
					if(!password.getText().toString().equals(userData.getString(getResources().getString(R.string.PSW_USER), null))){
						check=false;
						if (password.getText().toString().replace(" ", "").length()==0){
							 Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.error_msg_3), Toast.LENGTH_SHORT);
				             toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
				             toast.show();
						} else {
							 Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.invalid_password), Toast.LENGTH_SHORT);
				             toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
				             toast.show();
						}
					} else {
						check=true;
						o.addProperty(getResources().getString(R.string.new_password), newpassword.getText().toString());
					}
				} else {
					 Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.PASSWORD_ERROR), Toast.LENGTH_SHORT);
		             toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
		             toast.show();
				}
			}
			if (check) {
				Gson gson = new Gson();
	             
	            o.addProperty(getString(R.string.EMAIL_USER), userData.getString(getResources().getString(R.string.EMAIL_USER), null));
	            o.addProperty(getString(R.string.PSW_USER), userData.getString(getResources().getString(R.string.PSW_USER), null));

	            String string = gson.toJson(o);
	            ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
	            data.add(new BasicNameValuePair(getResources().getString(R.string.json), string));
	            String url = getResources().getString(R.string.HOST) + getResources().getString(R.string.PHP_MOD);
	           
	             try {
	                 String response = new Parser().execute(new DataHolders(url, data)).get();
	                 JsonElement msg = new JsonParser().parse(response); 
	                 JsonObject obj = msg.getAsJsonObject();
	                 
	                 if(obj.get("stato").getAsBoolean()){
	                	 if	(checkImage) g.setAvatar(selectedImage);
	                     Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.change), Toast.LENGTH_SHORT);
	                     toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
	                     toast.show();
	                	 getActivity().getSupportFragmentManager().popBackStack();
	                     
	                 } else {
	                     Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.ERROR_INS_MSG), Toast.LENGTH_SHORT);
	                     toast.setGravity(Gravity.CENTER_VERTICAL| Gravity.CENTER_HORIZONTAL, 0, 0);
	                     toast.show();
	                 }

	             } catch (InterruptedException e) {
	                 e.printStackTrace();
	             } catch (ExecutionException e) {
	                 e.printStackTrace();
	             }
			}
		}
		
			
		
	}
	
	
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	    
	    }
	 
	 
}
