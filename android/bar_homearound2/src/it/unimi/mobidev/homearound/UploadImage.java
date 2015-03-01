package it.unimi.mobidev.homearound;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import android.os.AsyncTask;
import android.util.Log;



public class UploadImage extends AsyncTask<DataHolders, Void, String>{
	public String result;
    @Override
    protected String doInBackground(DataHolders... params) {

            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            
            HttpURLConnection connection = null;
            DataOutputStream outputStream = null;
            
            File sourceFile = new File(params[0].path); 
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
           
            String imageID = UUID.randomUUID().toString();
            try{
            	FileInputStream fileInputStream = new FileInputStream(sourceFile);

                URL url = new URL(params[0].page);
                connection = (HttpURLConnection) url.openConnection();

                connection.setDoInput(true); //permetti input
                connection.setDoOutput(true); //permetti output
                connection.setUseCaches(false);

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("uploaded_file", imageID); 
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                
                outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=" + imageID +".png" + lineEnd);
                outputStream.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();
                //crea il buffer
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];
                //legge il file 
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                int serverResponseCode = connection.getResponseCode();
                result = connection.getResponseMessage();

                fileInputStream.close();
                outputStream.flush();
                outputStream.close();

            } catch (MalformedURLException ex) {

                Log.v("Upload file to server", "error: " + ex.getMessage(), ex);  
            } catch (Exception e) {
                 
                Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);  
            }

        return imageID;
    }
    @Override
    protected void onPostExecute(String results){
        super.onPostExecute(results);
    }	

}

