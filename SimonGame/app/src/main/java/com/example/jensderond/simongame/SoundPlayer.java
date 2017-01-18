package com.example.jensderond.simongame;

/**
 * Created by ruben on 10-1-2017.
 */

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.ArrayList;

public class SoundPlayer implements SoundPool.OnLoadCompleteListener {
    public static final int BLUE_TONE = 0;
    public static final int RED_TONE = 1;
    public static final int GREEN_TONE = 2;
    public static final int YELLOW_TONE = 3;

    private SoundPool mSp;
    private ArrayList<Integer> mTrackList;
    public static Context mContext;

    private AudioManager am;

    private int mSoundsLoadedCount = 0;
    private final int TOTAL_SOUND_COUNT = 4;

    private SoundPlayerLoadCompleteListener mLoadCompleteListener;

    /**
     * Create in class interface for OnAudioLoadComplete
     */
    public interface SoundPlayerLoadCompleteListener {
        public void OnAudioLoadComplete();
    }

    /**
     * This gets the audio focus
     */
    final AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                // do nothing since a missed sound or two isn't the end of the world
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // resume playing, except the sound is probably already over so who cares
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                am.abandonAudioFocus(afChangeListener);
            }
        }
    };

    /**
     * Constructor of soundplayer
     */
    public SoundPlayer() {
        mSp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mTrackList = new ArrayList<Integer>();

        mSp.setOnLoadCompleteListener(this);

        initSoundData();
    }

    /**
     * This function is used to call back
     *
     * @param soundPool
     * @param sampleId
     * @param status
     */
    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        mSoundsLoadedCount++;

        if (mSoundsLoadedCount >= TOTAL_SOUND_COUNT) {
            if (mLoadCompleteListener != null) {
                mLoadCompleteListener.OnAudioLoadComplete();
            }
        }
    }

    /**
     * This function initializes all sounds
     */
    private void initSoundData() {
        if (mContext != null) {
            mTrackList.add(mSp.load(mContext, R.raw.note1, 0));
            mTrackList.add(mSp.load(mContext, R.raw.note2, 0));
            mTrackList.add(mSp.load(mContext, R.raw.note3, 0));
            mTrackList.add(mSp.load(mContext, R.raw.note4, 0));
        }
    }

    /**
     * This function is used to play a sound
     *
     * @param soundId
     */
    public void playSound(int soundId) {
        if (soundId < mTrackList.size()) {
            am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            int result = am.requestAudioFocus(afChangeListener,
                    // use the music stream
                    AudioManager.STREAM_MUSIC,
                    // request permanent focus
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mSp.play(mTrackList.get(soundId), 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }
    }

    /**
     * Sets the onLoadCompleteListener
     *
     * @param listener
     */
    public void setOnLoadCompleteListener(SoundPlayerLoadCompleteListener listener) {
        mLoadCompleteListener = listener;
    }
}