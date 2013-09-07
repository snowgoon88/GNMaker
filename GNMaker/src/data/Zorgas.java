/**
 * 
 */
package data;


import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Collection;
import java.util.Observable;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Maintient une Hash de String, avec un id = key.
 * 
 * Notify Observers:
 * <li>id_add</li>
 * <li>id_set</li>
 * <li>id_del</li>
 * 
 * @author snowgoon88@gmail.com
 */
public class Zorgas extends Observable {

	/** Liste d'Orga */
	HashMap<Integer, String> _zorgas;
	/** Internal : current next Id */
	int _nextId = 0;
	/** Zorgas has been modified ? */
	boolean _fgModified;
	
	/* In order to Log */
	private static Logger logger = LogManager.getLogger(Zorgas.class.getName());
	
	/**
	 * Création d'une liste d'Orga vide.
	 */
	public Zorgas() {
		_zorgas = new HashMap<Integer,String>();
		_nextId = 0;
		_fgModified = false;
	}

//	/* (non-Javadoc)
//	 * @see java.util.List#toArray()
//	 */
//	public String[] toArray() {
//		return (String[]) _zorgas.toArray();
//	}

	/**
	 * Ajoute un Orga à la liste et prévient les Observers.
	 * Vérifie que l'Orga n'est pas déjà dans la liste.
	 * 
	 * @return id du nouvel orga ou -1
	 * @toObserver : id_add
	 */
	public int add(String orga) {
		// Existe déjà ?
		if (_zorgas.containsValue(orga)) {
			return -1;
		}
		// ajoute
		_zorgas.put(_nextId, orga);
		setChanged();
		notifyObservers(_nextId+"_add");
		_nextId +=1;
		return _nextId-1;
	}
	/**
	 * Détruit un Orga associé à cet Id.
	 * 
	 * @param zorgaId
	 * @return Valeur détruite ou null si pas de valeur associée.
	 * @toObserver : id_del
	 */
	public String remove(int zorgaId) {
		// Il est important de prévenir AVANT de détruire (pour les mise à jour potentielles)
		logger.debug(zorgaId+"_del");
		setChanged();
		notifyObservers(zorgaId+"_del");
		String old = _zorgas.remove(zorgaId);
		return old;
	}
	/**
	 * Vide la liste et prévient les Observers pour chaque Orga détruit.
	 * @see java.util.List#clear()
	 * @toObserver : id_del
	 */
	public void clear() {
		Set<Integer> keys = _zorgas.keySet();
		
		//Vérifier qu'on en tien compte
		
		_zorgas.clear();
		_nextId = 0;
		
		for (Integer index : keys) {
			logger.debug(index+"_del");
			setChanged();
			notifyObservers(index+"_del");	
		}
	}

	/** 
	 * @see java.util.List#get(int)
	 */
	public String get(int index) {
		return _zorgas.get(index);
	}
	/**
	 * AJoute ou modifie la valeur associée à l'index.
	 * 
	 * @return Valeur précédente ou null (si pas de valeur précédente).
	 * @toObserver : id_set
	 */
	public String set(int index, String orga) {
		String res = _zorgas.put(index, orga);
		if (_nextId <= index) {
			_nextId = index+1;
		}
		setChanged();
		notifyObservers(index+"_set");
		return res;
	}

	/**
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(String zorg) {
		for (Entry<Integer, String> entry : _zorgas.entrySet()) {
			if (entry.getValue().equals(zorg)) {
				return entry.getKey();
			}
		}
		return -1;
	}

	/**
	 * Renvoie un Array qui contient tous les zorgas.
	 * @return Object[]
	 */
	public Object[] toArray() {
		Collection<String> res = _zorgas.values();
		return res.toArray();
	}
	
	/**
	 * Renvoie un Set avec l'ensemble des données de _zorgas.
	 * @return
	 */
	public Set<Entry<Integer, String>> entrySet() {
		return _zorgas.entrySet();
	}
	
	/** 
	 * Dump all Zorga as a (id,String).
	 * @return String
	 */
	public String SDump() {
		StringBuffer str = new StringBuffer();
		for (Entry<Integer, String> entry : _zorgas.entrySet()) {
			str.append("("+entry.getKey()+", "+entry.getValue()+") ");
		}
		return str.toString();
	}

	
	/**
	 * Est-ce que cette liste de Zorga a été modifiée (besoin de save).
	 * @return true or false.
	 */
	public boolean isModified() {
		return _fgModified;
	}
	/**
	 * Indique si cette liste de Zorga a été modifiée.
	 * @param flag
	 */
	public void setModified( boolean flag ) {
		_fgModified = flag;
	}
	
}
