package com.xazux._2dlib;

import android.app.Activity;
import android.os.Vibrator;

import com.xazux._2dlib.sound.JukeBox;
import com.xazux._2dlib.sound.SoundEffectBox;
import com.xazux._2dlib.touch.MainTouchHandle;

public interface IOwner
{
	MainTouchHandle getTouchHandle();
	FontStore getFontStore();
	JukeBox getJukeBox();
	SoundEffectBox getSoundEffectsBox();
	Vibrator getVibrator();
	Activity getActivityContext();
	void Finish();
}
