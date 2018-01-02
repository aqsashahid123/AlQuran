package uberapp.itpvt.com.alquranmyapp.Activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import uberapp.itpvt.com.alquranmyapp.Adapters.SurahNameAdapter;
import uberapp.itpvt.com.alquranmyapp.R;

public class Player extends AppCompatActivity {

    SeekBar seekBar;
    Button stop,play,start,pause,download;
    String surahNum,surahName;
    MediaPlayer mediaPlayer;
    private String downloadAudioPath;
    Runnable runnable;
    private String urlDownloadLink = "";
    Handler handler;
    TextView s_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        surahNum = getIntent().getStringExtra("ayahNumber");
        surahName = getIntent().getStringExtra("surahName");
        handler= new Handler();
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        play=(Button)findViewById(R.id.play);
        pause=(Button)findViewById(R.id.pause);
        start=(Button)findViewById(R.id.start);
        stop=(Button)findViewById(R.id.stop);
        download=(Button)findViewById(R.id.download);
        s_name=(TextView)findViewById(R.id.s_name);
        s_name.setText(surahName);

        downloadAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(downloadAudioPath + File.separator + "Alquran");
        if(!audioVoice.exists()){
            audioVoice.mkdir();
        }

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlDownloadLink = "http://192.168.10.4/alquran/114.mp3";
                urlDownloadLink = "http://192.168.10.4/alquran/" + surahNum + ".mp3";



                if(urlDownloadLink.equals("")){
                    Toast.makeText(Player.this, "Please add audio download link", Toast.LENGTH_LONG).show();
                    return;
                }
                String filename = extractFilename();
                downloadAudioPath = downloadAudioPath + File.separator + "Alquran" + File.separator + filename;
            DownloadFile downloadAudioFile = new DownloadFile();
                downloadAudioFile.execute(urlDownloadLink, downloadAudioPath);
            }
        });

        play.setVisibility(View.GONE);
        seekBar.setEnabled(false);
        stop.setVisibility(View.GONE);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setEnabled(false);
                mediaPlayer.stop();
                start.setVisibility(View.VISIBLE);
                stop.setVisibility(View.GONE);
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();

                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setVisibility(View.VISIBLE);

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {

                    String filename = extractFilename();
//            mediaPlayer.setDataSource( downloadAudioPath + File.separator + "voices" + File.separator + filename);
                    File file = new File( downloadAudioPath + File.separator + "Alquran" + File.separator +surahNum+".mp3");
                    if (file.exists()) {
                        mediaPlayer.setDataSource(downloadAudioPath + File.separator + "Alquran" + File.separator + surahNum + ".mp3");
                        mediaPlayer.prepare();
                        start.setVisibility(View.GONE);
                        stop.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(Player.this,"Please Download File",Toast.LENGTH_SHORT).show();
                        seekBar.setVisibility(View.GONE);
                        // stop.setVisibility(View.GONE);
                    }


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




/////////sound //////
//                if(urlDownloadLink.equals("")){
//                    Toast.makeText(SurahTranslation.this, "Please add audio download link", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                String filename = extractFilename();
//                downloadAudioPath = downloadAudioPath + File.separator + "voices" + File.separator + filename;
//                //      downloadAudioPath = downloadAudioPath + File.separator + "voices" + File.separator + "114.mp3";
//
//                SurahTranslation.DownloadFile downloadAudioFile = new SurahTranslation.DownloadFile();
//                downloadAudioFile.execute(urlDownloadLink, downloadAudioPath);
//////finishe///////

//                Intent intent = new Intent(android.content.Intent.ACTION_GET_CONTENT);
//                Uri data = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Alquran" + File.separator + "114.mp3");
//
//                String type = "audio/mp3";
//                intent.setDataAndType(data, type);
//                startActivityForResult(intent, 1);

            }

        });
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


    //////////////////////////////////DOWNLOAD//////////////////////////////////////////////////
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
    }
}
