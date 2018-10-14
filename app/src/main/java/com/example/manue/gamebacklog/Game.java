package com.example.manue.gamebacklog;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "game")
public class Game implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "title")
    public String mGameTitle;

    @ColumnInfo (name = "platform")
    public String mGamePlatform;

    @ColumnInfo (name = "notes")
    public String mGameNotes;

    @ColumnInfo (name = "status")
    public String mGameStatus;

    public Game(String mGameTitle, String mGamePlatform, String mGameNotes, String mGameStatus) {
        this.mGameTitle = mGameTitle;
        this.mGamePlatform = mGamePlatform;
        this.mGameNotes = mGameNotes;
        this.mGameStatus = mGameStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGameTitle() {
        return mGameTitle;
    }

    public void setGameTitle(String mGameTitle) {
        this.mGameTitle = mGameTitle;
    }

    public String getGamePlatform() {
        return mGamePlatform;
    }

    public void setGamePlatform(String mGamePlatform) {
        this.mGamePlatform = mGamePlatform;
    }

    public String getGameNotes() {
        return mGameNotes;
    }

    public void setGameNotes(String mGameNotes) {
        this.mGameNotes = mGameNotes;
    }

    public String getGameStatus() {
        return mGameStatus;
    }

    public void setGameStatus(String mGameStatus) {
        this.mGameStatus = mGameStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.mGameTitle);
        parcel.writeString(this.mGamePlatform);
        parcel.writeString(this.mGameNotes);
        parcel.writeString(this.mGameStatus);
    }

    protected Game(Parcel in){
        this.id = in.readLong();
        this.mGameTitle = in.readString();
        this.mGamePlatform = in.readString();
        this.mGameNotes = in.readString();
        this.mGameStatus = in.readString();

    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
