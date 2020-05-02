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
	
	public boolean crossSchedules(ClassSession other) {
		if(dayOfWeek!=other.dayOfWeek) {
			return false;
		}else {
			return end>=other.start && start<=other.end;
		}
	}

	@Override
	public int compareTo(ClassSession other) {
		return (end-start)-(other.end-other.start);
	}
	
	public boolean equals(ClassSession other) {
		return compareTo(other)==0;
	}
}
