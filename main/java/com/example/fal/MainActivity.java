package com.example.fal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private TextView textView;
    private TextView textView2;
    String s="";
    private Intent intent;
    private Button button,button2;
    DBhand db=new DBhand(MainActivity.this);
    private WebView simpleWebView;
    //FirebaseDatabase firebaseDatabase;
    //DatabaseReference databaseReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("check");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        button = (Button)findViewById(R.id.button);
        button2 = (Button)findViewById(R.id.button2);
        //firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseReference = firebaseDatabase.getReference("HAFS Project");
        simpleWebView = (WebView) findViewById(R.id.simpleWebView);
        simpleWebView.setVisibility(simpleWebView.INVISIBLE);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            public void onInit(int status) {
            }
        });
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
            }
            @Override
            public void onBeginningOfSpeech() {
                textView.setText("Listening");
            }
            @Override
            public void onRmsChanged(float rmsdB) {
            }
            @Override
            public void onBufferReceived(byte[] buffer) {
            }
            @Override
            public void onEndOfSpeech() {

            }
            @Override
            public void onError(int error) {
            }
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                String string = "";
                textView.setText("");
                if (matches != null){
                    string = matches.get(0);

                    textView.setText("User said: "+string);
s=string;

                    string.toLowerCase();
                    String[] query = string.split(" ");
                    if(query[0].equals("Natasha"))
                    {
                        string=removeWord(string,"Natasha");
                    }
                    if(string.contains("please tell me something about"))
                    {
                        string=removeWord(string,"please tell me something about");
                    }
                    if(string.contains("tell me something about"))
                    {
                        string=removeWord(string,"tell me something about");
                    }
                    if(string.contains("play")&&(string.contains("song")||string.contains("songs"))) {
                        simpleWebView.setVisibility(simpleWebView.VISIBLE);
                        song(query);
                    }
                    else if(string.contains("play")&&(string.contains("video")||string.contains("videos")))
                    {
                        simpleWebView.setVisibility(simpleWebView.VISIBLE);
                        video(query);
                    }
                    else if(string.contains("who created you") || string.contains("who designed you"))
                    {
                        textToSpeech.speak("Yash Jaiswal designed me for helping you", TextToSpeech.QUEUE_FLUSH, null, null);
                        textView2.setText("Natasha:Yash Jaiswal designed me for helping you");
                    }
                    else if(string.contains("exit")){
                        System.exit(0);
                    }
                    else if(string.contains("what is your name")||string.contains("who are you"))
                    {
                        simpleWebView.setVisibility(simpleWebView.INVISIBLE);
                        textToSpeech.speak("Sir, my name is Natasha one point o. And i am your assistant", TextToSpeech.QUEUE_FLUSH, null, null);
                        textView2.setText("Natasha:Sir, my name is Natasha 1.O. And i am your Assistant");
                    }
                    else if(string.contains("battery")&&string.contains("status"))
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            simpleWebView.setVisibility(simpleWebView.INVISIBLE);
                            batteryLevel();
                        }
                        else{
                            simpleWebView.setVisibility(simpleWebView.INVISIBLE);
                            textView2.setText("Natasha: Please upgrade your android");
                            textToSpeech.speak("Please upgrade your android", TextToSpeech.QUEUE_FLUSH, null, null);

                        }
                    }
                    else if(query[0].equals("hello")||query[0].equals("hi"))
                    {
                        simpleWebView.setVisibility(simpleWebView.INVISIBLE);
                        textToSpeech.speak("Hello Sir, I am your Personal Assistant Natasha one point o.", TextToSpeech.QUEUE_FLUSH, null, null);
                        textView2.setText("Natasha:Hello Sir, I am your Personal Assistant Natasha 1.O.");
                    }
                    else if(string.contains("turn on light") )
                    {
                        turnon();
                        myRef.setValue(1);
                    }
                    else if(string.contains("turn off light") )
                    {
                        myRef.setValue(0);
                        turnoff();
                    }
                    else if(string.contains("open camera"))
                    {
                        camera();
                    }
                    else{
                        simpleWebView.setVisibility(simpleWebView.VISIBLE);
                        textToSpeech.speak("Searching " + string + " on the web", TextToSpeech.QUEUE_FLUSH, null, null);
                        textView2.setText("Natasha:Searching " + string + " on the web");
                        simpleWebView.setWebViewClient(new MyWebViewClient());
                        String url = "https://www.google.com/search?q=" + string.toString();
                        simpleWebView.getSettings().setJavaScriptEnabled(true);
                        simpleWebView.loadUrl(url); // load a web page in a web view
                    }

                }

            }
            @Override
            public void onPartialResults(Bundle partialResults) {
            }
            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v) {

                textToSpeech.speak("What can i do for you sir.", TextToSpeech.QUEUE_FLUSH, null, null);
                textView2.setText("Natasha: What can i do for you sir.");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                speechRecognizer.startListening(intent);
                boolean z=db.addNew(s);
                Log.e("yeee", String.valueOf(z));
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v) {
                boolean z=db.addNew(s);
                Log.e("yeee", String.valueOf(z));
                Intent i=new Intent(MainActivity.this,hist.class);
                startActivity(i);
            }
        });
    }
    void camera()
    {
        {int MY_PERMISSIONS_REQUEST_CAMERA=0;
            textToSpeech.speak("Sure sir. Opening Camera", TextToSpeech.QUEUE_FLUSH, null, null);
            textView2.setText("Natasha:Sure sir. Opening Camera");
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA))
                {

                }
                else
                {
                    textToSpeech.speak("Please give me permission from setting", TextToSpeech.QUEUE_FLUSH, null, null);
                    textView2.setText("Natasha:Please give me Permission from Setting");
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA );
                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            simpleWebView.setVisibility(simpleWebView.INVISIBLE);
            Intent Intent3=new   Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(Intent3);
    }}
    void turnoff()
    {
        textToSpeech.speak("Sure sir. Turning off", TextToSpeech.QUEUE_FLUSH, null, null);
        textView2.setText("Natasha:Sure sir. Turning off.....");
        simpleWebView.setWebViewClient(new MyWebViewClient());
        String url = "http://192.168.43.35/LED=OFF";
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(url);
    }
    void turnon()
    {
        textToSpeech.speak("Sure sir. Turning on", TextToSpeech.QUEUE_FLUSH, null, null);
        textView2.setText("Natasha:Sure sir. Turning on.....");
        simpleWebView.setWebViewClient(new MyWebViewClient());
        String url = "http://192.168.43.35/LED=ON";
        simpleWebView.getSettings().setJavaScriptEnabled(true);
        simpleWebView.loadUrl(url);
    }
    public static String removeWord(String string, String word)
    {
        string=string.replace(word,"");
        return string;
    }
public void song(String[] query){
    int b=0;
    int d=0;
    String c="";
    String e="";
    int z=0;
    for(b=0;b<query.length;b++)
    {
        if(query[b].equals("play"))
        {
            d = b;
        }
        if(query[b].equals("song")||query[b].equals("songs"))
        {
            for(int j=d+1;j<b;j++)
            {
                c = c + query[j]+" ";
            }
        }
    }
    textToSpeech.speak("Sure sir, enjoy with "+c+" song", TextToSpeech.QUEUE_FLUSH, null, null);
    textView2.setText("Natasha: Sure sir, enjoy with "+c+" song");
    simpleWebView.setWebViewClient(new MyWebViewClient());
    String url = "https://www.jiosaavn.com/search/"+c;

    simpleWebView.getSettings().setJavaScriptEnabled(true);
    simpleWebView.loadUrl(url); // load a web page in a web view

}
    private void batteryLevel() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int rawlevel = intent.getIntExtra("level", -1);
                int scale = intent.getIntExtra("scale", -1);
                int level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                textToSpeech.speak("Sir. Your device battery status is "+level+" percentage", TextToSpeech.QUEUE_FLUSH, null, null);
                textView2.setText("Natasha:Sir. Your device battery status is "+level+" percentage");
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

void video(String[] query)
{
    int b=0;
    int d=0;
    String c="";
    String e="";
    int z=0;
    for(b=0;b<query.length;b++)
    {
        if(query[b].equals("play"))
        {
            d = b;
        }
        if(query[b].equals("video")||query[b].equals("videos"))
        {
            for(int j=d+1;j<b;j++)
            {
                c = c + query[j] + " ";
            }
        }
    }
    textToSpeech.speak("Sure sir, enjoy with "+c+" video", TextToSpeech.QUEUE_FLUSH, null, null);
    textView2.setText("Natasha:Sure sir, enjoy with "+c+" video");
    simpleWebView.setWebViewClient(new MyWebViewClient());
    String url = "https://www.youtube.com/results?search_query="+c;

    simpleWebView.getSettings().setJavaScriptEnabled(true);
    simpleWebView.loadUrl(url); // load a web page in a web view

}
}
class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;

    }
}