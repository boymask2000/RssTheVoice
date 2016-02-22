package com.mycompany.rss0.com.mycompany.rss0.publish;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Created by gposabella on 1/28/15.
 */
public class TalkPublisher implements Publisher, TextToSpeech.OnInitListener {
    private static TextToSpeech tts;
    private final Context context;

    @Override
    public void publish(String s) {
        parla(s);
    }


    public TalkPublisher(Context context) {
        this.context = context;
        if (tts == null)
            tts = new TextToSpeech(context, this);
    }

    private void parla(String s) {
        while (tts.isSpeaking()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (tts == null) return;
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.ITALY);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("", "lingua non trovata");
                tts = null;
            } else {

            }
        }
    }

    @Override
    public void clean() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

}
