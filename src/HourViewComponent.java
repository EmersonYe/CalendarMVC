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
		this.dataModel = dataModel;
		this.displayDay = dayToView;
		this.setText(formatDay(displayDay));
		this.setFont(new Font("monospaced", Font.PLAIN, 12));
	}


	
	private String formatDay(GregorianCalendar displayDay) {
		String formattedString = "";
		
		for (int i = 1; i < 25; i++)
		{
			formattedString += i + ":00\n";
		}
		
		return formattedString;
	}



	@Override
	public void stateChanged(ChangeEvent e) {
		displayDay = dataModel.getDayToView();
		this.setText(formatDay(displayDay));
	}
}
