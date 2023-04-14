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

  public MessagePayload createMessagePayload(String printOut) {
	return new MessagePayload(printOut, null);
  }

  public MessagePayload createMessagePayload(String printOut, Position position) {
	return new MessagePayload(printOut, position);
  }
  
  public String getMessage() {
	return this.message;
  }

  public Position getPosition() {
	return this.position;
  }

}