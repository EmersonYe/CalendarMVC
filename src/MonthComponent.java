import java.awt.Font;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MonthComponent extends JTextArea implements ChangeListener{
	LinkedList dataModel;
	GregorianCalendar displayMonth;
	
	public MonthComponent(LinkedList dataModel, GregorianCalendar dayToView)
	{
		this.dataModel = dataModel;
		this.displayMonth = dayToView;
		this.setText(formatMonth(displayMonth));
		this.setFont(new Font("monospaced", Font.PLAIN, 12));
	}

	public void decrementDay()
	{
		dataModel.decrementAndGetDayToView();
	}
	public void incrementDay()
	{
		dataModel.incrementAndGetDayToView();
	}
	
	private String formatMonth(Calendar c)
	{
		MONTHS[] arrayOfMonths = MONTHS.values();
		String formattedString = "";
		
		formattedString += String.format(" " + arrayOfMonths[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR) + "\n");
		formattedString += String.format(" Su  Mo  Tu  We  Th  Fr  Sa\n");

		//starting day
		int startingDay = new GregorianCalendar(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1).get(Calendar.DAY_OF_WEEK);

		//days in the month
		int daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);

		//print the calendar
		for (int i = 1; i < startingDay; i++)
		{
			formattedString += String.format("    ");
		}
		for (int i = 1; i <= daysInMonth; i++)
		{
			//highlight current date
			//event on this date
			LinkedList.Pointer pointer = dataModel.listPointer();
			boolean printed = false;
			
			if(i == c.get(Calendar.DAY_OF_MONTH))
			{
				formattedString += String.format("[%2d]", i);
				printed = true;
			}
			while(pointer.hasNext())
			{
				if( (!printed) && ((Event)pointer.get()).getStart().get(Calendar.YEAR) == c.get(Calendar.YEAR) &&
						((Event)pointer.get()).getStart().get(Calendar.MONTH) == c.get(Calendar.MONTH) && 
						((Event)pointer.get()).getStart().get(Calendar.DAY_OF_MONTH) == i )
				{
					//event found
					formattedString += String.format("-%2d-", i);
					printed = true;
					break;
				}
				pointer.next();
			}
			if(!printed)
				formattedString += String.format(" %2d ", i);

			if (((i + startingDay - 1) % 7 == 0) || (i == daysInMonth))
				formattedString += String.format("\n");
		}
		return formattedString;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		displayMonth = dataModel.getDayToView();
		this.setText(formatMonth(displayMonth));
	}
}
