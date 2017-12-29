package uberapp.itpvt.com.alquranmyapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uberapp.itpvt.com.alquranmyapp.Adapters.SurahTranslationAdapter;
import uberapp.itpvt.com.alquranmyapp.Network.EndPoints;
import uberapp.itpvt.com.alquranmyapp.R;

public class SurahTranslation extends AppCompatActivity {

    String surahNum;
    String url;
    List<HashMap<String,String>> mapListAyahTranslation;
    List<HashMap<String,String>> mapListAyah;
    List<HashMap<String,String>> mapNumberList;

    HashMap<String,String> mapNumerItem;

    //List<HashMap<String,String>> mapListTranslation;

    HashMap<String,String> listItemAyahTranslation;
    HashMap<String ,String> listItemAyah;


    RecyclerView rv;
    SurahTranslationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_translation);

        rv = (RecyclerView) findViewById(R.id.recycler_view);


        mapNumerItem = new HashMap<>();
        mapNumberList = new ArrayList<>();


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
                        mapListAyah= new ArrayList<>();
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
                                                    String bis = f.substring(0,38);
                                                    String at = f.substring(38,f.length());
                                                    listItemAyah.put("ayah", bis);
                                                    mapListAyah.add(listItemAyah);
                                                    listItemAyah = new HashMap<>();
                                                    listItemAyah.put("ayah", at);
                                                    mapListAyah.add(listItemAyah);
                                                    mapNumerItem.put("number",ayahObj.getString("number"));
                                                    mapNumberList.add(mapNumerItem);
                                                    mapNumerItem = new HashMap<>();
                                                    mapNumerItem.put("number",ayahObj.getString("number"));
                                                    mapNumberList.add(mapNumerItem);
                                                }


                                                else if (s>0){
//                                                listItemAyah.put(String.valueOf(i), ayahObj.getString("text"));
                                                    mapNumerItem.put("number",ayahObj.getString("number"));

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
                                                else if (s>0){
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
//                                 if (i== 0){
//
//
//
//                                    JSONObject surahData = data.getJSONObject(i);
//
//                                    mapListAyah= new ArrayList<>();
//                                    String surahName = surahData.getString("name");
//                                    for (int p=0 ; p< surahData.length(); p++){
//
//                                        JSONArray ayah = surahData.getJSONArray("ayahs");
//
//                                        for (int s= 0; s< ayah.length();s++){
//
//                                            JSONObject ayahObj = ayah.getJSONObject(s);
//
//
//                                            listItemAyah = new HashMap<>();
//
//                                            listItemAyah.put(String.valueOf(i),ayahObj.getString("text"));
//                                            mapListAyah.add(listItemAyahTranslation);
//
//
//                                        }
//
//                                    }
//


                            }


//
//                                String dataStr = surahTranslationData.getString("name");
//                                listItemAyahTranslation = new HashMap<>();
//                                listItemTranslation = new HashMap<>();
//
//                                // tvSurah.setText(dataStr);
//                                listItem.put("englishName",surahNameData.getString("englishName") );
//                                listItem.put("name",surahNameData.getString("name"));
//                                listItem.put("number", surahNameData.getString("number"));
//                                mapList.add(listItem);
                            // listItem.clear();

                        //layoutManager = new LinearLayoutManager(getApplicationContext());
//                            LinearLayoutManager llm = new LinearLayoutManager(SurahTranslation.this);
//                            adapter = new SurahTranslationAdapter(getApplicationContext(), mapListAyahTranslation);
//
//                            rv.setAdapter(adapter);
//                            rv.setLayoutManager(llm);
                        adapter = new SurahTranslationAdapter(SurahTranslation.this, mapListAyah, mapListAyahTranslation,mapNumberList);
                        LinearLayoutManager llm = new LinearLayoutManager(SurahTranslation.this);
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                            rv.setLayoutManager(llm);
                            rv.setAdapter(adapter);



                    }


                        catch (JSONException e) {
                            Toast.makeText(SurahTranslation.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        //  tvSurah.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                tvSurah.setText("That didn't work!");
                Toast.makeText(getApplicationContext(),String.valueOf(error),Toast.LENGTH_SHORT).show();

            }
        });
// Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);



    }
}
