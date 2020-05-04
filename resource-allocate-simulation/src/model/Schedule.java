package model;

import java.util.Collection;
import java.util.Map;

public class Schedule {
	private String source;
	private String reportType;
	private String academicPeriod;
	private String academicUnit;
	private String shortAcademicUnit;
	
	private Collection<String> titleColumns;
	private Collection<Map<String, String>> scheduleDetail;
	
	public Schedule(String s, String rt, String ap, String au) {
		source = s;
		reportType = rt;
		academicPeriod = ap;
		academicUnit = au;
	}
	
	public void setShortAcademicUnit(String shortAU) {
		shortAcademicUnit = shortAU;
	}
	
	public void setTitleColumns(Collection<String> tc) {
		titleColumns = tc;
	}
	
	public void setValueColumns(Collection<Map<String, String>> sch) {
		scheduleDetail = sch;
	}
	
	public String getShortAcademicUnit() {
		return shortAcademicUnit;
	}

	public String getSource() {
		return source;
	}

	public String getReportType() {
		return reportType;
	}

	public String getAcademicPeriod() {
		return academicPeriod;
	}

	public String getAcademicUnit() {
		return academicUnit;
	}

	public Collection<String> getTitleColumns() {
		return titleColumns;
	}

	public Collection<Map<String, String>> getScheduleDetail() {
		return scheduleDetail;
	}
}
