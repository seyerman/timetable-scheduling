package ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import model.Day;
import model.Resource;
import model.ResourceAllocateSimulation;
import model.Schedule;
import model.Session;

public class Main {

	public static void main(String[] args) throws IOException {
		System.out.println(Arrays.toString(args));
		if(args.length<1) {
			System.out.println("Empty input filename. Please write the name of the input file.");
		}else {
			String inputfileName = args[0];
			Schedule s = ScheduleFileReader.readSchedule(inputfileName, ScheduleFileReader.LINE_SEPARATOR);
			
			ResourceAllocateSimulation ra = new ResourceAllocateSimulation(s);
			ra.simulate();
			List<Resource> resources = ra.getResources();
			
			String output = toString(resources);
			
			String[] parts = inputfileName.split("\\.");
			parts[0] = parts[0]+"_ALLOCATION_"+getDate();
			String outputFileName = String.join(".", parts);
			PrintWriter pw = new PrintWriter(outputFileName);
			pw.write(output);
			pw.close();
		}
	}
	
	public static String toString(List<Resource> resources) {
		String[] msg = new String[Day.values().length];
		for (int i = 0; i < msg.length; i++) {
			msg[i] = "";
		}
		msg[0] = "DAY;ZOOM-ID";
		for(Resource res:resources) {
			Day day = null;
			for(Session s:res.getSessions()) {
				if(s.getDay().getId()!=0) {
					if(day!=s.getDay()) {
						day = s.getDay();
						
						if(!msg[day.getId()-1].equals("")) {
							msg[day.getId()-1] += "\n";
						}
						
						msg[day.getId()-1] += day.getShortName() + ScheduleFileReader.LINE_SEPARATOR + res.getId();	
					}
					msg[day.getId()-1] += ScheduleFileReader.LINE_SEPARATOR + s;
				}
			}			
		}
		return String.join("\n", msg);
	}
	
	public static String getDate() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
		String format = formatter.format(date);
		return format;
	}
}
