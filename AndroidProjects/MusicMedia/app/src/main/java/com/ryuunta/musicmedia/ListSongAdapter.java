package com.ryuunta.musicmedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListSongAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Song> songList;

    public ListSongAdapter(Context context, int layout, List<Song> songList) {
        this.context = context;
        this.layout = layout;
        this.songList = songList;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView txtNameSong, txtSinger;
        ImageView imgHinh;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtNameSong = (TextView) convertView.findViewById(R.id.textViewNameSong);
            holder.txtSinger = (TextView) convertView.findViewById(R.id.textViewSinger);
            holder.imgHinh = (ImageView) convertView.findViewById(R.id.imageViewHinh);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Song song = songList.get(position);
        holder.txtNameSong.setText(song.getTitle());
        holder.txtSinger.setText(song.getSinger());
        holder.imgHinh.setImageResource(song.getCover());
        return convertView;
    }
}
