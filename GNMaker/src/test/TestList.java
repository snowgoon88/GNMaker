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

	boolean testUpdate(String[] args) {
		boolean res = true;
		ListOf<Zorga> _master = new ListOf<Zorga>(Zorga.zorgaNull);
		_master.put( 1, new Zorga("UN"));
		_master.put( 3, new Zorga("TROIS"));
		System.out.println("MASTER "+_master.sDump());
		
		ListOf<Zorga> _remote = new ListOf<Zorga>(Zorga.zorgaNull);
		_remote.put( 1, new Zorga("UN"));
		_remote.put( 3, new Zorga("TROIS"));
		System.out.println("REMOTE "+_remote.sDump());
		
		System.out.println("// Normalement, ne devrait rien Updater");
		_master.updateWith(_remote);
		for (Entry<Integer, Zorga> entry : _master.entrySet()) {
			res = (entry.getValue().getStatus() == State.BASIC);
			if (res == false) {
				System.out.println("MASTER "+_master.sDump());
				System.out.println("REMOTE "+_remote.sDump());
				System.err.println("Equal : elemt="+entry.getKey()+" : "+entry.getValue().sDump()+
						" devrait être State.BASIC");
				System.exit(1);
			}
		}
		
		
		System.out.println("// Devrait dire que elem no=3 est nouveau");
		_remote.get(3).setName("newer");
		_master.updateWith(_remote);
		res = (_master.get(1).getStatus() == State.BASIC);
		if (res == false) {
			System.out.println("MASTER "+_master.sDump());
			System.out.println("REMOTE "+_remote.sDump());
			System.err.println("Newer : elemt=1 : "+_master.get(1).sDump()+
					" devrait être State.BASIC");
			System.exit(1);
		}
		res = (_master.get(3).getStatus() == State.UPDATED);
		if (res == false) {
			System.out.println("MASTER "+_master.sDump());
			System.out.println("REMOTE "+_remote.sDump());
			System.err.println("Newer : elemt=3 : "+_master.get(1).sDump()+
					" devrait être State.UPDATED");
			System.exit(1);
		}
		
		System.out.println("// Ajout d'un élément");
		_remote.put( 4, new Zorga("QUATRE"));
		_master.updateWith(_remote);
		res = (_master.get(4).getStatus() == State.UPDATED);
		if (res == false) {
			System.out.println("MASTER "+_master.sDump());
			System.out.println("REMOTE "+_remote.sDump());
			System.err.println("Added : elemt=4 : "+_master.get(1).sDump()+
					" devrait être State.UPDATED");
			System.exit(1);
		}
		
		System.out.println("// Retrait d'un élément");
		_remote.remove(1);
		_master.updateWith(_remote);
		res = (_master.get(1).getStatus() == State.DELETED);
		if (res == false) {
			System.out.println("MASTER "+_master.sDump());
			System.out.println("REMOTE "+_remote.sDump());
			System.err.println("Deleted : elemt=1 : "+_master.get(1).sDump()+
					" devrait être State.DELETED");
			System.exit(1);
		}
		System.out.println("MASTER "+_master.sDump());
		
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
