import java.util.GregorianCalendar;

public class Event {
	private String title;
	private GregorianCalendar start;
	private int endingTime;
	
	public Event(String title, GregorianCalendar start)
	{
		this.setTitle(title);
		this.setStart(start);
	}
	public Event(String title, GregorianCalendar start, int endingTime)
	{
		this.setTitle(title);
		this.setStart(start);
		this.setEndingTime(endingTime);
	}
	
	//getters and setters
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public GregorianCalendar getStart() {
		return start;
	}
	public void setStart(GregorianCalendar start) {
		this.start = start;
	}
	public int getEndingTime() {
		return endingTime;
	}
	public void setEndingTime(int endingTime) {
		this.endingTime = endingTime;
	}
	public String getEndingTimeFormatted() {
		String first = String.format("%02d",Integer.parseInt(Integer.toString(endingTime).substring(0, 2)));
		String second = String.format("%02d",Integer.parseInt(Integer.toString(endingTime).substring(2)));
		return first+":"+second;
	}
}