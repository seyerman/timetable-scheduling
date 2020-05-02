package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ui.ScheduleFileReader;

public class ResourceAllocation {
	private List<TimeSlot> timeSlots;
	private Schedule schedule;
	private List<ClassSession> sessionSchedule;
	
	public ResourceAllocation(Schedule sch) {
		schedule = sch;		
		scheduleToClassSession();
	}
	
	private void scheduleToClassSession(){
		sessionSchedule = new ArrayList<>();
		Iterator<Map<String, String>> scheduleDetailIterator = schedule.getScheduleDetail().iterator();
		while (scheduleDetailIterator.hasNext()) {
			Map<String, String> session = (Map<String, String>) scheduleDetailIterator.next();			
			Collection<ClassSession> cs = createSession(session);
			sessionSchedule.addAll(cs);
		}
		Collections.sort(sessionSchedule);
	}
	
	private Collection<ClassSession> createSession(Map<String, String> session) {
		
		String daysRaw = session.get(ScheduleFileReader.DAYS_COLUMN);
		String[] days = daysRaw.split(ScheduleFileReader.DAYS_SEPARATOR);
		
		Collection<ClassSession> cs = new ArrayList<>();
		
		for (int i = 0; i < days.length; i++) {
			Day theDay = Day.valueOf2(days[i]);
			ClassSession classSession = new ClassSession(theDay, session);
			cs.add(classSession);
		}
		
		return cs;
	}
	
	public void allocateResources() {
		timeSlots = new ArrayList<>();
		//PrintWriter pw = new PrintWriter("data/debug.txt");
		for (ClassSession cs1: sessionSchedule) {
			//System.out.print(cs1.getCourseCode());
			TimeSlot timeSlotChoosed = null;
			for (TimeSlot ts: timeSlots) {
				Collection<ClassSession> sessions = ts.getSessions();
				for (ClassSession cs2: sessions) {
					//pw.write(cs2.getDay().getShortName()+cs2.getCourseCode()+":"+cs2.getStart()+"-"+cs2.getEnd());
					//pw.write("x");
					//pw.write(cs1.getDay().getShortName()+cs1.getCourseCode()+":"+cs1.getStart()+"-"+cs1.getEnd());
					if(cs2.crossSchedules(cs1)) {
						//pw.write(" crossed\n");
						timeSlotChoosed = ts;
						break;
					}
					//pw.write(" no cross\n");
				}
				if(timeSlotChoosed!=null) {
					break;
				}
			}
			if(timeSlotChoosed!=null) {
				timeSlotChoosed.getSessions().add(cs1);
				//System.out.println(" added to an existing slot "+timeSlotChoosed.getDay()+" "+timeSlotChoosed.getId()+":"+timeSlotChoosed.getSessions().size());
			}else {
				Collection<ClassSession> sessions = new ArrayList<>();
				sessions.add(cs1);
				timeSlotChoosed = new TimeSlot(timeSlots.size(),cs1.getDay(), sessions);
				timeSlots.add(timeSlotChoosed);
				//System.out.println(" added to a new slot "+timeSlots.size());
			}
		}
		//pw.close();
	}
	
	public List<TimeSlot> getTimeSlots(){
		return timeSlots;
	}
}
