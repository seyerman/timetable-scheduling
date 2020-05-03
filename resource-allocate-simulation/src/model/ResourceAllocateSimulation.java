package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import ui.ScheduleFileReader;

public class ResourceAllocateSimulation {
	private Schedule schedule;
	private List<Session> sessionSchedule;
	private List<Resource> resources;
	
	public ResourceAllocateSimulation(Schedule sch) {
		schedule = sch;		
		scheduleToClassSession();
	}
	
	private void scheduleToClassSession(){
		sessionSchedule = new ArrayList<>();
		Iterator<Map<String, String>> scheduleDetailIterator = schedule.getScheduleDetail().iterator();
		while (scheduleDetailIterator.hasNext()) {
			Map<String, String> session = (Map<String, String>) scheduleDetailIterator.next();			
			Collection<Session> cs = createSession(session);
			sessionSchedule.addAll(cs);
		}
	}

	private Collection<Session> createSession(Map<String, String> session) {
		
		String daysRaw = session.get(ScheduleFileReader.DAYS_COLUMN);
		String[] days = daysRaw.split(ScheduleFileReader.DAYS_SEPARATOR);
		
		Collection<Session> cs = new ArrayList<>();
		
		for (int i = 0; i < days.length; i++) {
			Day theDay = Day.valueOf2(days[i]);
			Session classSession = new Session(theDay, session);
			cs.add(classSession);
		}
		
		return cs;
	}
	
	public void simulate() {
		List<SessionEvent> events = generateEvents();
		Collections.sort(events);
		
		for (SessionEvent sessionEvent : events) {
			System.out.println(sessionEvent);
		}
		
		resources = new ArrayList<>();
		SortedSet<Resource> availableResources = new TreeSet<>();
		Map<String, Resource> busyResources = new TreeMap<>();
		
		for (SessionEvent event : events) {
			Resource res;
			if(event.getType()==SessionEventType.START) {
				if(availableResources.isEmpty()) {
					res = new Resource(resources.size());
					resources.add(res);
				}else {
					res = availableResources.first();
					availableResources.remove(res);					
				}
				res.getSessions().add(event.getSessionCourse());
				busyResources.put(event.getFullCourseCode(),res);
			}else { //event.getType()==SessionEventType.END
				res = busyResources.get(event.getFullCourseCode());
				//System.out.println(event.getFullCourseCode()+":"+res);
				busyResources.remove(event.getFullCourseCode());
				availableResources.add(res);
			}
		}
	}
	
	private List<SessionEvent> generateEvents(){
		List<SessionEvent> events = new ArrayList<SessionEvent>();
		Set<String> sessionKeys = new TreeSet<>();
		
		for(Session s: sessionSchedule) {
			String key = s.getDay()+"-"+s.getFullCourseCode()+"-"+s.getStart()+"-"+s.getEnd();
			if(!sessionKeys.contains(key)) {
				SessionEvent startEvent = new SessionEvent(s.getStart(), SessionEventType.START, s);
				SessionEvent endEvent = new SessionEvent(s.getEnd(), SessionEventType.END, s);
				
				events.add(startEvent);
				events.add(endEvent);
				
				sessionKeys.add(key);
			}
		}
		
		return events;
	}
	
	public List<Resource> getResources(){
		return resources;
	}
}
