package uberapp.itpvt.com.alquranmyapp.Activities;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uberapp.itpvt.com.alquranmyapp.Adapters.SurahTranslationAdapter;
import uberapp.itpvt.com.alquranmyapp.Network.EndPoints;
import uberapp.itpvt.com.alquranmyapp.R;

public class SurahTranslation extends AppCompatActivity {

    String surahNum;
    String url;
    List<HashMap<String, String>> mapListAyahTranslation;
    List<HashMap<String, String>> mapListAyah;
    List<HashMap<String, String>> mapNumberList;
    Button btnPlay;

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
        mapNumerItem = new HashMap<>();
        mapNumberList = new ArrayList<>();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_GET_CONTENT);
                Uri data = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Alquran" + File.separator + "114.mp3");

                String type = "audio/mp3";
                intent.setDataAndType(data, type);
                startActivityForResult(intent, 1);
            }
        });


        //Intent intent = new Intent();
        surahNum = getIntent().getStringExtra("ayahNumber");
        // intent.getStringExtra("ayahNumber");
        url = EndPoints.BASE_URL_SURAH + "/" + surahNum + "/editions/quran-uthmani,en.asad";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mapListAyahTranslation = new ArrayList<>();
                        mapListAyah = new ArrayList<>();
                        Toast.makeText(SurahTranslation.this, response, Toast.LENGTH_SHORT).show();
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


                                        break;
                                    case 1:

                                        JSONObject surahTranslationData = data.getJSONObject(i);


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
                Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_SHORT).show();

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
}
