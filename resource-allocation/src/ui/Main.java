package ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import model.ResourceAllocation;
import model.Schedule;
import model.TimeSlot;

public class Main {

	public static void main(String[] args) throws IOException {
		System.out.println(Arrays.toString(args));
		if(args.length<1) {
			System.out.println("Empty input filename. Please write the name of the input file.");
		}else {
			String inputfileName = args[0];
			Schedule s = ScheduleFileReader.readSchedule(inputfileName, ScheduleFileReader.LINE_SEPARATOR);
			
			ResourceAllocation ra = new ResourceAllocation(s);
			ra.allocateResources();
			List<TimeSlot> slots = ra.getTimeSlots();
			//Collections.sort(slots); //sort by day and size of slot
			
			String output = toString(slots);
			
			String[] parts = inputfileName.split("\\.");
			parts[0] = parts[0]+"_ALLOCATION_"+getDate();
			String outputFileName = String.join(".", parts);
			PrintWriter pw = new PrintWriter(outputFileName);
			pw.write(output);
			pw.close();
		}
	}
	
	public static String toString(List<TimeSlot> slots) {
		String msg = "";
		for(TimeSlot ts:slots) {
			msg += ts + "\n";
		}
		return msg;
	}
	
	public static String getDate() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		String format = formatter.format(date);
		return format;
	}
}
