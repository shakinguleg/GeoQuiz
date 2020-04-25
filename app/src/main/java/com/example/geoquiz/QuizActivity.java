package com.example.geoquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_IS_CHEATER = "is_cheater";
    private static final String KEY_CHEAT_INDEX = "cheat_index";
    private static final int REQUEST_CODE_CHEAT = 0;


    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    private Question[] mQuestions = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_oceans, true)
    };

    private int mCurrentIndex = 0;                                                 //当前题目索引
    private int mAnswerTrueCount = 0;                                              //回答正确的个数
    private int mAnswerCount = 0;                                                  //回答了的个数
    private boolean mIsCheater;
    private int mCheatIndex;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mCheatIndex = savedInstanceState.getInt(KEY_CHEAT_INDEX);
            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER);
        }

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestions[mCurrentIndex].setFinishAnswer(true);
                switchButtonState();
                checkAnswer(true);
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestions[mCurrentIndex].setFinishAnswer(true);
                switchButtonState();
                checkAnswer(false);
            }
        });

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCheatIndex = mCurrentIndex;
                boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
                switchButtonState();
                updateQuestion();
            }
        });

        mQuestionTextView = findViewById(R.id.question_text_view);
        updateQuestion();
    }

    /**
     * 根据是否回答过, 来切换回答按钮的是否可用
     */
    private void switchButtonState() {
        if (mQuestions[mCurrentIndex].isFinishAnswer()) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        } else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putInt(KEY_CHEAT_INDEX, mCheatIndex);
        outState.putBoolean(KEY_IS_CHEATER, mIsCheater);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestions[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }


    /**
     * 根据用户按下的按钮判断回答正确与否, 并在回答完所有题目后给出判分
     *
     * @param userPressedTrue true或false
     */
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;
        Toast mToast = null;
        mAnswerCount++;

        if (mIsCheater&&mCurrentIndex== mCheatIndex) {
                messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mAnswerTrueCount++;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }


        mToast = Toast.makeText(QuizActivity.this, messageResId, Toast.LENGTH_SHORT);
        showToast(mToast);

        if (mAnswerCount == mQuestions.length) {
            giveAMark();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_CHEAT:
                if (data == null) {
                    return;
                }
                mIsCheater = CheatActivity.wasAnswerShown(data);
        }

    }

    private void giveAMark() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                float score = (float) mAnswerTrueCount / mAnswerCount;
                String showScore = getString(R.string.score, score * 100);
                Toast mScoreToast = Toast.makeText(QuizActivity.this, showScore, Toast.LENGTH_LONG);
                showToast(mScoreToast);
            }
        }, 1000);
    }

    private void showToast(Toast toast) {
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
}
