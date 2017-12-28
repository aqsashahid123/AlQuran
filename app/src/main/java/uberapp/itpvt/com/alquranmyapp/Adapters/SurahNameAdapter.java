package uberapp.itpvt.com.alquranmyapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import uberapp.itpvt.com.alquranmyapp.Activities.SurahTranslation;
import uberapp.itpvt.com.alquranmyapp.R;

/**
 * Created by Mahek on 12/27/2017.
 */

public class SurahNameAdapter extends RecyclerView.Adapter<SurahNameAdapter.SurahNameHolder>{

    Context context;
    List<HashMap<String,String>> mapList;
    HashMap<String,String> mapItem;


    public SurahNameAdapter(Context context, List<HashMap<String,String>> mapList){


        this.context = context;
        this.mapList = mapList;
    }


    @Override
    public SurahNameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_surah_names, parent, false);

        return new SurahNameHolder(itemView);

        //return null;
    }

    @Override
    public void onBindViewHolder(SurahNameHolder holder, final int position) {
         mapItem = new HashMap<>();
        mapItem = mapList.get(position);
        holder.tvSurahNameEnglish.setText(mapItem.get("englishName"));
        holder.tvSurahNameArabic.setText(mapItem.get("name"));

        holder.tvSurahNameArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapItem = mapList.get(position);
                Intent intent = new Intent(context.getApplicationContext(), SurahTranslation.class);
                intent.putExtra("ayahNumber", mapItem.get("number") );
              //  context.startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    public class SurahNameHolder extends RecyclerView.ViewHolder{


    TextView tvSurahNameArabic, tvSurahNameEnglish;

    public SurahNameHolder(View itemView) {
        super(itemView);

        tvSurahNameArabic = itemView.findViewById(R.id.tvSurahNameArabic);
        tvSurahNameEnglish = itemView.findViewById(R.id.tvSurahNameEnglish);

    }
}
}