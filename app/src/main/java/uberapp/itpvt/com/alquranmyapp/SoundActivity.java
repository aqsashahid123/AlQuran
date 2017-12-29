package uberapp.itpvt.com.alquranmyapp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class SoundActivity extends AppCompatActivity {

    Button btnPlay;
    String url ;
    private String downloadAudioPath;
    private String urlDownloadLink = "";
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        btnPlay = (Button) findViewById(R.id.btnPlay);
        downloadAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(downloadAudioPath + File.separator + "Alquran");
        if(!audioVoice.exists()){
            audioVoice.mkdir();
        }


        urlDownloadLink = "http://192.168.10.4/alquran/114.mp3";


        progressbar = (ProgressBar)findViewById(R.id.progress_view);

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {

            String filename = extractFilename();
            mediaPlayer.setDataSource( downloadAudioPath + File.separator + "voices" + File.separator + filename);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
//                mediaPlayer.prepare(); // might take long! (for buffering, etc)
        mediaPlayer.start();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if(urlDownloadLink.equals("")){
                    Toast.makeText(SoundActivity.this, "Please add audio download link", Toast.LENGTH_LONG).show();
                    return;
                }
                String filename = extractFilename();
                downloadAudioPath = downloadAudioPath + File.separator + "voices" + File.separator + filename;
                DownloadFile downloadAudioFile = new DownloadFile();
                downloadAudioFile.execute(urlDownloadLink, downloadAudioPath);
               // audioText.setText("");
            }


//                File file = new File("114");
//                if(file.exists()){
//                    Toast.makeText(getApplicationContext(),"File Exists",Toast.LENGTH_SHORT).show();
//                }else{


//                    url = ;
//                    URL url = "http://localhost/alquran/114.mp3";
//                    url = new URL(url);
//                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.setDoOutput(true);
//
//                    //connect
//                    urlConnection.connect();
//
//
//                    File SDCardRoot = Environment.getExternalStorageDirectory();
//                     file = new File(SDCardRoot,"114");
//
//                    try {
//                        FileOutputStream fileOutput = new FileOutputStream(file);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    InputStream inputStream = urlConnection.getInputStream();
//                    totalSize = urlConnection.getContentLength();
//






               // }








//            }
        });


    }


    private class DownloadFile extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... url) {
            int count;
            try {
                URL urls = new URL(url[0]);
                URLConnection connection = urls.openConnection();
                connection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(urls.openStream());
                OutputStream output = new FileOutputStream(url[1]);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar.setVisibility(ProgressBar.GONE);
        }
    }

    private String extractFilename(){
        if(urlDownloadLink.equals("")){
            return "";
        }
        String newFilename = "";
        if(urlDownloadLink.contains("/")){
            int dotPosition = urlDownloadLink.lastIndexOf("/");
            newFilename = urlDownloadLink.substring(dotPosition + 1, urlDownloadLink.length());
        }
        else{
            newFilename = urlDownloadLink;
        }
        return newFilename;
    }


}
