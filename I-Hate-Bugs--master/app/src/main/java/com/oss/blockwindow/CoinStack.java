package com.oss.blockwindow;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class CoinStack extends AppCompatActivity {


    private int CoinAmount;
    private Context context;
    private SharedPreferences sharedPreferences;

    public CoinStack(Context context) {
        this.context = context;
    }

    public int getCoin() {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("coinFile", Context.MODE_PRIVATE);
            return sharedPreferences.getInt("Coin", 0);
        } else {
            return 0; // Context가 null 인 경우 기본값 0 반환
        }
    }

    public void setCoin(int CoinNum) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("coinFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("Coin", CoinNum);
            editor.apply();
        }
    }

}
