/**
 * 
 */
package data;

/**
 * Interface for Elements in a ListOf. => must have id.
 * 
 * @author snowgoon88@gmail.com
 */
public interface IElement {
	
	/** Get id of Element */
	public int getId();
	/** Set id of Element */
	public void setId(int id);
	
	/** Called when the element is removed of a ListOf */
	public void elementRemoved();
	
	/** Dump Element */
	public String sDump();
}
