package model;

import java.util.Map;

import ui.ScheduleFileReader;

public class Session implements Comparable<Session>{
	private Day dayOfWeek;
	private int start;
	private int end;
	private String courseCode;
	private String group;
	
	private Map<String, String> additionalInfo;

	public Session(Day dow, Map<String, String> info) {
		
		String hours = info.get(ScheduleFileReader.HOUR_COLUMN);
		String[] hourParts = hours.split(ScheduleFileReader.HOUR_SEPARATOR);
		
		start = Integer.parseInt(hourParts[0]);
		end = Integer.parseInt(hourParts[1]);

		String cc = info.get(ScheduleFileReader.COURSE_CODE_COLUMN);
		cc = cc.split(ScheduleFileReader.COURSE_CODE_SEPARATOR)[1];
		group = info.get(ScheduleFileReader.GROUP_COLUMN);
		
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

	public String getFullCourseCode() {
		return getCourseCode()+"-"+getGroup();
	}

	public String getCourseCode() {
		return courseCode;
	}

	public String getGroup() {
		return group;
	}

	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}

	public String toString() {
		return getFullCourseCode()+":"+getStart()+":"+getEnd();
	}
	
	//special cross validation
	public boolean crossSchedules(Session other) {
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

	public boolean fullCrossSchedules(Session other) {
		if(dayOfWeek!=other.dayOfWeek) {
			return false;
		}else {
			return end>=other.start && start<=other.end;
		}
	}

	@Override
	public int compareTo(Session other) {
		if(dayOfWeek!=other.dayOfWeek) {
			return dayOfWeek.compareTo(other.dayOfWeek);
		}else if(start!=other.start){
			return start-other.start;
		}else {
			return end-other.end;
		}
	}
	
	public boolean equals(Session other) {
		return compareTo(other)==0;
	}
}
