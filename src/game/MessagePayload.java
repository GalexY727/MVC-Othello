package game;

/**
 * This is the message payload class.  Instantiate this class when sending
 * field / value message data for the up/down buttons
 * 
 * @author Roger Jaffe
 * @version 1.0
 */
public class MessagePayload {
  
  private final String message;
  private final Position position;
  
  /**
   * Class constructor
   * @param _field Text field 1 or 2
   * @param _direction Direction (Constants.UP or Constants.DOWN)
   */
  public MessagePayload(String message, Position position) {
    this.message = message;
	this.position = position;
  }

  /**
   * The function creates a new MessagePayload object with a given printOut string and null metadata.
   * 
   * @param printOut The parameter "printOut" is a string that represents the message content that will
   * be included in the MessagePayload object being created.
   * @return A new instance of the class `MessagePayload` with the `printOut` parameter passed as an
   * argument and `null` as the second argument.
   */
  public MessagePayload createMessagePayload(String printOut) {
	return new MessagePayload(printOut, null);
  }

  /**
   * This function creates a new MessagePayload object with the given printOut and position parameters.
   * 
   * @param printOut printOut is a string parameter that represents the message or text that needs to
   * be sent as a payload.
   * @param position The "position" parameter is an object of the "Position" class, which represents a
   * position in a two-dimensional space. It may contain information such as the x and y coordinates of
   * the position. This parameter is used to specify the location where the message payload should be
   * displayed or attached to.
   * @return A new instance of the class `MessagePayload` with the specified `printOut` and `position`
   * values.
   */
  public MessagePayload createMessagePayload(String printOut, Position position) {
	return new MessagePayload(printOut, position);
  }
  
  /**
   * The function returns the message stored in the object.
   * 
   * @return The `getMessage()` method is returning the value of the `message` instance variable of the
   * current object.
   */
  public String getMessage() {
	return this.message;
  }

  /**
   * This function returns the position of an object.
   * 
   * @return The method `getPosition()` is returning the `position` object.
   */
  public Position getPosition() {
	return this.position;
  }

}