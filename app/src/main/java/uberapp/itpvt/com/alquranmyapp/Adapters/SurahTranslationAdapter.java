package uberapp.itpvt.com.alquranmyapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import uberapp.itpvt.com.alquranmyapp.R;

/**
 * Created by Mahek on 12/27/2017.
 */

public class SurahTranslationAdapter extends RecyclerView.Adapter<SurahTranslationAdapter.SurahTranslationHolder> {

    Context context;
    List<HashMap<String, String>> mapListAyah;
    List<HashMap<String,String>> mapListTranslation;
    HashMap<String, String> mapItemAyah,mapItemAyahTranslation;


    public SurahTranslationAdapter(Context context, List<HashMap<String, String>> mapListAyah,  List<HashMap<String,String>> mapListTranslation) {


        this.context = context;
        this.mapListAyah = mapListAyah;
        this.mapListTranslation = mapListTranslation;
    }


    @Override
    public SurahTranslationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_ayahs_with_translation, parent, false);

        return new SurahTranslationHolder(itemView);

        //return null;
    }

    @Override
    public void onBindViewHolder(SurahTranslationHolder holder, int position) {
        mapItemAyah = new HashMap<>();
        mapItemAyahTranslation = new HashMap<>();
        mapItemAyah = mapListAyah.get(position);
        mapItemAyahTranslation = mapListTranslation.get(position);
        holder.tvAyah.setText(mapItemAyah.get("ayah"));
        holder.tvAyahTranslation.setText(mapItemAyahTranslation.get("translation"));

      //  holder.tvAyahTranslation.setText(mapItemAyah.get("name"));

//        holder.tvSurahNameArabic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context.getApplicationContext(), SurahTranslation.class);
//                intent.putExtra("ayahNumber", mapItemAyah.get("number"));
//                context.startActivity(intent);
//
//            }
//        });


    }

    @Override
    public int getItemCount() {


        return mapListAyah.size();
    }

    public class SurahTranslationHolder extends RecyclerView.ViewHolder {


        TextView tvAyah, tvAyahTranslation;

        public SurahTranslationHolder(View itemView) {
            super(itemView);

            tvAyah = itemView.findViewById(R.id.ayah);
            tvAyahTranslation = itemView.findViewById(R.id.translation);

        }
    }
}