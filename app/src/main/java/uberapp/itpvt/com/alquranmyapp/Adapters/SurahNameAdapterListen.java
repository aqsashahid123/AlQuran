package uberapp.itpvt.com.alquranmyapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import uberapp.itpvt.com.alquranmyapp.Activities.MainActivity;
import uberapp.itpvt.com.alquranmyapp.Activities.Player;
import uberapp.itpvt.com.alquranmyapp.Activities.SurahTranslation;
import uberapp.itpvt.com.alquranmyapp.R;

/**
 * Created by Itpvt on 01-Jan-18.
 */

public class SurahNameAdapterListen extends RecyclerView.Adapter<SurahNameAdapterListen.SurahNameHolder>{

        Context context;
        List<HashMap<String,String>> mapList;
        HashMap<String,String> mapItem;
        String urlDownloadLink;
        String downloadAudioPath;


public SurahNameAdapterListen(Context context, List<HashMap<String,String>> mapList){


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
        Intent intent = new Intent(context.getApplicationContext(), Player.class);
        intent.putExtra("ayahNumber", mapItem.get("number") );
        //  context.startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
        }
        });
        holder.tvSurahNameEnglish.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        mapItem = mapList.get(position);
        Intent intent = new Intent(context.getApplicationContext(), Player.class);
        intent.putExtra("ayahNumber", mapItem.get("number") );

        //  context.startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
        }
        });
        holder.ivDownload.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
//                urlDownloadLink = "http://192.168.10.4/alquran/114.mp3";
        urlDownloadLink = "http://192.168.10.4/alquran/" + mapItem.get("number") + ".mp3";


        downloadAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File audioVoice = new File(downloadAudioPath + File.separator + "Alquran");
        if(!audioVoice.exists()){
        audioVoice.mkdir();
        }


        if(urlDownloadLink.equals("")){
        Toast.makeText(context.getApplicationContext(), "Please add audio download link", Toast.LENGTH_LONG).show();
        return;
        }
        String filename = extractFilename();
        downloadAudioPath = downloadAudioPath + File.separator + "Alquran" + File.separator + filename;
    SurahNameAdapterListen.DownloadFile downloadAudioFile = new SurahNameAdapterListen.DownloadFile();
        downloadAudioFile.execute(urlDownloadLink, downloadAudioPath);


        }
        });
//
//        holder.play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

        }

@Override
public int getItemCount() {
        return mapList.size();
        }

public class SurahNameHolder extends RecyclerView.ViewHolder{


    TextView tvSurahNameArabic, tvSurahNameEnglish,tvMessage;
    ImageView ivDownload;
//    SeekBar seekBar;
//    Button play,pause;

    public SurahNameHolder(View itemView) {
        super(itemView);

        //   tvMessage = itemView.findViewById(R.id.tvMessage);
        tvSurahNameArabic = itemView.findViewById(R.id.tvSurahNameArabic);
        tvSurahNameEnglish = itemView.findViewById(R.id.tvSurahNameEnglish);
        ivDownload = itemView.findViewById(R.id.ivDownload);
//        seekBar=itemView.findViewById(R.id.seekBar);
//        play=itemView.findViewById(R.id.start);
//        pause=itemView.findViewById(R.id.stop);
//        seekBar.setVisibility(View.GONE);

    }
}






//////////////////////////////////DOWNLOAD//////////////////////////////////////////////////
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
        MainActivity.progressbar.setVisibility(ProgressBar.VISIBLE);
        MainActivity.tvMessage.setVisibility(TextView.VISIBLE);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        MainActivity.progressbar.setVisibility(ProgressBar.GONE);
        MainActivity.tvMessage.setVisibility(TextView.GONE);
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

    {
    }
}
