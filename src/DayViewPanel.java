import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DayViewPanel extends JPanel implements ChangeListener{
	LinkedList dataModel;
	JTextArea currentDay;
	DAYS[] arrayOfDays = DAYS.values();
	String formattedString = "";
	
	public DayViewPanel(LinkedList dataModel)
	{
		this.setLayout(new BorderLayout());
		this.dataModel = dataModel;
		currentDay = new JTextArea(String.format(arrayOfDays[(dataModel.getDayToView()).get(Calendar.DAY_OF_WEEK)-1] + " " + (dataModel.getDayToView().get(Calendar.MONTH) + 1) + "/" + (dataModel.getDayToView()).get(Calendar.DAY_OF_MONTH) + "\n"));
		this.add(currentDay, BorderLayout.NORTH);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		currentDay.setText(String.format(arrayOfDays[(dataModel.getDayToView()).get(Calendar.DAY_OF_WEEK)-1] + " " + (dataModel.getDayToView().get(Calendar.MONTH) + 1) + "/" + (dataModel.getDayToView()).get(Calendar.DAY_OF_MONTH) + "\n"));
	}
	
}
