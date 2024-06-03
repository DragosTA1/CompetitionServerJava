package ro.mpp2024.services;

public class CompetitionException extends Exception{
    public CompetitionException() {
    }

    public CompetitionException(String message) {
        super(message);
    }

    public CompetitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
