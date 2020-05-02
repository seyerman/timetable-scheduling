package model;

import java.util.Collection;

import ui.ScheduleFileReader;

public class TimeSlot implements Comparable<TimeSlot>{
	private int id;
	private Day day;
	private Collection<ClassSession> sessions;
	public TimeSlot(int id, Day day, Collection<ClassSession> sessions) {
		this.id = id;
		this.day = day;
		this.sessions = sessions;
	}
	public int getId() {
		return id;
	}
	public Day getDay() {
		return day;
	}
	public Collection<ClassSession> getSessions() {
		return sessions;
	}
	@Override
	public int compareTo(TimeSlot other) {
		if(day!=other.day) {
			if(day!=null && other.day!=null) {
				return day.compareTo(other.day);
			}else {
				return day==null?-1:1;
			}
		}else {
			return sessions.size()-other.sessions.size();
		}
	}
	public String toString() {
		String dayStr = day==null?null:day.getShortName();
		String msg = "";
		//msg += id+ScheduleFileReader.LINE_SEPARATOR;
		msg += sessions.size()+ScheduleFileReader.LINE_SEPARATOR;
		msg += dayStr;
		for (ClassSession cs:sessions) {
			msg += ScheduleFileReader.LINE_SEPARATOR + cs.toString();
		}
		return msg;
	}
}
