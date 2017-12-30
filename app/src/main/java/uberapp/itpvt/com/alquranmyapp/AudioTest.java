package uberapp.itpvt.com.alquranmyapp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class AudioTest extends AppCompatActivity {
Button Btnft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_test);
        Btnft=(Button)findViewById(R.id.fatih);
        Btnft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String ur192.168.10.10
                String url = "http://download.quranicaudio.com/quran/abdullaah_3awwaad_al-juhaynee/001.mp3"; // your URL here
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });
    }
}
