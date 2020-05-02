package model;

public enum Day{
	NULL (null),
	MONDAY ("LU"),
	TUESTDAY ("MA"),
	WEDNESDAY ("MI"),
	THURSDAY ("JU"),
	FRIDAY ("VI"),
	SATURDAY ("SA");
	
	private String shortName;
	private Day(String sn) {
		shortName = sn;
	}
	
	public String getShortName() {
		return shortName;
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
