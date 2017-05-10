import java.awt.BorderLayout;
import java.util.Calendar;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DayViewPanel extends JPanel implements ChangeListener{
	DAYS[] arrayOfDays = DAYS.values();
	LinkedList dataModel;
	JTextArea currentDay;
	HourViewComponent hourViewComponent;
	
	public DayViewPanel(LinkedList dataModel)
	{
		this.setLayout(new BorderLayout());
		this.dataModel = dataModel;
		hourViewComponent = new HourViewComponent(dataModel, dataModel.getDayToView());
		currentDay = new JTextArea(String.format( arrayOfDays[(dataModel.getDayToView()).get(Calendar.DAY_OF_WEEK)-1] + " " + (dataModel.getDayToView().get(Calendar.MONTH) + 1) + "/" + (dataModel.getDayToView()).get(Calendar.DAY_OF_MONTH) + "\n"));
		this.add(currentDay, BorderLayout.NORTH);
		this.add(hourViewComponent, BorderLayout.CENTER);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		//update display current day
		currentDay.setText(String.format(arrayOfDays[(dataModel.getDayToView()).get(Calendar.DAY_OF_WEEK)-1] + " " + (dataModel.getDayToView().get(Calendar.MONTH) + 1) + "/" + (dataModel.getDayToView()).get(Calendar.DAY_OF_MONTH) + "\n"));
		hourViewComponent.stateChanged(new ChangeEvent(this));
	}
	
}
