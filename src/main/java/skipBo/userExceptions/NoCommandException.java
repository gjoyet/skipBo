package skipBo.userExceptions;

public class NoCommandException extends Exception {
    public String command;
    public String option;

    public NoCommandException() {}

    public NoCommandException(String command, String option) {
        this.command = command;
        this.option = option;
    }
}
