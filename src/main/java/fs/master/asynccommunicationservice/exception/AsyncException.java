package fs.master.asynccommunicationservice.exception;

public class AsyncException extends RuntimeException {
    public AsyncException(String message) {
        super(message); // Exception personnalisée
    }
}