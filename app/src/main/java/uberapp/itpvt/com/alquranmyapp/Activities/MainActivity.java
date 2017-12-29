package uberapp.itpvt.com.alquranmyapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import uberapp.itpvt.com.alquranmyapp.R;
import uberapp.itpvt.com.alquranmyapp.Adapters.SurahNameAdapter;

public class MainActivity extends AppCompatActivity {
    private String urlJsonObj = "http://api.alquran.cloud/surah";

    TextView tvSurah;
    List<HashMap<String,String>> mapList;
    HashMap<String,String> listItem;
    SurahNameAdapter adapter;
   public static TextView tvMessage;
    RecyclerView rv;
    public static ProgressBar progressbar;

    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // tvSurah = (TextView) findViewById(R.id.tvSurah);
        progressbar = (ProgressBar)findViewById(R.id.progress_view);
        mapList = new ArrayList<>();
        listItem = new HashMap<>();
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        tvMessage = (TextView) findViewById(R.id.tvMessage);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlJsonObj,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray data = obj.getJSONArray("data");
                            for (int i =0 ;i< data.length(); i++){

                                JSONObject surahNameData = data.getJSONObject(i);
                                String dataStr = surahNameData.getString("name");
                               listItem = new HashMap<>();
                                // tvSurah.setText(dataStr);
                                listItem.put("englishName",surahNameData.getString("englishName") );
                                listItem.put("name",surahNameData.getString("name"));
                                listItem.put("number", surahNameData.getString("number"));
                                //listItem.put("surahNum")
                                mapList.add(listItem);
                               // listItem.clear();
                            }
                            //layoutManager = new LinearLayoutManager(getApplicationContext());
                            LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
                            adapter = new SurahNameAdapter(getApplicationContext(),mapList);

                            rv.setAdapter(adapter);
                            rv.setLayoutManager(llm);

                        }


                        catch (JSONException e) {
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
