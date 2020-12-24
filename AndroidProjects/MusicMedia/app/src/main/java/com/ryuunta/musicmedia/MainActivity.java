package com.ryuunta.musicmedia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ImageView imgCover;
    ImageButton ibPlay, ibStop, ibPrev, ibNext, ibList;
    TextView txtTitle, txtTimeCount, txtTimeTotal;
    SeekBar sbSong;

    ArrayList<Song> arraySong;
    int position = 0;
    int REQUEST_CODE_POSITON;
    MediaPlayer mediaPlayer;
    Animation animCover;
    Bitmap bitmap, circularBitmap;
    SharedPreferences savePos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        savePos = getSharedPreferences("songPosition", MODE_PRIVATE);
        position = savePos.getInt("positionsong", 0);
        AnhXa();
        AddSong();
        PlaySong();
    }

    private void SaveSong(){
        SharedPreferences.Editor editor = savePos.edit();
        editor.putInt("positionsong", position);
        editor.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_POSITON && resultCode == RESULT_OK && data != null){
            position = data.getIntExtra("vitri", 0);
            //Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.release();
                CreateMediaPlayer();
                mediaPlayer.start();
            }else {
                CreateMediaPlayer();
            }
            SetTimeTotal();
            UpdateTimeSong();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void PlaySong() {
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListSongMusic.class);
                startActivityForResult(intent, REQUEST_CODE_POSITON);
            }
        });

        CreateMediaPlayer();

        animCover = AnimationUtils.loadAnimation(this, R.anim.anim_cover);

        ibPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()){
                    //neu dang play thi cho pause -> doi hinh buton thanh pause
                    mediaPlayer.pause();
                    ibPlay.setImageResource(R.drawable.play2);
                    imgCover.clearAnimation();
                }else {
                    //dang stop thi play va doi hinh -> pause
                    mediaPlayer.start();
                    ibPlay.setImageResource(R.drawable.pause);
                    imgCover.startAnimation(animCover);
                }
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        ibStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                ibPlay.setImageResource(R.drawable.play2);
                imgCover.clearAnimation();
                CreateMediaPlayer();
            }
        });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if (position > arraySong.size() - 1){
                    position = 0;
                }
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                CreateMediaPlayer();
                mediaPlayer.start();
                imgCover.startAnimation(animCover);
                ibPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        ibPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if (position < 0){
                    position = arraySong.size() - 1;
                }
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                CreateMediaPlayer();
                mediaPlayer.start();
                imgCover.startAnimation(animCover);
                ibPlay.setImageResource(R.drawable.pause);
                SetTimeTotal();
                UpdateTimeSong();
            }
        });

        sbSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(sbSong.getProgress());
            }
        });

    }

    private void UpdateTimeSong(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formatTime = new SimpleDateFormat("mm:ss");
                txtTimeCount.setText(formatTime.format(mediaPlayer.getCurrentPosition()));
                //update seekBar
                sbSong.setProgress(mediaPlayer.getCurrentPosition());
                //check time over -> next song
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        animCover = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_cover);
                        position++;
                        if (position > arraySong.size() - 1){
                            position = 0;
                        }
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.stop();
                        }
                        CreateMediaPlayer();
                        mediaPlayer.start();
                        imgCover.startAnimation(animCover);
                        ibPlay.setImageResource(R.drawable.pause);
                        SetTimeTotal();
                        UpdateTimeSong();
                    }
                });

                handler.postDelayed(this, 500);
            }
        }, 100);
    }

    protected void SetTimeTotal(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(dateFormat.format(mediaPlayer.getDuration()));
        //gán max của skSong = media duration
        sbSong.setMax(mediaPlayer.getDuration());
    }

    private void CreateMediaPlayer(){
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        txtTitle.setText(arraySong.get(position).getTitle());
        bitmap = BitmapFactory.decodeResource(this.getResources(), arraySong.get(position).getCover());
        circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 5000);
        imgCover.setImageBitmap(circularBitmap);
}

    protected void AddSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Ato Hitotsu", "Kobasolo", R.raw.ato_hitotsu_kobasolo, R.drawable.kobasolo_cover));
        arraySong.add(new Song("Chắc ai đó sẽ về", "SontungMTP", R.raw.chac_ai_do_se_ve, R.drawable.sontung_cover));
        arraySong.add(new Song("Greeeen Whiteeeen", "Kobasolo", R.raw.greeeen_whiteeeen, R.drawable.kobasolo_cover));
        arraySong.add(new Song("Kanade", "Takagi-san", R.raw.kanade_takagi, R.drawable.takagi_cover));
        arraySong.add(new Song("Matabaki", "Kobasolo", R.raw.mabataki_harutyaftosamu, R.drawable.harutya_cover));
        arraySong.add(new Song("Pokemon Esmeralda", "Pokemon", R.raw.pokemon_esmeralda, R.drawable.pokemon_cover));
        arraySong.add(new Song("Iwanaikedone", "Takagi-san", R.raw.iwanaikedone, R.drawable.cover_kanade));
        arraySong.add(new Song("Ato Hitotsu", "Kobasolo", R.raw.ato_hitotsu_kobasolo, R.drawable.kobasolo_cover));
        arraySong.add(new Song("Chắc ai đó sẽ về", "SontungMTP", R.raw.chac_ai_do_se_ve, R.drawable.sontung_cover));
        arraySong.add(new Song("Greeeen Whiteeeen", "Kobasolo", R.raw.greeeen_whiteeeen, R.drawable.kobasolo_cover));
        arraySong.add(new Song("Kanade", "Takagi-san", R.raw.kanade_takagi, R.drawable.takagi_cover));
        arraySong.add(new Song("Matabaki", "Kobasolo", R.raw.mabataki_harutyaftosamu, R.drawable.harutya_cover));
        arraySong.add(new Song("Pokemon Esmeralda", "Pokemon", R.raw.pokemon_esmeralda, R.drawable.pokemon_cover));
        arraySong.add(new Song("Iwanaikedone", "Takagi-san", R.raw.iwanaikedone, R.drawable.cover_kanade));

    }

    private void AnhXa() {
        imgCover     = (ImageView) findViewById(R.id.imageViewCover);
        ibPlay       = (ImageButton) findViewById(R.id.imageButtonPlay);
        ibStop       = (ImageButton) findViewById(R.id.imageButtonStop);
        ibPrev       = (ImageButton) findViewById(R.id.imageButtonPrev);
        ibNext       = (ImageButton) findViewById(R.id.imageButtonNext);
        txtTitle     = (TextView) findViewById(R.id.textViewTitle);
        txtTimeCount = (TextView) findViewById(R.id.textViewTimeCount);
        txtTimeTotal = (TextView) findViewById(R.id.textViewTimeTotal);
        sbSong       = (SeekBar) findViewById(R.id.seekBarSong);
        ibList       = (ImageButton) findViewById(R.id.imageButtonList);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SaveSong();
    }
}