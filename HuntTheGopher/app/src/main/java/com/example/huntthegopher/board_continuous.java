package com.example.huntthegopher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class board_continuous extends Activity {
    GameLogic game;
    private Handler bHandler = new Handler() ;

    public static final int SET_COLOR = 0;
    public static final int POST_MESSAGE = 1;
    public static final int GAME_WON = 2;

    private Handler rHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            int what = msg.what ;
            switch (what) {
                case SET_COLOR:
                    String button = "button" + Integer.toString(msg.arg1) + Integer.toString(msg.arg2);
                    Move response = (Move) msg.obj;

                    if(response == Move.Success) {
                        setBtnColor(button, Color.GREEN);
                    }
                    else if(response == Move.NearMiss){
                        setBtnColor(button, Color.rgb(225,0,00));
                    }
                    else if(response == Move.CloseGuess){
                        setBtnColor(button, Color.rgb(220,20,60));
                    }
                    else if(response == Move.CompleteMiss){
                        setBtnColor(button, Color.rgb(240,128,128));
                    }

                    break;
                case POST_MESSAGE:
                    Move response1 = (Move) msg.obj;
                    addToConsole("Red Thread: " + response1.toString() + " (" + msg.arg1 + ", " + msg.arg2 + ").");
                    break;
                case GAME_WON:
                    addToConsole("Red Thread: " + game.getWinner() + " Won!!!");
                    break;

            }

        }
    }	; // Handler is associated with UI Thread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_continuous);

        game = new GameLogic();

        addToConsole("Game has started.");

        Thread blue = new Thread(new BlueThread());
        blue.start();

        Thread red = new Thread(new RedThread());
        red.start();

    }

    public class RedThread implements Runnable {

        @Override
        public void run() {
            //synchronized (game) {
                for(int currX = 0;currX < 10; currX++) {
                    for(int currY = 0; currY < 10; currY++) {
                        try {  Thread.sleep(500); }
                        catch (InterruptedException e) { System.out.println("Thread interrupted!") ; } ;
                        Message msg;
                        synchronized (game) {
                            if (game.getIsWon()) {
                                msg = rHandler.obtainMessage(board_continuous.GAME_WON);
                                rHandler.sendMessage(msg);
                                currX = 10;
                                currY = 10;
                                break;
                            } else {
                                final Move response = game.playMove(currX, currY, Cell.Red);
                                final int finalCurrX = currX;
                                final int finalCurrY = currY;
                                msg = rHandler.obtainMessage(board_continuous.SET_COLOR);
                                msg.obj = response;
                                msg.arg1 = currX;
                                msg.arg2 = currY;
                                rHandler.sendMessage(msg);

                                msg = rHandler.obtainMessage(board_continuous.POST_MESSAGE);
                                msg.obj = response;
                                msg.arg1 = currX;
                                msg.arg2 = currY;
                                rHandler.sendMessage(msg);
                            }
                        }
                    }
                }
            //}
        }
    }

    public class BlueThread implements Runnable {

        @Override
        public void run() {
            //synchronized (game) {
                for(int currY = 0;currY < 10; currY++) {
                    for(int currX = 0; currX < 10; currX++) {
                        try {  Thread.sleep(500); }
                        catch (InterruptedException e) { System.out.println("Thread interrupted!") ; } ;
                        synchronized (game) {
                            if (game.getIsWon()) {
                                bHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        addToConsole("Blue Thread: " + game.getWinner() + " Won!!!");
                                    }
                                });
                                currX = 10;
                                currY = 10;
                                break;
                            } else {
                                final Move response = game.playMove(currX, currY, Cell.Blue);
                                final int finalCurrX = currX;
                                final int finalCurrY = currY;
                                bHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String button = "button" + Integer.toString(finalCurrX) + Integer.toString(finalCurrY);

                                        if (response == Move.Success) {
                                            setBtnColor(button, Color.GREEN);
                                        } else if (response == Move.NearMiss) {
                                            setBtnColor(button, Color.rgb(0, 0, 128));
                                        } else if (response == Move.CloseGuess) {
                                            setBtnColor(button, Color.rgb(0, 0, 225));
                                        } else if (response == Move.CompleteMiss) {
                                            setBtnColor(button, Color.rgb(0, 191, 225));
                                        }

                                        addToConsole("Blue Thread: " + response.toString() + " (" + finalCurrX + ", " + finalCurrY + ").");
                                    }
                                });
                            }
                        }
                    }
                }
            //}
        }
    }

    public void addToConsole(String string) {
        LinearLayout consoleLayout = (LinearLayout) findViewById(R.id.consoleLayout);
        TextView textView = new TextView(this);
        textView.setText(string);
        consoleLayout.addView(textView, 0);
    }
    public void setBtnColor(String button, int color) {
        Resources res = getResources();
        int id = res.getIdentifier(button, "id", "com.example.huntthegopher");
        Button btn = (Button) findViewById(id);
        btn.setBackgroundColor(color);
    }
}