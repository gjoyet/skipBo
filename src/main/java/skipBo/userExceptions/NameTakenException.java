package skipBo.userExceptions;

public class NameTakenException extends Exception {
    String name = null;

    public NameTakenException(String name) {
        this.name = name;
    }

    public String findName() {
        int i = 1;
        String nameWithNumber = this.name + i;
        while (skipBo.server.SBServer.getLobby().nameIsTaken(nameWithNumber)) {
            i++;
            nameWithNumber = name + i;
        }

        return nameWithNumber;
    }
}
