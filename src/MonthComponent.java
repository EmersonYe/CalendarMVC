import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
		
		final int height = this.getPreferredSize().height;
		final int width = this.getPreferredSize().width;
		final int heightPerRow = height/8;
		final int widthPerColumn = width/7;
		
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				int columnClicked = e.getX()/widthPerColumn;
				int rowClicked = e.getY()/heightPerRow - 2;
				clickedOnDate(columnClicked,rowClicked);
			}
		});
	}

	private void clickedOnDate(int columnClicked, int rowClicked) {
		int dayOfMonth=0;
		GregorianCalendar c = dataModel.getDayToView();
		int startingDay = new GregorianCalendar(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1).get(Calendar.DAY_OF_WEEK) -2;
		
		dayOfMonth += columnClicked-startingDay;
		for (int i = 0; i < rowClicked; i++)
		{
			dayOfMonth += 7;
		}
		
		if(dayOfMonth > 0 && dayOfMonth <= c.getActualMaximum(Calendar.DAY_OF_MONTH))
			dataModel.setDayToView(dayOfMonth);
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
