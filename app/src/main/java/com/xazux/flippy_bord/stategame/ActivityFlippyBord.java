package com.xazux.flippy_bord.stategame;

import com.xazux._2dlib._2DGameStateActivity;

/**
 * Created by josh on 23/01/15.
 */
public class ActivityFlippyBord extends _2DGameStateActivity {
    @Override
    public void onInit() {
        RegisterGameState(StateSplash.class);
        RegisterGameState(StateScrollGame.class);
        RegisterTransition(LoadingGame.class, StateScrollGame.class);
    }
}
