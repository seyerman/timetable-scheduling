package model;

public enum Day{
	NULL (0,"SIN DÍA"),
	MONDAY (1,"LU"),
	TUESTDAY (2,"MA"),
	WEDNESDAY (3,"MI"),
	THURSDAY (4,"JU"),
	FRIDAY (5,"VI"),
	SATURDAY (6,"SA"),
	SUNDAY (7,"DO");
	
	private int id;
	private String shortName;
	private Day(int idDay, String sn) {
		id = idDay;
		shortName = sn;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public int getId() {
		return id;
	}
	
	public static Day valueOf2(String shortName) {
		for (Day d:Day.values()) {
			if(d!=NULL && shortName.contentEquals(d.shortName)) {
				return d;
			}
		}
		return NULL;
	}
}
