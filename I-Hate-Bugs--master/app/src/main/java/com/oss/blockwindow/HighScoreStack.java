package com.oss.blockwindow;

import android.content.Context;
import android.content.SharedPreferences;

public class HighScoreStack {
    private int highscore;
    private Context context;
    private SharedPreferences sharedPreferences;

    public HighScoreStack(Context context) {
        this.context = context;
    }

    public int getHighScore() {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("ScoreFile", Context.MODE_PRIVATE);
            return sharedPreferences.getInt("HighScore", 0);
        } else {
            return 0; // Context가 null 인 경우 기본값 0 반환
        }
    }

    public void setHighscore(int score) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("ScoreFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("HighScore", score);
            editor.apply();
        }
    }
}
