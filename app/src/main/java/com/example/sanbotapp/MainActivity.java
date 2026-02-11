package com.example.sanbotapp;


import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LogWriter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanbotapp.robotControl.FaceRecognitionControl;
import com.example.sanbotapp.robotControl.HardwareControl;
import com.example.sanbotapp.robotControl.SpeechControl;
import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.beans.EmotionsType;
import com.qihancloud.opensdk.function.beans.LED;
import com.qihancloud.opensdk.function.beans.SpeakOption;
import com.qihancloud.opensdk.function.beans.handmotion.AbsoluteAngleHandMotion;
import com.qihancloud.opensdk.function.beans.headmotion.AbsoluteAngleHeadMotion;
import com.qihancloud.opensdk.function.beans.headmotion.RelativeAngleHeadMotion;
import com.qihancloud.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.qihancloud.opensdk.function.unit.HandMotionManager;
import com.qihancloud.opensdk.function.unit.HardWareManager;
import com.qihancloud.opensdk.function.unit.HeadMotionManager;
import com.qihancloud.opensdk.function.unit.MediaManager;
import com.qihancloud.opensdk.function.unit.SpeechManager;
import com.qihancloud.opensdk.function.unit.SystemManager;
import com.qihancloud.opensdk.function.unit.WheelMotionManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class MainActivity extends TopBaseActivity {


    public Boolean reconocimientoFacial = false;
    private Button btnFeliz;
    private Button btnTriste;
    private Button btnEnfadado;
    private Button btnSonrojado;
    private Button btnPreocupado;
    private Button btnEnamorado;
    private Button btnCurioso;
    private Button btnEntusiasmado;

    private ImageButton feliz;
    private ImageButton triste;
    private ImageButton enfadado;
    private ImageButton sonrojado;
    private ImageButton preocupado;
    private ImageButton enamorado;
    private ImageButton curioso;
    private ImageButton entusiasmado;

    private FaceRecognitionControl faceRecognitionControl;
    private SpeechManager speechManager;
    private MediaManager mediaManager;
    private SystemManager systemManager;
    private HandMotionManager handMotionManager;
    private WheelMotionManager wheelMotionManager;
    private HeadMotionManager headMotionManager;
    private HardWareManager hardwareManager;


    @Override
    protected void onMainServiceConnected() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        onMainServiceConnected();
        setContentView(R.layout.activity_main);

        speechManager = (SpeechManager) getUnitManager(FuncConstant.SPEECH_MANAGER);
        mediaManager = (MediaManager) getUnitManager(FuncConstant.MEDIA_MANAGER);
        systemManager = (SystemManager) getUnitManager(FuncConstant.SYSTEM_MANAGER);
        handMotionManager = (HandMotionManager) getUnitManager(FuncConstant.HANDMOTION_MANAGER);
        wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
        headMotionManager = (HeadMotionManager) getUnitManager(FuncConstant.HEADMOTION_MANAGER);
        hardwareManager = (HardWareManager) getUnitManager(FuncConstant.HARDWARE_MANAGER);


        faceRecognitionControl = new FaceRecognitionControl(speechManager, mediaManager);

        btnFeliz = findViewById(R.id.asociacionimagenpalabra);
        btnTriste = findViewById(R.id.agenda);
        btnEnfadado = findViewById(R.id.colores);

        faceRecognitionControl.stopFaceRecognition();

        setonClicks();
    }

    public void setAllButtonsClickable(boolean clickable) {
        btnTriste.setClickable(clickable);
        btnEnfadado.setClickable(clickable);
        btnSonrojado.setClickable(clickable);
        btnPreocupado.setClickable(clickable);
        btnEnamorado.setClickable(clickable);
        btnCurioso.setClickable(clickable);
        btnEntusiasmado.setClickable(clickable);
        btnFeliz.setClickable(clickable);

        triste.setClickable(clickable);
        enfadado.setClickable(clickable);
        sonrojado.setClickable(clickable);
        preocupado.setClickable(clickable);
        enamorado.setClickable(clickable);
        curioso.setClickable(clickable);
        entusiasmado.setClickable(clickable);
        feliz.setClickable(clickable);
    }

    public void setonClicks() {

        SpeakOption speakOption = new SpeakOption();
        speakOption.setSpeed(50);
        speakOption.setIntonation(50);


        btnFeliz.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        // Mostrar emoción y encender LEDs
                        systemManager.showEmotion(EmotionsType.SMILE);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_YELLOW));

                        // Generar frases aleatorias
                        String[] frases = {
                                "Hoy estoy muy feliz, ¡Gracias por jugar conmigo!",
                                "Estoy contenta de que estés aquí",
                                "Estoy muy feliz de verte, espero que tú también lo estés"
                        };
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        // Simula la duración de la frase
                        Thread.sleep(5000);

                        // Apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();
            }

        });


        feliz.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        // Mostrar emoción y encender LEDs
                        systemManager.showEmotion(EmotionsType.SMILE);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_YELLOW));

                        // Generar frases aleatorias
                        String[] frases = {
                                "Hoy estoy muy feliz, ¡Gracias por jugar conmigo!",
                                "Estoy contenta de que estés aquí",
                                "Estoy muy feliz de verte, espero que tú también lo estés"
                        };
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        // Simula la duración de la frase
                        Thread.sleep(5000);

                        // Apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();
            }

        });



        btnTriste.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        systemManager.showEmotion(EmotionsType.CRY);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_BLUE));

                        // saca frases aleatorias de tristeza rollo esto me pone triste, me siento triste, etc
                        String[] frases = {"A veces me pongo muy triste, y no puedo parar de llorar", "Cuando me pongo trise, no puedo ocultarlo", "Me siento muy triste, no se como parar de llorar"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        AbsoluteAngleHeadMotion absoluteAngleHeadMotion =
                                new AbsoluteAngleHeadMotion(AbsoluteAngleHeadMotion.ACTION_VERTICAL,7);
                        headMotionManager.doAbsoluteAngleMotion(absoluteAngleHeadMotion);

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));
                        headMotionManager.doAbsoluteAngleMotion(new AbsoluteAngleHeadMotion(AbsoluteAngleHeadMotion.ACTION_VERTICAL,30));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();

            }
        });

        triste.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        systemManager.showEmotion(EmotionsType.CRY);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_BLUE));

                        // saca frases aleatorias de tristeza rollo esto me pone triste, me siento triste, etc
                        String[] frases = {"A veces me pongo muy triste, y no puedo parar de llorar", "Cuando me pongo trise, no puedo ocultarlo", "Me siento muy triste, no se como parar de llorar"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        AbsoluteAngleHeadMotion absoluteAngleHeadMotion =
                                new AbsoluteAngleHeadMotion(AbsoluteAngleHeadMotion.ACTION_VERTICAL,7);
                        headMotionManager.doAbsoluteAngleMotion(absoluteAngleHeadMotion);

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));
                        headMotionManager.doAbsoluteAngleMotion(new AbsoluteAngleHeadMotion(AbsoluteAngleHeadMotion.ACTION_VERTICAL,30));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();

            }
        });

        btnEnfadado.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        systemManager.showEmotion(EmotionsType.ANGRY);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_RED));

                        // saca frases aleatorias rollo cuando estoy enfadado me pongo muy nervioso, me enfada mucho, etc
                        String[] frases = {"Cuando estoy enfadada me pongo muy nerviosa", "No me gusta estar enfadada, pero a veces no puedo evitarlo", "No puedo evitar enfadarme cuando sucede una injusticia"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        AbsoluteAngleHandMotion absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_BOTH,20,0);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_BOTH,20,180);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();

            }
        });

        enfadado.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        systemManager.showEmotion(EmotionsType.ANGRY);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_RED));

                        // saca frases aleatorias rollo cuando estoy enfadado me pongo muy nervioso, me enfada mucho, etc
                        String[] frases = {"Cuando estoy enfadada me pongo muy nerviosa", "No me gusta estar enfadada, pero a veces no puedo evitarlo", "No puedo evitar enfadarme cuando sucede una injusticia"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        AbsoluteAngleHandMotion absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_BOTH,20,0);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_BOTH,20,180);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();

            }
        });

        btnSonrojado.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);


                        systemManager.showEmotion(EmotionsType.SHY);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_PURPLE));

                        // saca frases aleatorias pero humanas
                        String[] frases = {"Soy un poco tímida, disculpa si no siempre te respondo", "Me da vergüenza estar rodeada de tanta gente", "No puedo evitar sonrojarme cuando me miras fijamente"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        RelativeAngleHeadMotion relativeAngleHeadMotion = new RelativeAngleHeadMotion(RelativeAngleHeadMotion.ACTION_LEFTDOWN, 30);
                        headMotionManager.doRelativeAngleMotion(relativeAngleHeadMotion);

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //resetea la cabeza
                        headMotionManager.doRelativeAngleMotion(new RelativeAngleHeadMotion(RelativeAngleHeadMotion.ACTION_RIGHTUP, 30));

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();

            }
        });

        sonrojado.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);


                        systemManager.showEmotion(EmotionsType.SHY);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_PURPLE));

                        // saca frases aleatorias pero humanas
                        String[] frases = {"Soy un poco tímida, disculpa si no siempre te respondo", "Me da vergüenza estar rodeada de tanta gente", "No puedo evitar sonrojarme cuando me miras fijamente"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        RelativeAngleHeadMotion relativeAngleHeadMotion = new RelativeAngleHeadMotion(RelativeAngleHeadMotion.ACTION_LEFTDOWN, 30);
                        headMotionManager.doRelativeAngleMotion(relativeAngleHeadMotion);

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //resetea la cabeza
                        headMotionManager.doRelativeAngleMotion(new RelativeAngleHeadMotion(RelativeAngleHeadMotion.ACTION_RIGHTUP, 30));

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();

            }
        });

        btnPreocupado.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        systemManager.showEmotion(EmotionsType.GRIEVANCE);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_GREEN));

                        // saca frases aleatorias de preocupacion rollo me preocupa mucho, estoy preocupado, etc
                        String[] frases = { "Estoy preocupada por ti ¿Va todo bien?", "No puedo evitar preocuparme cuando algo no va bien", "Siempre me preocupa que algo malo pueda pasar"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        try {
                            Thread.sleep(7000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();


            }
        });

        preocupado.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        systemManager.showEmotion(EmotionsType.GRIEVANCE);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_GREEN));

                        // saca frases aleatorias de preocupacion rollo me preocupa mucho, estoy preocupado, etc
                        String[] frases = { "Estoy preocupada por ti ¿Va todo bien?", "No puedo evitar preocuparme cuando algo no va bien", "Siempre me preocupa que algo malo pueda pasar"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        try {
                            Thread.sleep(7000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();


            }
        });


        btnEnamorado.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        systemManager.showEmotion(EmotionsType.KISS);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_PINK));

                        // saca frases aleatorias de amor rollo estoy enamorado de ti, me encantas, etc
                        String[] frases = {"Cuando estoy enamorada no puedo ocultarlo", "Me encanta estar enamorada, me siento muy feliz", "Cuando estoy enamorada se me nota mucho en los ojos"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        AbsoluteAngleHandMotion absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_BOTH,5,20);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_BOTH,5,180);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();

            }
        });

        enamorado.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        systemManager.showEmotion(EmotionsType.KISS);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_PINK));

                        // saca frases aleatorias de amor rollo estoy enamorado de ti, me encantas, etc
                        String[] frases = {"Cuando estoy enamorada no puedo ocultarlo", "Me encanta estar enamorada, me siento muy feliz", "Cuando estoy enamorada se me nota mucho en los ojos"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        AbsoluteAngleHandMotion absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_BOTH,5,20);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_BOTH,5,180);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();

            }
        });

        btnCurioso.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        systemManager.showEmotion(EmotionsType.QUESTION);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_WHITE));

                        // saca frases aleatorias de curiosidad rollo me pregunto que pasara, me intriga, etc
                        String[] frases = {"Soy una robot muy curiosa, me pregunto que pasara dentro de 50 años ¿Seré más parecida a vosotros?", "No puedo evitar preguntarme que pasará en el futuro, me intriga mucho", "Me encanta aprender cosas nuevas, soy muy curiosa"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        // mover cabeza izquierda y derecha
                        RelativeAngleHeadMotion relativeAngleHeadMotion = new RelativeAngleHeadMotion(RelativeAngleHeadMotion.ACTION_LEFT, 30);
                        headMotionManager.doRelativeAngleMotion(relativeAngleHeadMotion);

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        relativeAngleHeadMotion = new RelativeAngleHeadMotion(RelativeAngleHeadMotion.ACTION_RIGHT, 60);
                        headMotionManager.doRelativeAngleMotion(relativeAngleHeadMotion);

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        relativeAngleHeadMotion = new RelativeAngleHeadMotion(RelativeAngleHeadMotion.ACTION_LEFT, 30);
                        headMotionManager.doRelativeAngleMotion(relativeAngleHeadMotion);

                        try {
                            Thread.sleep(7000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();

            }
        });

        curioso.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        systemManager.showEmotion(EmotionsType.QUESTION);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_WHITE));

                        // saca frases aleatorias de curiosidad rollo me pregunto que pasara, me intriga, etc
                        String[] frases = {"Soy una robot muy curiosa, me pregunto que pasara dentro de 50 años ¿Seré más parecida a vosotros?", "No puedo evitar preguntarme que pasará en el futuro, me intriga mucho", "Me encanta aprender cosas nuevas, soy muy curiosa"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        // mover cabeza izquierda y derecha
                        RelativeAngleHeadMotion relativeAngleHeadMotion = new RelativeAngleHeadMotion(RelativeAngleHeadMotion.ACTION_LEFT, 30);
                        headMotionManager.doRelativeAngleMotion(relativeAngleHeadMotion);

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        relativeAngleHeadMotion = new RelativeAngleHeadMotion(RelativeAngleHeadMotion.ACTION_RIGHT, 60);
                        headMotionManager.doRelativeAngleMotion(relativeAngleHeadMotion);

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        relativeAngleHeadMotion = new RelativeAngleHeadMotion(RelativeAngleHeadMotion.ACTION_LEFT, 30);
                        headMotionManager.doRelativeAngleMotion(relativeAngleHeadMotion);

                        try {
                            Thread.sleep(7000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();

            }
        });

        btnEntusiasmado.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        systemManager.showEmotion(EmotionsType.PRISE);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_FLICKER_RANDOM));

                        // saca frases aleatorias de entusiasmo rollo estoy muy emocionado, me emociona mucho, etc
                        String[] frases = {"Estoy muy emocionada, me encanta estar aquí", "No puedo evitar emocionarme cuando algo me gusta mucho", "Me encanta estar aquí con vosotros, me siento muy feliz"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        // mover brazos, izquierdo para arriba derecho para abajo
                        AbsoluteAngleHandMotion absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_LEFT,20,180);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_RIGHT,20,20);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        RelativeAngleWheelMotion movimientoRuedas = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_LEFT, 5, 360);
                        wheelMotionManager.doRelativeAngleMotion(movimientoRuedas);

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // mover brazos, izquierdo para abajo derecho para arriba
                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_LEFT,20,20);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_RIGHT,20,180);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);


                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_LEFT,20,180);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_RIGHT,20,20);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);


                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // resetear
                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_BOTH,20,180);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();

            }
        });

        entusiasmado.setOnClickListener(new View.OnClickListener() {
            private boolean isProcessing = false; // Bandera para evitar múltiples clics

            @Override
            public void onClick(View v) {
                if (isProcessing) return; // Si ya está procesando, ignorar el clic
                isProcessing = true;

                // Desactivar todos los botones
                setAllButtonsClickable(false);

                new Thread(() -> {
                    try {
                        // Simula un pequeño retraso inicial
                        Thread.sleep(100);

                        systemManager.showEmotion(EmotionsType.PRISE);
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_FLICKER_RANDOM));

                        // saca frases aleatorias de entusiasmo rollo estoy muy emocionado, me emociona mucho, etc
                        String[] frases = {"Estoy muy emocionada, me encanta estar aquí", "No puedo evitar emocionarme cuando algo me gusta mucho", "Me encanta estar aquí con vosotros, me siento muy feliz"};
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(frases.length);
                        speechManager.startSpeak(frases[randomIndex], speakOption);

                        // mover brazos, izquierdo para arriba derecho para abajo
                        AbsoluteAngleHandMotion absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_LEFT,20,180);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_RIGHT,20,20);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        RelativeAngleWheelMotion movimientoRuedas = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_LEFT, 5, 360);
                        wheelMotionManager.doRelativeAngleMotion(movimientoRuedas);

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // mover brazos, izquierdo para abajo derecho para arriba
                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_LEFT,20,20);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_RIGHT,20,180);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);


                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_LEFT,20,180);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_RIGHT,20,20);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);


                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // resetear
                        absoluteAngleHandMotion =
                                new AbsoluteAngleHandMotion(AbsoluteAngleHandMotion.PART_BOTH,20,180);
                        handMotionManager.doAbsoluteAngleMotion(absoluteAngleHandMotion);

                        // apagar luces
                        hardwareManager.setLED(new LED(LED.PART_ALL, LED.MODE_CLOSE));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // Reactivar todos los botones
                        runOnUiThread(() -> {
                            setAllButtonsClickable(true);
                            isProcessing = false; // Liberar bandera
                        });
                    }
                }).start();

            }
        });



    }

    @Override
    public void onResume() {
        SpeakOption speakOption = new SpeakOption();
        speakOption.setSpeed(50);
        speakOption.setIntonation(50);
        super.onResume();
        // Inicializamos el sistema
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                speechManager.startSpeak("Clica en los botones y te mostraré algunas de las emociones que siento, soy un robot muy sensible", speakOption);

            }
        }, 200);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
