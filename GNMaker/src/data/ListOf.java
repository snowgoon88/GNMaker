/**
 * 
 */
package data;

import java.util.HashMap;
import java.util.Observable;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Maintient une HashMap<Integer,T> de T.
 * Contient tjs un couple (-1,ElementNull)
 * 
 * Notify Observers:
 * <li>id_add</li>
 * <li>id_dell</li>
 * 
 * 
 * @author snowgoon88@gmail.com
 */
public class ListOf<T extends IElement> extends Observable {
	
	/** Liste */
	HashMap<Integer, T> _list;
	/** Internal : current next Id */
	int _nextId = 0;
	/** ListOf has been modified ? */
	boolean _fgModified;
	
	/* In order to Log */
	private static Logger logger = LogManager.getLogger(ListOf.class.getName());
	
	/**
	 * Création d'une liste vide.
	 */
	public ListOf(T elemNull) {
		_list = new HashMap<Integer, T>();
		_list.put(-1,  elemNull);
		_nextId = 0;
		_fgModified = false;
	}
	/**
	 * Ajoute un T à la liste et prévient les Observers.
	 * Vérifie que le T n'est pas déjà dans la liste.
	 * 
	 * @return id du nouveau T ou -1 'déjà dans liste)
	 * @toObserver : id_add
	 */
	public int add(T elem) {
		// Existe déjà ?
		if (_list.containsValue(elem)) {
			return -1;
		}
		// ajoute
		_list.put(_nextId, elem);
		elem.setId(_nextId);
		
		logger.debug(_nextId+"_add");
		setChanged();
		notifyObservers(_nextId+"_add");
		_nextId +=1;
		
		return _nextId-1;
	}
	/**
	 * Détruit un T associé à cet Id.
	 * 
	 * @param id
	 * @return Valeur détruite ou null si pas de valeur associée.
	 * @toObserver : id_del
	 */
	public T remove(int id) {
		// Il est important de prévenir AVANT de détruire (pour les mise à jour potentielles)
		logger.debug(id+"_del");
		setChanged();
		notifyObservers(id+"_del");
		T old = _list.remove(id);
		if (old != null) {
			old.elementRemoved();
		}
		return old;
	}
	/**
	 * Associe l'Elemet elem à la clef id.
	 * @param id
	 * @param elem
	 * @return null ou le T précédemment associé à id.
	 */
	public T put(int id, T elem) {
		T res = _list.put(id, elem);
		elem.setId(id);
		if (id <= _nextId) {
			_nextId = id+1;
		}
		setChanged();
		notifyObservers(_nextId+"_add");
		
		return res;
	}
	/** 
	 * @see java.util.List#get(int)
	 */
	public T get(int index) {
		return _list.get(index);
	}
	/**
	 * Renvoie un Set avec l'ensemble des données de la liste.
	 * @return
	 */
	public Set<Entry<Integer, T>> entrySet() {
		return _list.entrySet();
	}
	
	/**
	 * Vide la liste et prévient les Observers pour chaque T détruit.
	 * @see java.util.List#clear()
	 * @toObserver : id_del
	 */
	public void clear() {
		Set<Integer> keys = _list.keySet();
		
		//Vérifier qu'on en tien compte
		
		_list.clear();
		_nextId = 0;
		
		for (Integer index : keys) {
			logger.debug(index+"_del");
			setChanged();
			notifyObservers(index+"_del");	
		}
	}
	
	/**
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(T elem) {
		for (Entry<Integer, T> entry : _list.entrySet()) {
			if (entry.getValue().equals(elem)) {
				return entry.getKey();
			}
		}
		return -1;
	}
	
	/** 
	 * Dump all T as a (id,T.SDump()).
	 * @return String
	 */
	public String sDump() {
		StringBuffer str = new StringBuffer();
		for (Entry<Integer, T> entry : _list.entrySet()) {
			str.append("("+entry.getKey()+", "+entry.getValue().sDump()+") ");
		}
		return str.toString();
	}
}