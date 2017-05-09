import java.awt.Font;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HourViewComponent extends JTextArea implements ChangeListener{
	LinkedList dataModel;
	GregorianCalendar displayDay;

	public HourViewComponent(LinkedList dataModel, GregorianCalendar dayToView)
	{
		super(0,64);
		this.dataModel = dataModel;
		this.displayDay = dayToView;
		this.setText(formatDay(displayDay));
		this.setFont(new Font("monospaced", Font.PLAIN, 12));
	}



	private String formatDay(GregorianCalendar displayDay) {
		String formattedString = "";

		for (int i = 0; i < 24; i++)
		{
			formattedString += i + ":00";
			GregorianCalendar hour = dataModel.getDayToView();
			hour.set(Calendar.HOUR_OF_DAY, i);
			Event eventChecker = eventAtTime(hour);
			//there is an event
			if(eventChecker != null)
			{
				formattedString += " "+eventChecker.getTitle();
			}

			formattedString += "\n";
		}

		return formattedString;
	}

	private Event eventAtTime(GregorianCalendar c)
	{
		LinkedList.Pointer pointer = dataModel.listPointer();
		while(pointer.hasNext())
		{
			Event currentEvent = (Event)pointer.get();
			if( currentEvent.getStart().get(Calendar.YEAR) == c.get(Calendar.YEAR) &&
					currentEvent.getStart().get(Calendar.MONTH) == c.get(Calendar.MONTH) && 
					currentEvent.getStart().get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH) &&
					currentEvent.getStart().get(Calendar.HOUR_OF_DAY) == c.get(Calendar.HOUR_OF_DAY))
			{
				//event found
				return currentEvent;
			}
			else
			{
				//System.out.println("event found on month" + currentEvent.getStart().get(Calendar.MONTH));
			}
			if(pointer.hasNext())
				pointer.next();

		}
		return null;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		displayDay = dataModel.getDayToView();
		this.setText(formatDay(displayDay));
	}
}
