package com.xazux.flippy_bord.stategame;

import com.xazux._2dlib._2DGameActivityWithStates;

/**
 * Created by josh on 23/01/15.
 */
public class ActivityFlippyBord extends _2DGameActivityWithStates {

    @Override
    public void onInit() {
        RegisterGameState(StateSplash.class);
        RegisterGameState(StateScrollGame.class);
    }
}
