package model;

import java.util.SortedSet;

import ui.ScheduleFileReader;

public class TimeSlot implements Comparable<TimeSlot>{
	private Day day;
	private SortedSet<ClassSession> sessions;
	public TimeSlot(Day day, SortedSet<ClassSession> sessions) {
		this.day = day;
		this.sessions = sessions;
	}
	public Day getDay() {
		return day;
	}
	public SortedSet<ClassSession> getSessions() {
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
		String msg = ""+dayStr;
		for (ClassSession cs:sessions) {
			msg += ScheduleFileReader.LINE_SEPARATOR + cs.toString();
		}
		return msg;
	}
}
