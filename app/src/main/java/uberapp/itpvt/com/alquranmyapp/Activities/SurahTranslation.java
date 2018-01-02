package uberapp.itpvt.com.alquranmyapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uberapp.itpvt.com.alquranmyapp.Adapters.SurahTranslationAdapter;
import uberapp.itpvt.com.alquranmyapp.Network.EndPoints;
import uberapp.itpvt.com.alquranmyapp.R;
import uberapp.itpvt.com.alquranmyapp.SoundActivity;

public class SurahTranslation extends AppCompatActivity {

    String surahNum;
    String url;
    private String downloadAudioPath;
    private String urlDownloadLink = "";
    MediaPlayer mediaPlayer;
    Runnable runnable;
    Handler handler;
    SeekBar seekBar;
    Button stop;
    private ProgressBar progressbar;
    String s_numer;
    List<HashMap<String, String>> mapListAyahTranslation;
    List<HashMap<String, String>> mapListAyah;
    List<HashMap<String, String>> mapNumberList;
    Button btnPlay,resume,pause;

    HashMap<String, String> mapNumerItem;


    //List<HashMap<String,String>> mapListTranslation;

    HashMap<String, String> listItemAyahTranslation;
    HashMap<String, String> listItemAyah;
    MediaPlayer mp;

    RecyclerView rv;
    SurahTranslationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_translation);


        rv = (RecyclerView) findViewById(R.id.recycler_view);

        btnPlay = (Button) findViewById(R.id.btnPlay);

        resume=(Button)findViewById(R.id.play);

        pause=(Button)findViewById(R.id.pause);
        resume.setVisibility(View.GONE);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
//                btnPlay.setVisibility(View.VISIBLE);
                resume.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
            }
        });
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();

                pause.setVisibility(View.VISIBLE);
                btnPlay.setVisibility(View.GONE);
                resume.setVisibility(View.GONE);
            }
        });
        mapNumerItem = new HashMap<>();
        mapNumberList = new ArrayList<>();
        surahNum = getIntent().getStringExtra("ayahNumber");
///////////////SOUND ACTIVITY CODE/////////////////////////
        handler = new Handler();
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        stop=(Button)findViewById(R.id.btnstop);
        stop.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
        downloadAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(downloadAudioPath + File.separator + "Alquran");
        if(!audioVoice.exists()){
            audioVoice.mkdir();
        }


    stop.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mediaPlayer.stop();
            seekBar.setVisibility(View.GONE);
            btnPlay.setVisibility(View.VISIBLE);
            stop.setVisibility(View.GONE);
        }
    });


     /////////////////////////////Finished/////////////////////////

        btnPlay.setOnClickListener(new View.OnClickListener() {
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
                        btnPlay.setVisibility(View.GONE);
                        stop.setVisibility(View.VISIBLE);
                        resume.setVisibility(View.GONE);
                        pause.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(SurahTranslation.this,"Please Download File",Toast.LENGTH_SHORT).show();
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


        //Intent intent = new Intent();

        // intent.getStringExtra("ayahNumber");
        url = EndPoints.BASE_URL_SURAH + "/" + surahNum + "/editions/quran-uthmani,en.asad";
        final ProgressDialog pd = new ProgressDialog(SurahTranslation.this);
        pd.setMessage("loading");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mapListAyahTranslation = new ArrayList<>();
                        mapListAyah = new ArrayList<>();
                        pd.dismiss();
                       // Toast.makeText(SurahTranslation.this, response, Toast.LENGTH_SHORT).show();


                        try {
                            JSONObject obj = new JSONObject(response);
                            //  JSONObject dataObj = obj.getJSONObject("data");
                            JSONArray data = obj.getJSONArray("data");
                            // JSONObject obc = data.getJSONObject();

                            //data.getJSONArray("ayahs");
                            for (int i = 0; i < data.length(); i++) {
                                switch (i) {
                                    case 0:

                                        JSONObject surahData = data.getJSONObject(i);


                                        String surahName = surahData.getString("name");
                                        String surahNum = surahData.getString("number");
                                        if (surahNum.equals("1")){
                                            for (int s = 0; s< surahData.length(); s++){

                                                mapListAyah = new ArrayList<>();
                                                JSONArray surahFatheAyah = surahData.getJSONArray("ayahs");
                                                for (int h =0;h<surahFatheAyah.length();h++){

                                                    JSONObject ayahObj = surahFatheAyah.getJSONObject(h);
                                                    listItemAyah = new HashMap<>();
                                                    mapNumerItem = new HashMap<>();

                                                    mapNumerItem.put("number", ayahObj.getString("number"));

                                                    listItemAyah.put("ayah", ayahObj.getString("text"));
                                                    mapNumberList.add(mapNumerItem);
                                                    mapListAyah.add(listItemAyah);


                                                }
                                            }

                                        }

                                        else {
                                            for (int p = 0; p < surahData.length(); p++) {
                                                mapListAyah = new ArrayList<>();
                                                JSONArray ayah = surahData.getJSONArray("ayahs");

                                                for (int s = 0; s < ayah.length(); s++) {
                                                    JSONObject ayahObj = ayah.getJSONObject(s);
                                                    listItemAyah = new HashMap<>();
                                                    mapNumerItem = new HashMap<>();
                                                    if (s == 0) {
                                                        String f = ayahObj.getString("text");
                                                        String bis = f.substring(0, 38);
                                                        String at = f.substring(38, f.length());
                                                        listItemAyah.put("ayah", bis);
                                                        mapListAyah.add(listItemAyah);
                                                        listItemAyah = new HashMap<>();
                                                        listItemAyah.put("ayah", at);
                                                        mapListAyah.add(listItemAyah);
                                                        mapNumerItem.put("number", ayahObj.getString("number"));
                                                        mapNumberList.add(mapNumerItem);
                                                        mapNumerItem = new HashMap<>();
                                                        mapNumerItem.put("number", ayahObj.getString("number"));
                                                        mapNumberList.add(mapNumerItem);
                                                    } else if (s > 0) {
//                                                listItemAyah.put(String.valueOf(i), ayahObj.getString("text"));
                                                        mapNumerItem.put("number", ayahObj.getString("number"));

                                                        listItemAyah.put("ayah", ayahObj.getString("text"));
                                                        mapNumberList.add(mapNumerItem);
                                                        mapListAyah.add(listItemAyah);
                                                    }

                                                }

                                            }

                                        }

                                        break;
                                    case 1:

                                        JSONObject surahTranslationData = data.getJSONObject(i);
                                  surahNum = surahTranslationData.getString("number");


                                        if (surahNum.equals("1")){
                                            mapListAyahTranslation = new ArrayList<>();
                                            JSONArray ayahTranslationFateha = surahTranslationData.getJSONArray("ayahs");
                                            for (int s = 0; s < ayahTranslationFateha.length(); s++) {
                                                listItemAyahTranslation = new HashMap<>();
                                                JSONObject ayahObj = ayahTranslationFateha.getJSONObject(s);
                                                listItemAyahTranslation.put("translation", ayahObj.getString("text"));
                                                mapListAyahTranslation.add(listItemAyahTranslation);

                                            }
                                        }
                                        else {
                                            for (int p = 0; p < surahTranslationData.length(); p++) {
                                                //  String surahName = surahTranslationData.getString("name");
                                                mapListAyahTranslation = new ArrayList<>();
                                                JSONArray ayahTranslation = surahTranslationData.getJSONArray("ayahs");

                                                for (int s = 0; s < ayahTranslation.length(); s++) {

                                                    listItemAyahTranslation = new HashMap<>();
                                                    JSONObject ayahObj = ayahTranslation.getJSONObject(s);
                                                    if (s == 0) {
                                                        String f = ayahObj.getString("text");
//                                                        String bis = f.substring(0,18);
//                                                        String at = f.substring(18,8);
                                                        listItemAyahTranslation.put("translation", "  ");
                                                        mapListAyahTranslation.add(listItemAyahTranslation);
                                                        listItemAyahTranslation = new HashMap<>();
                                                        // listItemAyahTranslation.clear();
                                                        listItemAyahTranslation.put("translation", f);
                                                        mapListAyahTranslation.add(listItemAyahTranslation);

                                                    }

                                                    // listItemAyahTranslation = new HashMap<>();

//                                                listItemAyahTranslation.put(String.valueOf(i), ayahObj.getString("text"));
                                                    else if (s > 0) {
                                                        listItemAyahTranslation.put("translation", ayahObj.getString("text"));
                                                        mapListAyahTranslation.add(listItemAyahTranslation);

                                                    }


                                                }

                                            }
                                        }
                                        break;
                                }
//                                if (i==1){
//                                 //   JSONArray surahTranslationData = data.getJSONArray(i);
//                                    JSONObject surahTranslationData = data.getJSONObject(i);
//
//                                    mapListAyahTranslation = new ArrayList<>();
//
//                                    for (int p=0 ; p< surahTranslationData.length(); p++){
//                                        String surahName = surahTranslationData.getString("name");
//                                        JSONArray ayahTranslation = surahTranslationData.getJSONArray("ayahs");
//
//                                        for (int s= 0; s< ayahTranslation.length();s++){
//
//                                            JSONObject ayahObj = ayahTranslation.getJSONObject(s);
//
//
//                                            listItemAyahTranslation = new HashMap<>();
//
//                                            listItemAyahTranslation.put(String.valueOf(i),ayahObj.getString("text"));
//                                            mapListAyahTranslation.add(listItemAyahTranslation);
//
//
//                                        }
//
//                                    }
//                                }

//
//                                    }
//


                            }


                            adapter = new SurahTranslationAdapter(SurahTranslation.this, mapListAyah, mapListAyahTranslation, mapNumberList);
                            LinearLayoutManager llm = new LinearLayoutManager(SurahTranslation.this);
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            rv.setLayoutManager(llm);
                            rv.setAdapter(adapter);


                        } catch (JSONException e) {
                            Toast.makeText(SurahTranslation.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        //  tvSurah.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                tvSurah.setText("That didn't work!");
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();

            }
        });
// Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (data != null) {
                mp = new MediaPlayer();
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    Uri uri = data.getData();
                    if (uri != null) {
                        mp.setDataSource(getApplicationContext(), uri);
                        mp.prepare();
                        mp.start();

                        mp.start();
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();
                            }
                        });
                    } else {
                        Toast.makeText(SurahTranslation.this, "Data is null...", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                    Toast.makeText(SurahTranslation.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }
    /////////////////Sound Activity Code///////////////////////////

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
