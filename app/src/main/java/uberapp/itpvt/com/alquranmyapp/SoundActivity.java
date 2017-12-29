package uberapp.itpvt.com.alquranmyapp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SoundActivity extends AppCompatActivity {

    Button btnPlay;
    String url ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        btnPlay = (Button) findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                File file = new File("114");
                if(file.exists()){
                    Toast.makeText(getApplicationContext(),"File Exists",Toast.LENGTH_SHORT).show();
                }else{
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






                }




                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();

                } catch (IOException e) {
                    e.printStackTrace();
                }
//                mediaPlayer.prepare(); // might take long! (for buffering, etc)
                mediaPlayer.start();





            }
        });


    }
}
