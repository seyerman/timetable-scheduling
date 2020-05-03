package model;

public class SessionEvent implements Comparable<SessionEvent>{
	private int time;
	private SessionEventType type;
	private Session sessionCourse;
	
	public SessionEvent(int time, SessionEventType type, Session sessionCourse) {
		this.time = time;
		this.type = type;
		this.sessionCourse = sessionCourse;
	}

	public Day getDay() {
		return sessionCourse.getDay();
	}

	public int getTime() {
		return time;
	}

	public SessionEventType getType() {
		return type;
	}

	public String getFullCourseCode() {
		return sessionCourse.getFullCourseCode();
	}

	public String getCourseCode() {
		return sessionCourse.getCourseCode();
	}

	public String getGroup() {
		return sessionCourse.getGroup();
	}

	public Session getSessionCourse() {
		return sessionCourse;
	}

	@Override
	public int compareTo(SessionEvent other) {
		if(sessionCourse.getDay()!=other.sessionCourse.getDay()) {
			return sessionCourse.getDay().compareTo(other.sessionCourse.getDay());
		}else if(time!=other.time){
			return time-other.time;
		}else {
			return type.compareTo(other.type);
		}
	}
	
	public String toString() {
		return getDay().getShortName()+":"+getFullCourseCode()+":"+getTime()+":"+getType();
	}
}
