package com.ryuunta.musicmedia;

public class Song {
    private String Title;
    private String Singer;
    private int File;
    private int Cover;

    public Song(String title, String singer, int file, int cover) {
        Title = title;
        Singer = singer;
        File = file;
        Cover = cover;
    }

    public Song(String title, int file, int cover) {
        Title = title;
        File = file;
        Cover = cover;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getFile() {
        return File;
    }

    public void setFile(int file) {
        File = file;
    }

    public int getCover() {
        return Cover;
    }

    public void setCover(int cover) {
        Cover = cover;
    }

    public String getSinger() {
        return Singer;
    }

    public void setSinger(String singer) {
        Singer = singer;
    }
}
