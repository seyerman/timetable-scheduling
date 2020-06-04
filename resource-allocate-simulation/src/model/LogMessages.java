package model;

import java.util.ArrayList;
import java.util.List;

public class LogMessages {
	private static LogMessages logMessages;
	private ArrayList<String> log;
	
	private LogMessages() {
		log = new ArrayList<>();
	}
	
	private static void verifyInstance() {
		if(logMessages==null) {
			logMessages = new LogMessages();
		}
	}
	
	private void printMessage(String message) {
		log.add(message);
		System.out.println(message);
	}
	
	public static void print(String message) {
		verifyInstance();
		logMessages.printMessage(message);		
	}
	
	public List<String> getLogMessages(){
		return log;
	}
	
	public String getLog() {
		return String.join("\n", log);
	}
}
