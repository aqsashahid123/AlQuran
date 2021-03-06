package uberapp.itpvt.com.alquranmyapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
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

import uberapp.itpvt.com.alquranmyapp.Adapters.SurahNameAdapter;
import uberapp.itpvt.com.alquranmyapp.Adapters.SurahNameAdapterListen;
import uberapp.itpvt.com.alquranmyapp.R;

public class SurahNameListen extends AppCompatActivity {
    private String urlJsonObj = "http://api.alquran.cloud/surah";

    Toolbar toolbar;

    TextView tvSurah;
    List<HashMap<String,String>> mapList;
    HashMap<String,String> listItem;
    SurahNameAdapterListen adapter;
    public static TextView tvMessage;
    RecyclerView rv;
    public static ProgressBar progressbar;

    LinearLayoutManager layoutManager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ProgressDialog loading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_name_listen);



        progressbar = (ProgressBar)findViewById(R.id.progress_view);
        mapList = new ArrayList<>();
        listItem = new HashMap<>();
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//
//        toolbar.inflateMenu(R.menu.toolbar_menu);
//        toolbar.setTitle("Al Quran");
//        navigationView = (NavigationView) findViewById(R.id.nav_view);

//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//
//
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//
//                    case R.id.openMenu:
//                        drawerLayout.openDrawer(Gravity.RIGHT);
//                        break;
//
//                }
//
//
//                return true;
//            }
//        });
//
//
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//
//                    case R.id.recitation:
//
//
//                        Intent intent = new Intent(SurahNameListen.this,RecitationActivity.class);
//                        startActivity(intent);
//
//                        break;
//
//                }
//
//
//                return true;
//            }
//        });

        loading = ProgressDialog.show(SurahNameListen.this,"Loading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlJsonObj,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

//                        Toast.makeText(SurahNameListen.this, response, Toast.LENGTH_SHORT).show();
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
                            LinearLayoutManager llm = new LinearLayoutManager(SurahNameListen.this);
                            adapter = new SurahNameAdapterListen(getApplicationContext(),mapList);

                            rv.setAdapter(adapter);
                            rv.setLayoutManager(llm);
                            loading.dismiss();
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
                loading.dismiss();
                Toast.makeText(SurahNameListen.this,"No Inherent",Toast.LENGTH_SHORT).show();

            }
        });
// Add the request to the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);


    }

}
