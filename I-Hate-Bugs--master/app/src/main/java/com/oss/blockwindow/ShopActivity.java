package com.oss.blockwindow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity {
    private static final int ITEM_EXTRA_LIFE = 55000;
    private static final int ITEM_COIN_X2 = 15000;
    private static final int ITEM_SCORE_X2 = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CoinStack coinStack = new CoinStack(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);

        Button buyButton_X2Coin = findViewById(R.id.buy_X2Coin);
        Button buyButton_X2Score = findViewById(R.id.buy_X2Score);

        buyButton_X2Coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyItem_X2Coin();
            }
        });

        buyButton_X2Score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyItem_X2Score();
            }
        });

        TextView coinTextView = findViewById(R.id.coinTextView);
        coinTextView.setText("Coin  " + coinStack.getCoin());
    }

    // 나머지 코드...

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void buyItem_X2Coin() {
        CoinStack coinStack = new CoinStack(this);
        int remainingCoins = coinStack.getCoin(); // 구매 후 남은 코인

        if (remainingCoins >= ITEM_COIN_X2) {
            // 아이템 구매 가능
            remainingCoins -= ITEM_COIN_X2;

            // GameActivity로 데이터 전달
            Intent intent = new Intent(ShopActivity.this, GameActivity.class);
            Intent intentcoin = new Intent();
            intent.putExtra("whichItem", 1);
            coinStack.setCoin(coinStack.getCoin()+15000);
            startActivity(intent);
        } else {
            // 코인 부족한 경우 처리
            setResult(RESULT_CANCELED);
        }

        finish();
    }

    private void buyItem_X2Score() {
        CoinStack coinStack = new CoinStack(this);
        int remainingCoins = coinStack.getCoin(); // 구매 후 남은 코인

        if (remainingCoins >= ITEM_SCORE_X2) {
            // 아이템 구매 가능
            remainingCoins -= ITEM_SCORE_X2;

            // GameActivity로 데이터 전달
            Intent intent = new Intent(ShopActivity.this, GameActivity.class);
            Intent intentscore = new Intent();
            intent.putExtra("whichItem", 0);
            startActivity(intent);
            coinStack.setCoin(coinStack.getCoin()-5000);
        } else {
            // 코인 부족한 경우 처리
            setResult(RESULT_CANCELED);
        }

        finish();
    }
} //코인 연동