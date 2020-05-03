package model;

import java.util.ArrayList;
import java.util.List;

public class Resource implements Comparable<Resource>{
	private int id;
	private List<Session> sessions;
	public Resource(int identifier) {
		id = identifier;
		sessions = new ArrayList<Session>();
	}
	public int getId() {
		return id;
	}
	public List<Session> getSessions() {
		return sessions;
	}
	@Override
	public int compareTo(Resource other) {
		return id-other.id;
	}
}
