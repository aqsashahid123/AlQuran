package uberapp.itpvt.com.alquranmyapp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
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
    MediaPlayer mediaPlayer;
    Runnable runnable;
    Handler handler;
    SeekBar seekBar;
    Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);


        handler = new Handler();
        btnPlay = (Button) findViewById(R.id.btnPlay);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        downloadAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(downloadAudioPath + File.separator + "Alquran");
        if(!audioVoice.exists()){
            audioVoice.mkdir();
        }


       // urlDownloadLink = "http://192.168.10.4/alquran/114.mp3";


       // progressbar = (ProgressBar)findViewById(R.id.progress_view);

         mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {

            String filename = extractFilename();
//            mediaPlayer.setDataSource( downloadAudioPath + File.separator + "voices" + File.separator + filename);

            mediaPlayer.setDataSource( downloadAudioPath + File.separator + "voices" + File.separator + "114.mp3");

            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
//                mediaPlayer.prepare(); // might take long! (for buffering, etc)

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                playCycle();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (b) {

                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {




            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


   //     mediaPlayer.start();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if(urlDownloadLink.equals("")){
                    Toast.makeText(SoundActivity.this, "Please add audio download link", Toast.LENGTH_LONG).show();
                    return;
                }
                String filename = extractFilename();
               downloadAudioPath = downloadAudioPath + File.separator + "voices" + File.separator + filename;
          //      downloadAudioPath = downloadAudioPath + File.separator + "voices" + File.separator + "114.mp3";

                DownloadFile downloadAudioFile = new DownloadFile();
                downloadAudioFile.execute(urlDownloadLink, downloadAudioPath);

            }



        });


    }
    public void playCycle(){

        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        if (mediaPlayer.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            handler.postDelayed(runnable,1000);

        }




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
