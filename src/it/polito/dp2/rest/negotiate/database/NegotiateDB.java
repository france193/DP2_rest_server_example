package it.polito.dp2.rest.negotiate.database;

import java.util.HashMap;
import java.util.Map;

import it.polito.dp2.rest.negotiate.model.Negotiate;

public class NegotiateDB {

	// this is a database class containing a static Map of negotiate objects
	private static Map<Long,Negotiate> map = new HashMap<Long, Negotiate>();
	private static long last=0;

	public static Map<Long, Negotiate> getMap() {
		return map;
	}

	public static void setMap(Map<Long, Negotiate> map) {
		NegotiateDB.map = map;
	}
	
	public static long getNext() {
		return ++last;
	}

}
