package ui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
			File f = new File(inputfileName);
			
			List<String> inFileNames  = new ArrayList<>();
			List<String> outFileNames = new ArrayList<>();;
			
			String dateStr = getDate();
			String extReq = ".csv";
			String extExc = "_ALLOCATION_SIMULATED.csv";
			
			if(f.isDirectory()) {
				
				String outDirStr = f.getAbsolutePath()+"_output";
				File[] files = f.listFiles();
				for (int i = 0; i < files.length; i++) {
					String fileName = files[i].getName();
					if(fileName.length()>4 
						&& extReq.equals(fileName.substring(fileName.length()-extReq.length(), fileName.length()))
						&& (fileName.length()<extExc.length() || !extExc.equals(fileName.substring(fileName.length()-extExc.length(), fileName.length())))){
						
						inFileNames.add(files[i].getAbsolutePath());
						outFileNames.add(getOutputFileName(files[i], extReq, extExc, dateStr,outDirStr));		
					}
				}
				//System.out.println(Arrays.toString(files));
				File outputDir = new File(outDirStr);
				if(!outputDir.exists()) {
					outputDir.mkdir();
				}
			}else {
				inFileNames.add(inputfileName);
				outFileNames.add(getOutputFileName(f, extReq, extExc, dateStr,f.getParentFile().getAbsolutePath()));				
			}				
			
			for(int i=0;i<inFileNames.size();i++) {
				String inputName = inFileNames.get(i);
				String outputName = outFileNames.get(i);
				
				Schedule s = ScheduleFileReader.readSchedule(inputName, ScheduleFileReader.LINE_SEPARATOR);
				
				if(s==null) {
					System.out.println(inputName+" has no data");
				}else {
					ResourceAllocateSimulation ra = new ResourceAllocateSimulation(s);
					ra.simulate();
					List<Resource> resources = ra.getResources();
					
					String output = toString(resources);
					
					PrintWriter pw = new PrintWriter(outputName);
					pw.write(output);
					pw.close();
				}
			}
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
	
	public static String getOutputFileName(File file, String extReq, String extExc, String dateStr, String outDirStr) {
		String fileName = file.getName();
		
		String baseName = fileName.substring(0,fileName.length()-extReq.length());
		String outFName = outDirStr+File.separator+baseName+"_"+dateStr+extExc;
		
		return outFName;
	}
}
