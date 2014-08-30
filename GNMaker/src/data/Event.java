package data;

import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Un Event estcomposé de :
 * <ul>
 * <li>String: Un Titre</li>
 * <li>??? : Une Date</li>
 * <li>String: Un Body</li>
 * <li>ListOf<PersoEvent> : les perso liés à l'Event.</li>
 * </ul>
 * <br>
 * 
 * Notify Observers:
 * <ul>
 * <li>set_title</li>
 * <li>set_body</li>
 * </ul>
 * 
 * Anciens notification:
 * <ul>
 * <li>Perso : when added</li>
 * <li>"removed"</li>
 * <li>'' when setStatusChanged</li>
 * </ul>
 * SHOULD armonize with other Observable
 * 
 * @author snowgoon88@gmail.com
 */
public class Event extends Observable implements IElement, Observer {
		
	/** Appartient à une Story */
	public Story _story;
	/** Id de l'Event */
	int _id = -1;
	/** Titre de l'événement */
	String _title;
	/** Corps de l'événement */
	String _body;
	/** Date de l'événement */
	// Date _date;
	/** Liste de Perso impliqués. */
	public ListOf<PersoEvent> _listPE;
	
	/** In order to Log */
	private static Logger logger = LogManager.getLogger(Perso.class.getName());
	
	
	/**
	 * Creation sans Personnage
	 * @param _title Titre de l'événement
	 * @param _body Descripion de l'événement
	 */
	public Event(Story story, String _title, String _body) {
		_story = story;
		this._title = _title;
		this._body = _body;
		_listPE = new ListOf<Event.PersoEvent>();
		
		_story._persoList.addObserver(this);
	}

	/** 
	 * Add a Perso, by default with "status"=false.
	 * 
	 * @param pers Perso to add
	 */
	public void addPerso( Perso pers) {
		addPerso( pers, false, "-");
	}
	public void addPerso( Perso pers, boolean status, String desc) {
		logger.trace("adding : "+pers.sDump());
		PersoEvent pe = new PersoEvent( pers, status, desc);
		_listPE.put( pe.getId(), pe);
		
	}
	/**
	 * Remove a Perso (si le Perso n'existe pas, rien n'est removed).
	 * 
	 * @param pers to remove
	 */
	public void removePerso( Perso pers ) {
		_listPE.remove(pers.getId());
	}
	
	/**
	 * @return the _title
	 */
	public String getTitle() {
		return _title;
	}
	/**
	 * @param title the _title to set
	 */
	public void setTitle(String title) {
		this._title = title;
		
		logger.debug(getTitle()+" set_title");
		setChanged();
		notifyObservers("set_title");
	}

	/**
	 * @return the _body
	 */
	public String getBody() {
		return _body;
	}
	/**
	 * @param body the _body to set
	 */
	public void setBody(String body) {
		this._body = body;
		
		logger.debug(getTitle()+" set_body");
		setChanged();
		notifyObservers("set_body");
	}

	/** 
	 * Dump all Perso as a String.
	 * @return String
	 */
	public String sDump() {
		StringBuffer str = new StringBuffer();
		str.append( "Event : "+_title+" ("+getId()+")"+"\n");
		str.append( _body+"\n");
		str.append( _listPE.sDump());
		str.append( "\n" );
		return str.toString();
	}
	
	@Override
	/**
	 * Si 'arg' est 'id_del', this.removePerso(Perso avec cet id').
	 */
	public void update(Observable o, Object arg) {
		// Log
		logger.debug(getTitle()+" o is a "+o.getClass().getName()+ " arg="+arg);
		
		// Observe seulement un ListOf<Perso>
		// o est un ListOf<Perso>
		// arg est un string
		StringTokenizer sTok = new StringTokenizer((String)arg, "_");
		int id = Integer.parseInt(sTok.nextToken());
		String command = sTok.nextToken();

		// "del" -> remove specific Perso.
		if (command.equals("del")) {
			removePerso(_story._persoList.get(id));
		}
	}
	@Override
	public int getId() {
		return _id;
	}

	@Override
	public void setId(int id) {
		_id = id;
	}

	@Override
	public void elementRemoved() {
		logger.debug(getTitle()+" del");
		setChanged();
		notifyObservers("del");
	}
	
	/**
	 * Les données liant Perso à Event.
	 * <ul>
	 * <li> _status : à jour ou pas</li>
	 * <li> _desc : la description de l'évt du pt de vue du Perso.</li>
	 * </ul>
	 */
	public static class PersoEvent extends Observable implements IElement {
		public Perso _perso;
		public boolean _status;
		String _desc;
		
		/** In order to Log */
		private static Logger loggerPE = LogManager.getLogger(PersoEvent.class.getName());
		
		
		/**
		 * @param _status
		 * @param _desc
		 */
		public PersoEvent(Perso perso, boolean _status, String _desc) {
			this._perso = perso;
			this._status = _status;
			this._desc = _desc;
		}
		/**
		 * @return the _desc
		 */
		public String getDesc() {
			return _desc;
		}
		/**
		 * @param desc the _desc to set
		 */
		public void setDesc(String desc) {
			this._desc = desc;
			
			loggerPE.debug("desc");
			setChanged();
			notifyObservers("desc");
		}
		
		
		/**
		 * @return the _status
		 */
		public boolean getStatus() {
			return _status;
		}
		/**
		 * @param status the _status to set
		 */
		public void setStatus(boolean status) {
			this._status = status;
			
			loggerPE.debug("status");
			setChanged();
			notifyObservers("status");
		}
		@Override
		public int getId() {
			return _perso.getId();
		}
		@Override
		public void setId(int id) {
			// Ne fait rien car cela reviendrait à changer de Perso
		}
		@Override
		public void elementRemoved() {
			// Ne fait rien
			
		}
		@Override
		public String sDump() {
			StringBuffer str = new StringBuffer();
			str.append( "PersoEvent with perso_id="+_perso.getId()+" ("+_status+") ");
			str.append( _desc+"\n");
			return str.toString();
		}
	}

	
}
