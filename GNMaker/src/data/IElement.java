/**
 * 
 */
package data;

/**
 * @author snowgoon88@gmail.com
 *
 */
public interface IElement {
	
	static public enum State {BASIC, UPDATED, DELETED};
	
	/** Get id of Element */
	public int getId();
	/** Set id of Element */
	public void setId(int id);
	
	/** Update if different */
	public void updateWith(IElement other);
	/** Get status of Element */
	public State getStatus();
	/** Set status of Element */
	public void setStatus(State status);
	
	/** Called when the element is removed of a ListOf */
	public void elementRemoved();
	
	/** Dump Element */
	public String sDump();
}
