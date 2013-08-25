/**
 * 
 */
package data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Maintient une Hash de Perso, avec un id = key.
 * 
 * Notify Observers:
 * <li>id_add</li>
 * <li>id_del</li>
 * <li>0_clear</li>
 * 
 * @author snowgoon88@gmail.com
 */
public class PersoList extends Observable {
	/** A besoin d'une liste d'Orga */
	public Zorgas _zorgas;
	
	
	/** Liste de Perso */
	HashMap<Integer, Perso> _persoList;
	/** Internal : current next Id */
	int _nextId = 0;
	/** PersoList has been modified ? */
	boolean _fgModified;
	
	/**
	 * Creation d'une liste de Perso vide.
	 */
	public PersoList( Zorgas zorgas ) {
		_zorgas = zorgas;
		_persoList = new HashMap<Integer, Perso>();
		_nextId = 0;
		_fgModified = false;
	}
	
	/**
	 * Ajoute un Perso à la liste et prévient les Observers.
	 * Vérifie que le Perso n'est pas déjà dans la liste.
	 * 
	 * @return id du nouveau perso ou -1
	 * @toObserver : id_add
	 */
	public int add(Perso p) {
		// Existe déjà ?
		if (_persoList.containsValue(p)) {
			return -1;
		}
		// ajoute
		_persoList.put(_nextId, p);
		setChanged();
		notifyObservers(_nextId+"_add");
		_nextId +=1;
		return _nextId-1;
	}
	/**
	 * Associe le Perso p à la clef id.
	 * @param id
	 * @param p
	 * @return null ou le Perso précédemment associé à id.
	 */
	public Perso put(int id, Perso p) {
		Perso res = _persoList.put(id, p);
		if (id <= _nextId) {
			_nextId = id+1;
		}
		setChanged();
		notifyObservers(_nextId+"_add");
		
		return res;
	}
	/**
	 * Détruit un Perso associé à cet Id.
	 * 
	 * @param persoId
	 * @return Valeur détruite ou null si pas de valeur associée.
	 * @toObserver : id_del
	 */
	public Perso remove(int persoId) {
		Perso old = _persoList.remove(persoId);
		if (old != null ) {
			setChanged();
			notifyObservers(persoId+"_del");
		}
		return old;
	}
	/**
	 * Vide la liste et prévient les Observers pour chaque Perso détruit.
	 * @see java.util.List#clear()
	 * @toObserver : id_del
	 */
	public void clear() {
		// TODO Vérifier, une fois qu'on a Story qui Observe _persoList, que
		// TODO c'est bien utile de différencier les Remove.
		
		Object[] keys = _persoList.keySet().toArray();
		for (int i=0; i < keys.length; i++) {
			_persoList.remove((int) keys[i]);
			setChanged();
			notifyObservers((int) keys[i]+"_del");
		}
		_nextId = 0;
	}
	
	/** 
	 * @see java.util.List#get(int)
	 */
	public Perso get(int index) {
		return _persoList.get(index);
	}
	/**
	 * Trouve l'index/key d'un Perso (ou -1 si existe pas)
	 * @param perso
	 * @return int ou -1 si existe pas.
	 */
	public int indexOf(Perso perso) {
		for (Entry<Integer, Perso> entry : _persoList.entrySet()) {
			if (entry.getValue().equals(perso)) {
				return entry.getKey();
			}
		}
		return -1;
	}
	/**
	 * Renvoie un Array qui contient tous les perso.
	 * @return Object[]
	 */
	public Object[] toArray() {
		Collection<Perso> res = _persoList.values();
		return res.toArray();
	}
	/**
	 * Renvoie un Set avec l'ensemble des données de _persoList.
	 * @return
	 */
	public Set<Entry<Integer, Perso>> entrySet() {
		return _persoList.entrySet();
	}
	
	/** 
	 * Dump all Perso as a (id,String).
	 * @return String
	 */
	public String SDump() {
		StringBuffer str = new StringBuffer();
		for (Entry<Integer, Perso> entry : _persoList.entrySet()) {
			str.append("("+entry.getKey()+", "+entry.getValue()+") ");
		}
		return str.toString();
	}
	
	/**
	 * Est-ce que cette liste de Perso a été modifiée (besoin de save).
	 * @return true or false.
	 */
	public boolean isModified() {
		return _fgModified;
	}
	/**
	 * Indique si cette liste de Perso a été modifiée.
	 * @param flag
	 */
	public void setModified( boolean flag ) {
		_fgModified = flag;
	}
}
