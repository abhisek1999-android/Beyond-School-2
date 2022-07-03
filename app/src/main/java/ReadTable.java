import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;
public class ReadTable {
    TextToSpeech textToSpeech;
    Context context;
    public ReadTable(Context context){
        this.context=context;
    }
    public  void read(String toread){
        textToSpeech=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(new Locale("en","IN"));
                    textToSpeech.setSpeechRate((float) 0.8);
                    textToSpeech.speak(toread,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }
}
