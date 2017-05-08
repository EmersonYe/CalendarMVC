import java.awt.Font;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MonthComponent extends JTextArea implements ChangeListener{
	LinkedList DataModel;
	GregorianCalendar displayMonth;
	
	public MonthComponent(LinkedList DataModel)
	{
		this.DataModel = DataModel;
		this.displayMonth = new GregorianCalendar();
		this.setText(formatMonth(displayMonth));
		this.setFont(new Font("monospaced", Font.PLAIN, 12));
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
			if(i == c.get(Calendar.DAY_OF_MONTH))
			{
				formattedString += String.format("[%2d]", i);
			}
			else
				formattedString += String.format(" %2d ", i);

			if (((i + startingDay - 1) % 7 == 0) || (i == daysInMonth))
				formattedString += String.format("\n");
		}
		return formattedString;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		this.setText(formatMonth(displayMonth));
	}
}
