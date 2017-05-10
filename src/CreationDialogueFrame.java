import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class CreationDialogueFrame extends JFrame{


	LinkedList dataModel;
	GregorianCalendar dayToAddTo;
	JTextArea eventNameArea;
	JTextArea startTimeArea;
	JTextArea endTimeArea;
	JButton saveButton;

	public CreationDialogueFrame(LinkedList dataModel){
		this.dataModel = dataModel;
		dayToAddTo = dataModel.getDayToView();
		eventNameArea = new JTextArea("Untitled event",1,20);
		startTimeArea = new JTextArea("08:00",1,5);
		endTimeArea = new JTextArea(1,5);
		saveButton = new JButton("SAVE");

		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				Event newEvent;
				//potential shallow copy going on right here
				GregorianCalendar startTime = dataModel.getDayToView();
				startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startTimeArea.getText().substring(0, 2)));
				//ignore minutes, only check hour
				//startTime.set(Calendar.MINUTE, Integer.parseInt(startTimeArea.getText().substring(3)));

				//there is an end time
				if (endTimeArea.getText().length() >= 5)
				{
					newEvent = new Event(eventNameArea.getText(), startTime, Integer.parseInt(endTimeArea.getText().replaceAll(":", "")));
				}
				//no end time
				else
				{
					newEvent = new Event(eventNameArea.getText(), startTime);
				}

				LinkedList.Pointer pointer = dataModel.listPointer();

				//nothing loaded
				if(dataModel.first == null)
				{
					//System.out.println("Adding to empty LinkedList");
					pointer.add(newEvent);
				}
				//new event starts before pointer, adds in sorted order
				else
				{
					while( ((Event)pointer.get() ).getStart().compareTo(startTime) > 0 )
					{
						if(pointer.get().equals(dataModel.getLast()))
						{
							//System.out.println("Adding to end of LinkedList");
							pointer.next();
							break;
						}
						pointer.next();
					}
					
					//no collision
					if (!(((Event)pointer.get() ).getStart().compareTo(startTime) == 0))
					{
						//System.out.println("Adding to middle of LinkedList");
						pointer.add(newEvent);
					}
					//collision detected
					else
					{
						JOptionPane.showMessageDialog(new JFrame(), "An event already exists at this time. New event not added.");
					}
				}
				closeCreationDialogueFrame();
			}
		});


		//set borders
		eventNameArea.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		startTimeArea.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		endTimeArea.setBorder(BorderFactory.createLineBorder(Color.black, 1));

		this.setLayout(new FlowLayout());
		this.add(eventNameArea);
		this.add(startTimeArea);
		this.add(endTimeArea);
		this.add(saveButton);
		this.pack();
		this.setVisible(true);
	}

	protected void closeCreationDialogueFrame() {
		// TODO Auto-generated method stub
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

}
