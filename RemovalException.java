package exceptions;

/**
 * Exception for bad removals.
 *
 * <p>
 * Some data structures don't like certain removals. For example, the Tree
 * interface doesn't allow remove() on a position with children, the Graph
 * interface doesn't allow remove() on a vertex with incident edges, etc.
 * </p>
 */
public class RemovalException extends RuntimeException {
  /**
   * Constructs a new RemovalException.
   */
  public RemovalException() {

  }

  /**
   * Constructs a new RemovalException with the specified detail message.
   *
   * @param message the detail message. The detail message is saved for
   *                later retrieval by the {@link #getMessage()} method.
   */
  public RemovalException(String message) {
    super(message);
  }
}