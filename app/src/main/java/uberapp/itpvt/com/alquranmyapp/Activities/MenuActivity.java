package uberapp.itpvt.com.alquranmyapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import uberapp.itpvt.com.alquranmyapp.Adapters.SearchAdapter;
import uberapp.itpvt.com.alquranmyapp.Adapters.SurahNameAdapter;
import uberapp.itpvt.com.alquranmyapp.R;

public class MenuActivity extends AppCompatActivity {

    LinearLayout lvSearch,lvRead,lvListen;
    TextView tvSearch;
    EditText etSearch;
    RecyclerView rv;
    List<HashMap<String,String>> mapList;
    ArrayList<String> searchData;
    HashMap<String,String> listItem;
    SearchAdapter adapter;
    private String urlJsonObj = "http://api.alquran.cloud/surah";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mapList = new ArrayList<>();
        listItem = new HashMap<>();
        etSearch = (EditText) findViewById(R.id.tvSearch);
        lvSearch = (LinearLayout) findViewById(R.id.lvSearch);
        lvRead = (LinearLayout) findViewById(R.id.lvRead);
        lvListen = (LinearLayout) findViewById(R.id.lvListen);
        searchData = new ArrayList<>();
        lvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                lvListen.setVisibility(View.GONE);
                lvRead.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);


            }
        });


        rv = (RecyclerView) findViewById(R.id.rv);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlJsonObj,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                   //     Toast.makeText(MenuActivity.this, response, Toast.LENGTH_SHORT).show();
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
                                searchData.add(surahNameData.getString("name"));
                                //listItem.put("surahNum")
                                mapList.add(listItem);
                                // listItem.clear();
                            }
                            //layoutManager = new LinearLayoutManager(getApplicationContext());
//                            LinearLayoutManager llm = new LinearLayoutManager(MenuActivity.this);
//                            adapter = new SearchAdapter(getApplicationContext(),mapList,searchData);
//
//                            rv.setAdapter(adapter);
//                            rv.setLayoutManager(llm);

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

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){

//                    lvListen.setVisibility(View.GONE);
//                    lvRead.setVisibility(View.GONE);
//                    rv.setVisibility(View.VISIBLE);


                }
                if (!b){
                    lvListen.setVisibility(View.VISIBLE);
                    lvRead.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);

                }
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                String nameToSearch = etSearch.getText().toString();
                ArrayList<String> filteredLeaves = new ArrayList<String>();

                for (String data : searchData) {
                    if (data.toLowerCase().contains(nameToSearch.toLowerCase()) ) {
                        filteredLeaves.add(data);
                    }


                }
                /*leaveDatas.clear();
                leaveDatas.addAll(filteredLeaves);
                leaves_adapter.notifyDataSetChanged();*/
//                adapter = new SearchAdapter(MenuActivity.this,mapList, filteredLeaves);
//                rv.setAdapter(adapter);
                LinearLayoutManager llm = new LinearLayoutManager(MenuActivity.this);
                adapter = new SearchAdapter(getApplicationContext(),mapList,filteredLeaves);

                rv.setAdapter(adapter);
                rv.setLayoutManager(llm);
                rv.setVisibility(View.VISIBLE);

            }
            @Override
            public void afterTextChanged(Editable s) {


            }
        });      //



    }
}
