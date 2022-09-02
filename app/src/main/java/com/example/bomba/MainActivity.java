package com.example.bomba;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bomba.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private List<FacciaDado> positionsTupla;
    private List<String> listTuple;
    private FacciaDado Tick;
    private FacciaDado Tack;
    private FacciaDado TickTack;
    private String valueFacciaSelected;
    private String tuplaSelected;
    private long starttime = 0;
    private int limiteTempo = 0;
    private int MAXTIME = 60;
    private int MINTIME = 7;
    private MediaPlayer ticchettio = new MediaPlayer();
    private MediaPlayer pocoTempo = new MediaPlayer();
    private MediaPlayer boom = new MediaPlayer();

    private boolean pocoTempoAcceso = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.play.setOnClickListener(this);

        ticchettio = MediaPlayer.create(this,R.raw.tic_della_bomba_lento);
        pocoTempo = MediaPlayer.create(this,R.raw.tic_della_bomba);
        boom = MediaPlayer.create(this,R.raw.boom);
        valueFacciaSelected = null;

        this.listTuple = Arrays.asList("TTI","IU","SCI","MAS",
                "OPI","VIO","NSA","MEG","SAI","DA","OCA","CHE","LAI",
                "TRA","INF","RES","STR","DIT","ES","QUI","AIO",
                "PED","ERE","LUC","NG","NER","ELI","OB","CAN","DA",
                "SAR","STI","ELE");


        // TRA, INF, RES, STR, DIT, ES, QUI, AIO
        this.positionsTupla = new ArrayList<>();
        Tick = new FacciaDado("Tick",3);
        TickTack = new FacciaDado("Tick-Tack",1);
        Tack = new FacciaDado("Tack",2);
        this.positionsTupla.add(Tick);
        this.positionsTupla.add(TickTack);
        this.positionsTupla.add(Tack);
    }

    //runs without timer be reposting self
    Handler h2 = new Handler();
    Runnable run = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - starttime;
            int seconds = (int) (millis / 1000);
            Log.d("timer", String.format("secondi = %02d" + " limite = %d", seconds, limiteTempo));
            if(seconds + limiteTempo > MAXTIME ) { // se termina il tempo
                //pocoTempo.setLooping(false);
                pocoTempo.pause();
                h2.removeCallbacks(this);
                Toast.makeText(MainActivity.this, "è SCADUTO IL TEMPO", Toast.LENGTH_SHORT).show();
                binding.play.setVisibility(View.VISIBLE);
                boom.start();
            } else { // altrimenti
                if(seconds + limiteTempo > MAXTIME*0.8) {
                    pocoTempoAcceso = true;
                    ticchettio.pause();
                    pocoTempo.start();
                    pocoTempo.setLooping(true);
                }
                h2.postDelayed(this, 1000);
            }
        }
    };

    @Override
    public void onClick(View view) {
        int idButton = view.getId();
        switch (idButton) {
            case R.id.play: {
                // boom.pause();
                binding.play.setVisibility(View.INVISIBLE);
                Log.d("button","play button");
                Random random = new Random();
                AtomicInteger numGen = new AtomicInteger(random.nextInt(6) + 1);
                Log.d("genPosition",numGen.toString());
                int i = 0;
                boolean fine = false;
                while(i<3 && !fine) {
                    FacciaDado faccia = this.positionsTupla.get(i);
                    numGen.set(numGen.get() - faccia.getProbabilita());
                    Log.d("numGenSottrazione", numGen.toString());
                    if(numGen.get() < 1) { // se il numero è negativo
                        this.valueFacciaSelected = faccia.getValue();
                        this.binding.position.setText(this.valueFacciaSelected);
                        fine = true;
                    }
                    i = i + 1;
                }
                Log.d("genPositionValue",this.valueFacciaSelected);
                numGen.set(random.nextInt(this.listTuple.size()));
                Log.d("genTupla",numGen.toString());
                this.tuplaSelected = this.listTuple.get(numGen.get());
                this.binding.word.setText(this.tuplaSelected);
                Log.d("genTuplaValue",this.tuplaSelected);
                numGen.set(random.nextInt(MAXTIME - MINTIME) + 1);
                limiteTempo = numGen.get();
                starttime = System.currentTimeMillis();
                pocoTempoAcceso = false;
                ticchettio.start();
                ticchettio.setLooping(true);
                h2.postDelayed(run, 0); // faccio partire il timer
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + idButton);
        }
    }
}