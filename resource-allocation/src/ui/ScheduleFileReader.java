package ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.Schedule;

public class ScheduleFileReader {
	public static String LINE_SEPARATOR = ";";
	
	public static String DAYS_COLUMN = "DIAS";
	public static String DAYS_SEPARATOR = "-";
	public static String HOUR_COLUMN = "HORARIO";
	public static String HOUR_SEPARATOR = " - ";
	public static String COURSE_CODE_SEPARATOR = " - ";
	public static String COURSE_CODE_COLUMN = "CODIGO_MATERIA";
	
	public static Schedule readSchedule(String inputFileName, String sep) throws IOException {
		Schedule sch;
		
		BufferedReader br = new BufferedReader(new FileReader(inputFileName));
		
		String source = br.readLine();
		String reportType = br.readLine();
		String academicPeriod = br.readLine().split(sep)[1];
		String academicUnit = br.readLine();
		
		sch = new Schedule(source, reportType, academicPeriod, academicUnit);
		
		br.readLine();
		
		String titles[] = br.readLine().split(sep);
		Collection<String> titleColumns = new ArrayList<>();
		
		for (String t : titles) {
			titleColumns.add(t);
		}
		
		Collection<Map<String, String>> schedule = new ArrayList<>();
		
		String line = br.readLine();
		while(line!=null && !line.trim().equals("")) {
			String[] stringValues = line.split(sep);
			Map<String, String> values = new HashMap<>();
			
			for (int i = 0; i < stringValues.length; i++) {
				values.put(titles[i], stringValues[i]);
			}
			schedule.add(values);
			line = br.readLine();
		}
				
		br.close();
		
		sch.setTitleColumns(titleColumns);
		sch.setValueColumns(schedule);
		
		return sch;
	}
}
