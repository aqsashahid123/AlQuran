package uberapp.itpvt.com.alquranmyapp.Adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import uberapp.itpvt.com.alquranmyapp.Network.EndPoints;
import uberapp.itpvt.com.alquranmyapp.R;

/**
 * Created by Mahek on 12/27/2017.
 */

public class SurahTranslationAdapter extends RecyclerView.Adapter<SurahTranslationAdapter.SurahTranslationHolder> {

    Context context;
    List<HashMap<String, String>> mapListAyah;
    List<HashMap<String,String>> mapListTranslation;
    HashMap<String, String> mapItemAyah,mapItemAyahTranslation,mapNumberListItem;
    List<HashMap<String,String>> numberList;

    public SurahTranslationAdapter(Context context, List<HashMap<String, String>> mapListAyah,  List<HashMap<String,String>> mapListTranslation,List<HashMap<String,String>> numberList) {


        this.context = context;
        this.mapListAyah = mapListAyah;
        this.mapListTranslation = mapListTranslation;
        this.numberList =  numberList;

    }


    @Override
    public SurahTranslationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_ayahs_with_translation, parent, false);

        return new SurahTranslationHolder(itemView);

        //return null;
    }

    @Override
    public void onBindViewHolder(SurahTranslationHolder holder, final int position) {
        mapItemAyah = new HashMap<>();
        mapItemAyahTranslation = new HashMap<>();
        mapItemAyah = mapListAyah.get(position);
        mapNumberListItem = new HashMap<>();
        mapItemAyahTranslation = mapListTranslation.get(position);

        holder.tvAyah.setText(mapItemAyah.get("ayah"));
        holder.tvAyahTranslation.setText(mapItemAyahTranslation.get("translation"));

        if (position==0){


            holder.ivPlay.setVisibility(View.GONE);

        }


        holder.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = EndPoints.AUDIO_ALFASAY + mapNumberListItem.get("number") + "/high";

                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();

                } catch (IOException e) {
                    e.printStackTrace();
                }
//                mediaPlayer.prepare(); // might take long! (for buffering, etc)
                mediaPlayer.start();


                mapNumberListItem = new HashMap<>();
                mapNumberListItem = numberList.get(position);
                Toast.makeText(context.getApplicationContext(),mapNumberListItem.get("number"),Toast.LENGTH_SHORT).show();





            }
        });


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
        ImageView ivPlay;


        public SurahTranslationHolder(View itemView) {
            super(itemView);

            tvAyah = itemView.findViewById(R.id.ayah);
            tvAyahTranslation = itemView.findViewById(R.id.translation);
            ivPlay = itemView.findViewById(R.id.ivPlay);

        }
    }
}