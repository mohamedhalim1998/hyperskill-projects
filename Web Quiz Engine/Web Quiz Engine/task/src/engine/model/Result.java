package engine.model;

public class Result {
    private boolean success;
    private String feedback;

    public Result(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getFeedback() {
        return feedback;
    }
}