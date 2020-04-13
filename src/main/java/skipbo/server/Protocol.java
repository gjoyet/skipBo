package skipbo.server;

/**
 * Enum containing network protocol commands.
 * The layout of a full network protocol command is: COMMAND§Option§Argument1§Argument2...
 * The command is always an all-capital 5-letter code.
 */
public enum Protocol {
    /** Prints a message given as argument to a location given as option. */
    PRINT,
    /** For server: sends all clients the received chat message; For client: prints chat message (options: Global, Private). */
    CHATM,
    /** Sets a parameter given as option to the value given as argument. */
    SETTO,
    /** Changes an already existing parameter given as option to the value given as argument. */
    CHNGE,
    /** For server: client wants to log ut; For Client: confirmation that logout was successful. */
    LGOUT,
    /** Starts new game with the first 4 players which have the status 'READY' */
    NWGME,
    /** Plays a card with index i from Pile fr to Pile to (i, fr and to given as arguments). */
    PUTTO,
    /** Gives the client information about the game state (e.g. what its handcards are in that moment). */
    CHECK,
    /** Displays information on chat window. */
    DISPL;

}