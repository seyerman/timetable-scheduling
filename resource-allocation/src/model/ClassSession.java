package model;

import java.util.Map;

import ui.ScheduleFileReader;

public class ClassSession implements Comparable<ClassSession>{
	private Day dayOfWeek;
	private int start;
	private int end;
	private String courseCode;
	
	private Map<String, String> additionalInfo;

	public ClassSession(Day dow, Map<String, String> info) {
		
		String hours = info.get(ScheduleFileReader.HOUR_COLUMN);
		String[] hourParts = hours.split(ScheduleFileReader.HOUR_SEPARATOR);
		
		start = Integer.parseInt(hourParts[0]);
		end = Integer.parseInt(hourParts[1]);

		String cc = info.get(ScheduleFileReader.COURSE_CODE_COLUMN);
		cc = cc.split(ScheduleFileReader.COURSE_CODE_SEPARATOR)[1];
		
		dayOfWeek = dow;
		courseCode = cc;
		additionalInfo = info;
	}
	
	public int duration() {
		return end-start;
	}

	public Day getDay() {
		return dayOfWeek;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}

	public String toString() {
		return courseCode+":"+start+"-"+end;
	}
	
	//special cross validation
	public boolean crossSchedules(ClassSession other) {
		if(dayOfWeek!=other.dayOfWeek) {
			return false;
		}else {
			if((duration()<300 && other.duration()<300) || (duration()>=300 && other.duration()>=300)) {
				return start==other.start || (start<other.start && other.end<=end);
			}else {
				return false;
			}
		}
	}

	public boolean fullCrossSchedules(ClassSession other) {
		if(dayOfWeek!=other.dayOfWeek) {
			return false;
		}else {
			return end>=other.start && start<=other.end;
		}
	}

	@Override
	public int compareTo(ClassSession other) {
		if(dayOfWeek!=other.dayOfWeek) {
			return dayOfWeek.compareTo(other.dayOfWeek);
		}else if(start!=other.start){
			return start-other.start;
		}else {
			return other.end-end;//I want the biggest session is first
		}
	}
	
	public boolean equals(ClassSession other) {
		return compareTo(other)==0;
	}
}
