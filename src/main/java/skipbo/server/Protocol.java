package skipbo.server;

/**
 * Enum containing network protocol commands.
 * The layout of a full network protocol command is: COMMAND§Option§Argument1§Argument2...
 * The command is always an all-capital 5-letter code.
 */
public enum Protocol {
    PRINT, /** Prints a message given as argument to a location given as option. */
    CHATM, /** For server: sends all clients the received chat message; For client: prints chat message (options: Global, Private). */
    SETTO, /** Sets a parameter given as option to the value given as argument. */
    CHNGE, /** Changes an already existing parameter given as option to the value given as argument. */
    LGOUT, /** For server: client wants to log ut; For Client: confirmation that logout was successful. */
    PLAYC; /** Plays a card with index i from Pile f to Pile t (i, f and t given as arguments). */

}