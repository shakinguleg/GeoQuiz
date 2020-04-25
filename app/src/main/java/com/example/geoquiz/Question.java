package com.example.geoquiz;

public class Question {
    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isFinishAnswer() {
        return mFinishAnswer;
    }

    public void setFinishAnswer(boolean finishAnswer) {
        this.mFinishAnswer = finishAnswer;
    }

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mFinishAnswer;
    private int mAnswerTrueCount = 0;


    public int getAnswerTrueCount() {
        return mAnswerTrueCount;
    }

    public void setAnswerTrueCount(int answerCount) {
        mAnswerTrueCount = answerCount;
    }


    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mFinishAnswer = false;
    }
}
