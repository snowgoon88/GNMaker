/**
 * 
 */
package data;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Maintient une liste de String au format 'Prénom', classée par Ordre alphabétique.
 * 
 * @author snowgoon88@gmail.com
 */
public class Zorgas extends Observable{

	/** Liste d'Orga */
	ArrayList<String> _zorgas;
	/** Zorgas has been modified ? */
	boolean _fgModified;
	
	/**
	 * 
	 */
	public Zorgas() {
		_zorgas = new ArrayList<String>();
		_fgModified = false;
	}

	/* (non-Javadoc)
	 * @see java.util.List#size()
	 */
	public int size() {
		return _zorgas.size();
	}

	/* (non-Javadoc)
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty() {
		return _zorgas.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains(String orga) {
		return _zorgas.contains(orga);
	}

	/* (non-Javadoc)
	 * @see java.util.List#toArray()
	 */
	public String[] toArray() {
		return (String[]) _zorgas.toArray();
	}

	/**
	 * Ajoute un Orga à la liste et prévient les Observers.
	 * Conserve l'ordre alphabétique, avec une Majuscule au début.
	 * Vérifie que l'Orga n'est pas déjà dans la liste.
	 */
	public boolean add(String orga) {
		String endS = orga.trim().toLowerCase().substring(1);
		String startS = orga.trim().toUpperCase().substring(0, 1);
		String formatedS = startS.concat(endS);
		
		// Recherche la bonne position
		for (int i = 0; i < _zorgas.size(); i++) {
			if (_zorgas.get(i).compareTo(formatedS) ==0) {
				// Already in
				return false;
			}
			if (_zorgas.get(i).compareTo(formatedS) > 0) {
				_zorgas.add(i,formatedS );
				setChanged();
				notifyObservers(orga);
				return true;
			}
		}
		boolean res = _zorgas.add( formatedS );
		if (res) {
			setChanged();
			notifyObservers(orga);
		}
		return res;
	}
	/**
	 * Enlève un Orga à la liste et prévient les Observers.
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(String orga) {
		boolean res = _zorgas.remove(orga);
		if (res) {
			setChanged();
			notifyObservers("deleted");
		}
		return res;

	}
	/**
	 * Vide la liste et prévient les Observers.
	 * @see java.util.List#clear()
	 */
	public void clear() {
		_zorgas.clear();
		setChanged();
		notifyObservers("clear");
	}

	/** 
	 * @see java.util.List#get(int)
	 */
	public String get(int index) {
		return _zorgas.get(index);
	}
	/**
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public String set(int index, String orga) {
		setChanged();
		notifyObservers("clear");
		return _zorgas.set(index, orga);
	}

//	/**
//	 * @see java.util.List#indexOf(java.lang.Object)
//	 */
//	public int indexOf(String o) {
//		return _zorgas.indexOf(o);
//	}
	
	/** 
	 * Dump all Zorga as a String.
	 * @return String
	 */
	public String SDump() {
		StringBuffer str = new StringBuffer();
		for (String orga : _zorgas) {
			str.append(orga+"; ");
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
