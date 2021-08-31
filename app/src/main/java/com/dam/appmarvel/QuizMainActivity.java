package com.dam.appmarvel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class QuizMainActivity extends AppCompatActivity {

    private static final long COUNTDOWN_IN_MILLIS = 30000;

    private TextView countLabel, questionLabel, tvTime;
    private Button answerBtn1, answerBtn2, answerBtn3, answerBtn4;
    VectorDrawable vector;

    private String rightAnswer;
    private int rightAnswerCount = 0;
    private int quizCount = 1;
    static final private int QUIZ_COUNT = 10;

    CountDownTimer countDownTimer;
    private ColorStateList textColorDefaultCd;
    private long timeLeftInMillis;

    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();

    String quizData [][] = {
            {"What year was the first Iron Man movie released, kicking off the Marvel Cinematic Universe?","2008","2005","2010", "2012"},
            {"What is the name of Thor’s hammer?","Mjolnir","Vanir","Aesir", "Norn"},
            {"In the Incredible Hulk, what does Tony tell Thaddeus Ross at the end of the film?","That they are putting a team together","That he wants to study The Hulk","That he knows about S.H.I.E.L.D.", "That Thaddeus owes him money"},
            {"What is Captain America’s shield made of?","Vibranium","Promethium","Adamantium", "Carbonadium"},
            {"The Flerkens are a race of extremely dangerous aliens that resembles what?","Cats","Ducks","Reptiles", "Raccoons"},
            {"Before becoming Vision, what is the name of Iron Man’s A.I. butler?","J.A.R.V.I.S.","H.O.M.E.R.","A.L.F.R.E.D", "M.A.R.V.I.N"},
            {"What is the real name of the Black Panther?","T’Challa","M’Baku","N’Jadaka", "N’Jobu"},
            {"What is the alien race Loki sends to invade Earth in The Avengers?","The Chitauri","The Skrulls","The Kree", "The Flerkens"},
            {"Who was the last holder of the Space Stone before Thanos claims it for his Infinity Gauntlet?","Loki","Thor","The Collector", "Tony Stark"},
            {"What fake name does Natasha use when she first meets Tony?","Natalie Rushman","Natalia Romanoff","Nicole Rohan", "Naya Rabe"},
            {"What does Thor want another of when he’s in the diner?","A cup of coffee","A stack of pancakes","A pint of beer", "A slice of pie"},
            {"Where does Peggy tell Steve she wants to meet him for a dance, before he plunges into the ice?","The Stork Club","The Cotton Club","El Morocco", "The Copacabana"},
            {"About which city do Hawkeye and Black Widow often reminisce?","Budapest","Prague","Istanbul", "Sokovia"},
            {"Who does the Mad Titan sacrifice to acquire the Soul Stone?","Gamora","Cull Obsidian","Ebony Maw", "Nebula"},
            {"What is the name of the little boy Tony befriends while stranded in the Iron Man 3?","Harley","Harry","Henry", "Holden"},
            {"Where do Lady Sif and Volstagg keep the Reality Stone after the Dark Elves tried to steal it?","To the Collector","Inside Sif’s sword","In a vault on Asgard", "On Vormir"},
            {"What does the Winter Soldier say after Steve recognizes him for the first time?","Who the hell is Bucky?","Do I know you?","He’s gone", "What did you say?"},
            {"What were the three items Rocket claims he needs in order to escape the prison?","A security band, a battery, and a prosthetic leg","A security card, a fork, and an ankle monitor","A pair of binoculars, a detonator, and a prosthetic leg", "A knife, cable wires, and Peter’s mixtape"},
            {"What word does Tony utter that makes Steve say, “Language”?","Shit!","Crap!","Asshole!", "Idiot!"},
            {"What animal does Darren Cross unsuccessfully shrink in the Ant-Man?","Sheep","Duck","Mouse", "Hamster"},
            {"Who is killed by Loki in the Avengers?","Agent Coulson","Nick Fury","Maria Hill", "Doctor Erik Selvig"},
            {"Who is Black Panther’s sister?","Shuri","Nakia","Ramonda", "Okoye"},
            {"What landmark does Peter Parker rescue his classmates from in Spider-Man: Homecoming?","Washington Monument","Statue of Liberty","Mount Rushmore", "Golden Gate Bridge"},
            {"What song does Baby Groot dance to at the end of the first Guardian of the Galaxy?","‘I Want You Back’ – The Jackson 5","‘Ain’t No Mountain High Enough’ – Marvin Gaye & Tammi Terrell","‘Hooked On A Feeling’ – Voidoid", "‘Cherry Bomb’ – The Runaways"},
            {"What type of doctor is Stephen Strange?","Neurosurgeon","Cardiothoracic Surgeon","Trauma Surgeon", "Plastic Surgeon"},
            {"How many Infinity Stones are there?","Six","Five","Four", "Seven"},
            {"Where is Captain America from?","Brooklyn","Jersey City","Manhattan", "Bensonhurst"},
            {"Who is Tony Stark’s father?","Howard Stark","Ben Stark","John Stark", "He hasn't father"},
            {"Who was able to pick up Thor’s hammer in Endgame?","Captain America","Hulk","Iron Man", "Loki"},
            {"Who was responsible for King T’Chaka’s death?","Zemo","Baron","Captain America", "Red Skull"},
            {"In which movie did Spider-Man make his first appearance in the MCU?","Captain America: Civil War","Avengers: Infinity War","Venom: Let There Be Carnage", "Avengers: Endgame"},
            {"On what planet was the Soul Stone in Infinity War?","Vormir","Knowhere","Titán", "Nidavellir"},
            {"Which movie kicked off the Marvel Cinematic Universe?","Iron Man","Spider-man","Captain America", "Hulk"},
            {"Black Panther is set in which fictional country?","Wakanda","Latveria","Genosha", "Symkaria"},
            {"Who rescued Tony Stark and Nebula from space?","Captain Marvel","Hulk","Thor", "Rocket Raccoon"},
            {"Who did Captain America give his shield to in Endgame?","Sam","John","Tony", "Nick"}



    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);

        countLabel = findViewById(R.id.countLabel);
        questionLabel = findViewById(R.id.questionLabel);
        answerBtn1 = findViewById(R.id.answerBtn1);
        answerBtn2 = findViewById(R.id.answerBtn2);
        answerBtn3 = findViewById(R.id.answerBtn3);
        answerBtn4 = findViewById(R.id.answerBtn4);



        //TIME
        tvTime = findViewById(R.id.tvTime);

        timeLeftInMillis = COUNTDOWN_IN_MILLIS;

        countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timeLeftInMillis = millisUntilFinished;

                int minutes = (int) ((timeLeftInMillis / 1000) / 60);
                int seconds = (int) ((timeLeftInMillis / 1000) % 60);

                String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

                tvTime.setText(timeFormatted);
                tvTime.setTextColor(Color.BLACK);

                String alertTime = "";

                if(timeLeftInMillis < 10000){
                    tvTime.setTextColor(Color.RED);

                }

            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                tvTime.setTextColor(Color.RED);


                AlertDialog.Builder alerta = new AlertDialog.Builder(QuizMainActivity.this);
                alerta.setTitle("Oh, what a pity!!");
                alerta.setMessage("You didn't answer the question in time");


                LayoutInflater layoutInflater = LayoutInflater.from(QuizMainActivity.this);
                View timeout = layoutInflater.inflate(R.layout.imgtimeout,null);

                alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        countDownTimer.cancel();
                        showNextQuiz();
                        countDownTimer.start();
                    }
                });
                alerta.setView(timeout);
                alerta.show();
                quizCount++;

            }


        };


        // Create quizArray from quizData.
        for (int i = 0; i < quizData.length; i++) {

            // Prepare array.
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(quizData[i][0]); // Country
            tmpArray.add(quizData[i][1]); // Right Answer
            tmpArray.add(quizData[i][2]); // Choice1
            tmpArray.add(quizData[i][3]); // Choice2
            tmpArray.add(quizData[i][4]); // Choice3

            // Add tmpArray to quizArray.
            quizArray.add(tmpArray);



        }


        showNextQuiz();
    }


    public void showNextQuiz() {

        countDownTimer.start();


        // Update quizCountLabel.
        countLabel.setText("Question - " + quizCount);


        // Generate random number between 0 and 14 (quizArray's size - 1)
        Random random = new Random();
        int randomNum = random.nextInt(quizArray.size());

        // Pick one quiz set.
        ArrayList<String> quiz = quizArray.get(randomNum);

        // Set question and right answer.
        // Array format: {"Country", "Right Answer", "Choice1", "Choice2", "Choice3"}
        questionLabel.setText(quiz.get(0));
        rightAnswer = quiz.get(1);


        // Remove "Country" from quiz and Shuffle choices.
        quiz.remove(0);
        Collections.shuffle(quiz);

        // Set choices.
        answerBtn1.setText(quiz.get(0));
        answerBtn2.setText(quiz.get(1));
        answerBtn3.setText(quiz.get(2));
        answerBtn4.setText(quiz.get(3));

        // Remove this quiz from quizArray.
        quizArray.remove(randomNum);



    }

    public void checkAnswer(View view) {

        countDownTimer.cancel();

        // Get pushed button.
        Button answerBtn = findViewById(view.getId());
        String btnText = answerBtn.getText().toString();

        String alertTitle;

        if (btnText.equals(rightAnswer)) {
            // Correct
            alertTitle = "Correct!";
            rightAnswerCount++;



        } else {
            // Incorrect
            alertTitle = "Oh, Incorrect!";
        }

        LayoutInflater layoutInflater = LayoutInflater.from(QuizMainActivity.this);
        View correct = layoutInflater.inflate(R.layout.imgcorrect,null);
        LayoutInflater layoutInflater2 = LayoutInflater.from(QuizMainActivity.this);
        View incorrect = layoutInflater2.inflate(R.layout.imgincorrect,null);


        // Create AlertDialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alertTitle);
        builder.setMessage("Correct answer: " + rightAnswer);

        if(alertTitle.equals("Correct!")){
            builder.setView(correct);
        } else {
            builder.setView(incorrect);
        }

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (quizCount == QUIZ_COUNT) {
                    // Show Result.
                    Intent intent = new Intent(getApplicationContext(), QuizResultActivity.class);
                    intent.putExtra("RIGHT_ANSWER_COUNT", rightAnswerCount);
                    startActivity(intent);

                } else {
                    quizCount++;
                    showNextQuiz();
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

}


