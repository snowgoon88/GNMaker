/**
 * 
 */
package test;

import java.util.Map.Entry;

import data.IElement.State;
import data.ListOf;
import data.Zorga;

/**
 * @author snowgoon88@gmail.com
 *
 */
public class TestList {

	public void run(String[] args) {	
		boolean res;
		int nbTest = 0;
		int nbPassed = 0;

		// -------
		nbTest++;
		res = testUpdate(args);
		if (res) {
			System.out.println("testUpdate >> " + res);
			nbPassed++;
		} else {
			System.err.println("testUpdate >> " + res);
		}
		
		// ---------------------
		if (nbTest > nbPassed) {
			System.err.println("FAILURE : only "+nbPassed+" success out of "+nbTest);
			System.exit(1);
		}
		else {
			System.out.println("SUCCESS : "+nbPassed+" success out of "+nbTest);
			System.exit(0);
		}
	}
	
	
	
	/**
	 * Update copyRemote en fonction de remote.
	 */
	public void synchroCopyRemote( ListOf<Zorga> copyRemote, ListOf<Zorga> remote) {
		// On regarde les éléments de copyRemote qui sont différent de remote
		for (Entry<Integer, Zorga> zCopy : copyRemote.entrySet()) {
			int cKey = zCopy.getKey();
			// Il se peut que remote n'ait pas cette 'key'
			Zorga zRemote = remote.get(cKey);
			if (zRemote == null) {
				// A été effacé.
				zCopy.getValue().setStatus(State.DELETED);
			}
			else {
				zCopy.getValue().updateWith(zRemote);
			}
		}
		// On regarde les éléments qui ont été ajouté
		for (Entry<Integer, Zorga> zRemote : remote.entrySet()) {
			int rKey = zRemote.getKey();
			Zorga zCopy = copyRemote.get(rKey);
			if (zCopy == null) {
				// Crée un nouveau
				zRemote.getValue().setStatus(State.UPDATED);
				copyRemote.put( rKey, zRemote.getValue());
			}
		}
	}
	
	/**
	 * On part sur le fait qu'il y a 3 listes:
	 * + remote : c'est ce qui est sur le serveur
	 * + copyRemote : c'est en local, une copie de remote (normalement)
	 * + local : c'est notre version à nous.
	 * 
	 * Cette fonction teste comment évolue l'état de copyRemote en fonction 
	 * des valeurs actuelles de remote.
	 */
	boolean testUpdate(String[] args) {
		boolean res = true;
		ListOf<Zorga> _copyRemote = new ListOf<Zorga>(Zorga.zorgaNull);
		_copyRemote.put( 1, new Zorga("UN"));
		_copyRemote.put( 3, new Zorga("TROIS"));
		System.out.println("COPYremote "+_copyRemote.sDump());
		
		ListOf<Zorga> _remote = new ListOf<Zorga>(Zorga.zorgaNull);
		_remote.put( 1, new Zorga("UN"));
		_remote.put( 3, new Zorga("TROIS"));
		System.out.println("REMOTE "+_remote.sDump());
		
		System.out.println("// Normalement, ne devrait rien Updater");
		//_copyRemote.updateWith(_remote);
		synchroCopyRemote(_copyRemote, _remote);
		for (Entry<Integer, Zorga> entry : _copyRemote.entrySet()) {
			res = (entry.getValue().getStatus() == State.BASIC);
			if (res == false) {
				System.out.println("COPYre "+_copyRemote.sDump());
				System.out.println("REMOTE "+_remote.sDump());
				System.err.println("Equal : elemt="+entry.getKey()+" : "+entry.getValue().sDump()+
						" devrait être State.BASIC");
				System.exit(1);
			}
		}
		
		
		System.out.println("// Devrait dire que elem no=3 est nouveau");
		_remote.get(3).setName("newer");
		//_copyRemote.updateWith(_remote);
		synchroCopyRemote(_copyRemote, _remote);
		res = (_copyRemote.get(1).getStatus() == State.BASIC);
		if (res == false) {
			System.out.println("COPYre "+_copyRemote.sDump());
			System.out.println("REMOTE "+_remote.sDump());
			System.err.println("Newer : elemt=1 : "+_copyRemote.get(1).sDump()+
					" devrait être State.BASIC");
			System.exit(1);
		}
		res = (_copyRemote.get(3).getStatus() == State.UPDATED);
		if (res == false) {
			System.out.println("COPYre "+_copyRemote.sDump());
			System.out.println("REMOTE "+_remote.sDump());
			System.err.println("Newer : elemt=3 : "+_copyRemote.get(1).sDump()+
					" devrait être State.UPDATED");
			System.exit(1);
		}
		
		System.out.println("// Ajout d'un élément");
		_remote.put( 4, new Zorga("QUATRE"));
		//_copyRemote.updateWith(_remote);
		synchroCopyRemote(_copyRemote, _remote);
		res = (_copyRemote.get(4).getStatus() == State.UPDATED);
		if (res == false) {
			System.out.println("COPYre "+_copyRemote.sDump());
			System.out.println("REMOTE "+_remote.sDump());
			System.err.println("Added : elemt=4 : "+_copyRemote.get(1).sDump()+
					" devrait être State.UPDATED");
			System.exit(1);
		}
		
		System.out.println("// Retrait d'un élément");
		_remote.remove(1);
		//_copyRemote.updateWith(_remote);
		synchroCopyRemote(_copyRemote, _remote);
		res = (_copyRemote.get(1).getStatus() == State.DELETED);
		if (res == false) {
			System.out.println("COPYre "+_copyRemote.sDump());
			System.out.println("REMOTE "+_remote.sDump());
			System.err.println("Deleted : elemt=1 : "+_copyRemote.get(1).sDump()+
					" devrait être State.DELETED");
			System.exit(1);
		}
		System.out.println("COPYre "+_copyRemote.sDump());
		
		return true;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestList app = new TestList();
		app.run(args);

	}

}
