package com.ryuunta.musicmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListSongMusic extends AppCompatActivity {
    ListView lvSong;
    ArrayList<Song> songs;
    ListSongAdapter songAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list_song_music);

        lvSong = (ListView) findViewById(R.id.listViewSong);

        MainActivity mainActivity = new MainActivity();
        mainActivity.AddSong();

        songAdapter = new ListSongAdapter(this, R.layout.list_song, mainActivity.arraySong);
        lvSong.setAdapter(songAdapter);

        lvSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ListSongMusic.this, position + "", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("vitri", position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}