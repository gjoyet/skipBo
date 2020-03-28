package skipbo.server;

/**
 * Enum containing network protocol commands.
 * The layout of a full network protocol command is: COMMAND§Option§Argument1§Argument2...
 * The command is always an all-capital 5-letter code.
 */
public enum Protocol {
    /**
     * Prints a message given as argument to a location given as option.
     */
    PRINT,
    /**
     * For the server: sends all clients the received chat message; For the client: prints chat message (options: Global, Private).
     */
    CHATM,
    /**
     * Sets a parameter given as option to the value given as argument.
     */
    SETTO,
    /**
     * Changes an already existing parameter given as option to the value given as argument.
     */
    CHNGE,
    /**
     * For the server: client wants to log out; For Client: confirmation that logout was successful.
     */
    LGOUT


}