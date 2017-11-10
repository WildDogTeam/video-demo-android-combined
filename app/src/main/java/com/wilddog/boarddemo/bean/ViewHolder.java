package com.wilddog.boarddemo.bean;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by he on 2017/11/2.
 */

public class ViewHolder {
    ImageView ivAudio;
    ImageView ivBoard;
    public TextView nick;

    public ViewHolder(ImageView ivAudio, ImageView ivBoard, TextView nick) {
        this.ivAudio = ivAudio;
        this.ivBoard = ivBoard;
        this.nick = nick;
    }

    public ViewHolder(ImageView ivAudio, ImageView ivBoard) {
        this.ivAudio = ivAudio;
        this.ivBoard = ivBoard;
    }

    public ViewHolder(TextView nick) {
        this.nick = nick;
    }

    public ImageView getIvAudio() {
        return ivAudio;
    }

    public ImageView getIvBoard() {
        return ivBoard;
    }


}
