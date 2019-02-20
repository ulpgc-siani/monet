package org.monet.federation.accountoffice.core.model;


public class NoAuthenticatedUser {

    public static final int SUSPEND_TIME = 60000;
    public static final int MAX_RETRIES = 6;
    private String userName;
    private int tryCount;
    private long suspendTime;
    private String captchaAnswer;
    private boolean isSuspend;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTryCount() {
        return tryCount;
    }

    public void setTryCount(int tryCount) {
        this.tryCount = tryCount;
    }

    public long getSuspendTime() {
        return suspendTime;
    }

    public void setSuspendTime(long suspendTime) {
        this.suspendTime = suspendTime;
        isSuspend = true;
    }

    public boolean isCaptchaAnswerCorrect(String answer) {
        return captchaAnswer == null || (captchaAnswer != null && captchaAnswer.equals(answer));
    }

    public void setCaptchaAnswer(String captchaAnswer) {
        this.captchaAnswer = captchaAnswer;
    }

    public boolean isSuspend() {
        if (isSuspend && (System.currentTimeMillis() - suspendTime) > SUSPEND_TIME) {
            isSuspend = false;
            return isSuspend;
        }
        return isSuspend;
    }

}
