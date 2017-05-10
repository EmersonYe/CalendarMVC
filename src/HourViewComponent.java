import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HourViewComponent extends JTextArea implements ChangeListener{
	final int PxPERLINE = 16;
	LinkedList dataModel;
	GregorianCalendar displayDay;

	public HourViewComponent(LinkedList dataModel, GregorianCalendar dayToView)
	{
		super(0,64);
		this.dataModel = dataModel;
		this.displayDay = dayToView;
		this.setText(formatDay(displayDay));
		this.setFont(new Font("monospaced", Font.PLAIN, 12));
		final int height = this.getPreferredSize().height;
		final int heightPerRow = height/25;
		System.out.println(height);
		
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e) {
				//every line is 16 px
				int hourClicked = e.getY()/heightPerRow;
				clickedOnEvent(hourClicked);
			}
		});
	}

	private String formatDay(GregorianCalendar displayDay) {
		String formattedString = "";

		for (int i = 0; i < 24; i++)
		{
			formattedString += i + ":00";
			
			//FIXED: was making shallow copy. Now makes hard copy
			GregorianCalendar c = dataModel.getDayToView();
			//not checking minutes
			GregorianCalendar hour = new GregorianCalendar(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),i,0);
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
			if(pointer.hasNext())
				pointer.next();

		}
		return null;
	}
	
	private void clickedOnEvent(int time)
	{
		//will prompt user to delete event
		GregorianCalendar c = dataModel.getDayToView();
		LinkedList.Pointer pointer = dataModel.listPointer();
		while(pointer.hasNext())
		{
			Event currentEvent = (Event)pointer.get();
			if( currentEvent.getStart().get(Calendar.YEAR) == c.get(Calendar.YEAR) &&
					currentEvent.getStart().get(Calendar.MONTH) == c.get(Calendar.MONTH) && 
					currentEvent.getStart().get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH) &&
					currentEvent.getStart().get(Calendar.HOUR_OF_DAY) == time)
			{
				//event found
				int decision = JOptionPane.showConfirmDialog(null, "Delete event: \""+currentEvent.getTitle()+"\"?");
				if (decision == 0)
				{
					pointer.remove();
				}
			}
			if(pointer.hasNext())
				pointer.next();

		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		displayDay = dataModel.getDayToView();
		this.setText(formatDay(displayDay));
	}
}
