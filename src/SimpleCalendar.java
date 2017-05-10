import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

enum MONTHS
{
	Jan, Feb, March, Apr, May, June, July, Aug, Sep, Oct, Nov, Dec;
}
enum DAYS
{
	Sun, Mon, Tue, Wed, Thur, Fri, Sat ;
}

public class SimpleCalendar 
{
	static LinkedList events = new LinkedList();
	public static void main(String [] args)
	{
		MONTHS[] arrayOfMonths = MONTHS.values();
		DAYS[] arrayOfDays = DAYS.values();
		GregorianCalendar cal = new GregorianCalendar(); // capture today
		//LinkedList events = new LinkedList();
		Scanner sc = new Scanner(System.in);
		
		File f = new File("events.txt");
		if(!f.exists())
			System.out.println("This is the first run.");
		else{
			events = new Nodify().fileToLinkedList(f);
			System.out.println("Load successful.");
		}
		
		//gui
		JFrame frame = new JFrame();
		JPanel monthPanel = new JPanel();
		JPanel menuPanel = new JPanel();
		DayViewPanel dayViewPanel = new DayViewPanel(events);
		
		final MonthComponent monthView = new MonthComponent(events, events.getDayToView());
		
		
		//create new event button
		JButton createButton = new JButton("CREATE");
		createButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				CreationDialogueFrame creationFrame = new CreationDialogueFrame(events);
			}
		});
		
		JButton quitButton = new JButton("QUIT");
		quitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		
		
		// prev/next day buttons
		JButton prevDay = new JButton("<");
		prevDay.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				monthView.decrementDay();;
			}
		});
		
		JButton nextDay = new JButton(">");
		nextDay.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				monthView.incrementDay();;
			}
		});
		
		events.addChangeListener(monthView);
		events.addChangeListener(dayViewPanel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(0,2));
		buttonPanel.add(prevDay);
		buttonPanel.add(nextDay);
		
		menuPanel.setLayout(new BorderLayout());
		menuPanel.add(createButton, BorderLayout.WEST);
		menuPanel.add(quitButton, BorderLayout.EAST);
		
		monthPanel.setLayout(new BorderLayout());
		monthPanel.add(buttonPanel, BorderLayout.NORTH);
		monthPanel.add(monthView, BorderLayout.CENTER);
		
		frame.setLayout(new BorderLayout());
		
		frame.add(menuPanel, BorderLayout.NORTH);
		frame.add(dayViewPanel, BorderLayout.CENTER);
		frame.add(monthPanel, BorderLayout.WEST);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		
		
		
		// text-based display
		System.out.println("Welcome to Goggle Calendar!");
		printCurrentCalendar(cal);
		System.out.println("\nSelect one of the following options:\n[L]oad   [V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
		while (sc.hasNextLine())
		{

			String input = sc.nextLine().toLowerCase();
			//quit
			if (input.equals("q"))
				break;
			//load
			if (input.equals("l"))
			{
				f = new File("events.txt");
				if(!f.exists())
					System.out.println("This is the first run.");
				else{
					events = new Nodify().fileToLinkedList(f);
					System.out.println("Load successful.");
				}
			}
			//view by
			else if (input.equals("v"))
			{
				System.out.println("[D]ay view or [M]view?: ");
				String viewChoice = sc.nextLine().toLowerCase();
				if(viewChoice.equals("d"))
				{
					printDay(cal,events);
					String daySelector="";
					GregorianCalendar calDupe= cal;
					while(!daySelector.equals("m"))
					{
						System.out.println("[P]revious or [N]ext or [M]ain menu?: ");
						daySelector = sc.nextLine().toLowerCase();

						if(daySelector.equals("p"))
						{
							calDupe = new GregorianCalendar(calDupe.get(Calendar.YEAR),
									calDupe.get(Calendar.MONTH),calDupe.get(Calendar.DAY_OF_MONTH)-1);
						}
						else if(daySelector.equals("n"))
						{
							calDupe = new GregorianCalendar(calDupe.get(Calendar.YEAR),
									calDupe.get(Calendar.MONTH),calDupe.get(Calendar.DAY_OF_MONTH)+1);
						}
						printDay(calDupe,events);
					}
				}
				else if(viewChoice.equals("m"))
				{
					printCalendar(cal,events);
					String monthSelector="";
					GregorianCalendar calDupe= cal;
					while(!monthSelector.equals("m"))
					{
						System.out.println("[P]revious or [N]ext or [M]ain menu?: ");
						monthSelector = sc.nextLine().toLowerCase();

						if(monthSelector.equals("p"))
						{
							calDupe = new GregorianCalendar(calDupe.get(Calendar.YEAR),
									calDupe.get(Calendar.MONTH)-1,calDupe.get(Calendar.DAY_OF_MONTH));
						}
						else if(monthSelector.equals("n"))
						{
							calDupe = new GregorianCalendar(calDupe.get(Calendar.YEAR),
									calDupe.get(Calendar.MONTH)+1,calDupe.get(Calendar.DAY_OF_MONTH)+1);
						}
						printCalendar(calDupe,events);
					}
				}
			}
			//create
			else if (input.equals("c"))
			{
				System.out.println("Event title: ");
				String title = sc.nextLine();
				System.out.println("Event date (MM/DD/YYYY): ");
				String[] date = sc.nextLine().split("/");
				System.out.println("Event start time (HH:MM): ");
				String[] startTime = sc.nextLine().split(":");
				System.out.println("Event end time (HH:MM). Leave blank if none: ");
				String endResponse = sc.nextLine();


				int year = Integer.parseInt(date[2]);
				int month = Integer.parseInt(date[0]);
				int day = Integer.parseInt(date[1]);
				int hour = Integer.parseInt(startTime[0]);
				int minute = Integer.parseInt(startTime[1]);
				GregorianCalendar eventStart = new GregorianCalendar(year,month-1,day,hour,minute);

				Event newEvent;
				if(endResponse.length()>0){
					int endTime = Integer.parseInt(endResponse.replaceAll(":", ""));
					newEvent = new Event(title,eventStart,endTime);
				}
				else
					newEvent = new Event(title,eventStart);

				LinkedList.Pointer pointer = events.listPointer();

				//nothing loaded
				if(events.first == null)
				{
					pointer.add(newEvent);
				}
				//new event starts before pointer, adds in sorted order
				else
				{
					while( ((Event)pointer.get()).getStart().compareTo(eventStart)>0 )
					{
						if(pointer.get().equals(events.getLast()))
						{
							pointer.next();
							break;
						}
						pointer.next();
					}
					pointer.add(newEvent);
				}

			}
			//go to
			else if (input.equals("g"))
			{
				System.out.println("Enter date to go to (MM/DD/YYYY): ");
				String[] desiredDate = sc.nextLine().split("/");
				printDay(new GregorianCalendar(Integer.parseInt(desiredDate[2]),Integer.parseInt(desiredDate[0])-1
						,Integer.parseInt(desiredDate[1])),events);
			}
			//event list
			else if (input.equals("e"))
			{
				LinkedList.Node currentNode = events.first;
				while(currentNode != null)
				{
					Event currentEvent = (Event) currentNode.data;
					String title = currentEvent.getTitle();
					String month = arrayOfMonths[currentEvent.getStart().get(Calendar.MONTH)].toString();
					String dayOfWeek = arrayOfDays[currentEvent.getStart().get(Calendar.DAY_OF_WEEK)-1].toString();
					int year = currentEvent.getStart().get(Calendar.YEAR);
					int dayOfMonth = currentEvent.getStart().get(Calendar.DAY_OF_MONTH);
					int hour = currentEvent.getStart().get(Calendar.HOUR_OF_DAY);
					int minute = currentEvent.getStart().get(Calendar.MINUTE);

					if(currentEvent.getEndingTime()>0)//ending time exists
						System.out.printf("%d %s %s %d %d:%02d-%s | %s\n", year, dayOfWeek, month, dayOfMonth, hour, minute, currentEvent.getEndingTimeFormatted(), title);
					else //no ending time
						System.out.printf("%d %s %s %d %d:%02d | %s\n", year, dayOfWeek, month, dayOfMonth, hour, minute, title);

					currentNode = currentNode.next;

				}
			}
			//delete
			else if (input.equals("d"))
			{
				System.out.println("Delete [A]ll events or [S]elect date to delete events from: ");
				String deletionChoice = sc.nextLine().toLowerCase();
				if (deletionChoice.equals("a"))
				{
					events = new LinkedList();
					System.out.println("All events successfully deleted.");
				}

				else if (deletionChoice.equals("s"))
				{
					//delete event from selected date.
					System.out.println("Enter date to delete from (MM/DD/YYYY): ");
					String[] desiredDate = sc.nextLine().split("/");
					deleteEvents(new GregorianCalendar(Integer.parseInt(desiredDate[2]),Integer.parseInt(desiredDate[0])-1
							,Integer.parseInt(desiredDate[1])),events);
				}
			}
			
			System.out.println("\nSelect one of the following options:\n[L]oad   [V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");

		}
		new Nodify().linkedListToFile(events, new File("events.txt"));
		System.out.println("Thank you for using Goggle Calendar.");
		sc.close();
	}

	protected static void save() {
		// TODO Auto-generated method stub
		new Nodify().linkedListToFile(events, new File("events.txt"));
	}

	public static void printCurrentCalendar(Calendar c)
	{   
		MONTHS[] arrayOfMonths = MONTHS.values();

		System.out.println(" " + arrayOfMonths[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR));
		System.out.println(" Su  Mo  Tu  We  Th  Fr  Sa");

		//starting day
		int startingDay = new GregorianCalendar(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1).get(Calendar.DAY_OF_WEEK);

		//days in the month
		int daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);

		//print the calendar
		for (int i = 1; i < startingDay; i++)
		{
			System.out.print("    ");
		}
		for (int i = 1; i <= daysInMonth; i++)
		{
			//highlight current date
			if(i == c.get(Calendar.DAY_OF_MONTH))
			{
				System.out.printf("[%2d]", i);
			}
			else
				System.out.printf(" %2d ", i);

			if (((i + startingDay - 1) % 7 == 0) || (i == daysInMonth))
				System.out.println();
		}

	}

	public static void printCalendar(Calendar c, LinkedList events)
	{   
		MONTHS[] arrayOfMonths = MONTHS.values();

		System.out.println(" " + arrayOfMonths[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR));
		System.out.println(" Su  Mo  Tu  We  Th  Fr  Sa");

		//starting day
		int startingDay = new GregorianCalendar(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1).get(Calendar.DAY_OF_WEEK);

		//days in the month
		int daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);

		//print the calendar
		for (int i = 1; i < startingDay; i++)
		{
			System.out.print("    ");
		}
		for (int i = 1; i <= daysInMonth; i++)
		{
			//event on this date
			LinkedList.Pointer pointer = events.listPointer();
			boolean printed = false;
			while(pointer.hasNext())
			{
				if( ((Event)pointer.get()).getStart().get(Calendar.YEAR) == c.get(Calendar.YEAR) &&
						((Event)pointer.get()).getStart().get(Calendar.MONTH) == c.get(Calendar.MONTH) && 
						((Event)pointer.get()).getStart().get(Calendar.DAY_OF_MONTH) == i )
				{
					//event found
					System.out.printf("[%2d]", i);
					printed = true;
					break;
				}
				pointer.next();
			}
			if(!printed)
				System.out.printf(" %2d ", i);

			if (((i + startingDay - 1) % 7 == 0) || (i == daysInMonth))
				System.out.println();
		}

	}

	public static void printDay(Calendar c, LinkedList events)
	{   
		MONTHS[] arrayOfMonths = MONTHS.values();
		DAYS[] arrayOfDays = DAYS.values();

		System.out.println(arrayOfDays[c.get(Calendar.DAY_OF_WEEK)-1] + ", " + arrayOfMonths[c.get(Calendar.MONTH)] + " " +
				c.get(Calendar.DAY_OF_MONTH) + ", " + c.get(Calendar.YEAR));

		LinkedList.Pointer pointer = events.listPointer();

		while(pointer.hasNext())
		{
			Event currentEvent = (Event)pointer.get();
			if( currentEvent.getStart().get(Calendar.YEAR) == c.get(Calendar.YEAR) &&
					currentEvent.getStart().get(Calendar.MONTH) == c.get(Calendar.MONTH) && 
					currentEvent.getStart().get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH) )
			{
				//event found
				if(currentEvent.getEndingTime()>0)//ending time exists
					System.out.printf("%s %d:%02d-%s\n", currentEvent.getTitle(), currentEvent.getStart().get(Calendar.HOUR_OF_DAY),
							currentEvent.getStart().get(Calendar.MINUTE),currentEvent.getEndingTimeFormatted());
				else
					System.out.printf("%s %d:%02d\n", currentEvent.getTitle(), currentEvent.getStart().get(Calendar.HOUR_OF_DAY),
							currentEvent.getStart().get(Calendar.MINUTE));
			}
			pointer.next();
		}

	}

	public static void deleteEvents(Calendar c, LinkedList events)
	{
		LinkedList.Pointer pointer = events.listPointer();

		while(pointer.hasNext())
		{
			Event currentEvent = (Event)pointer.get();
			if( currentEvent.getStart().get(Calendar.YEAR) == c.get(Calendar.YEAR) &&
					currentEvent.getStart().get(Calendar.MONTH) == c.get(Calendar.MONTH) && 
					currentEvent.getStart().get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH))
			{
				//event found
				//System.out.println("removing from month" + c.get(Calendar.MONTH));
				pointer.remove();
			}
			else
			{
				//System.out.println("event found on month" + currentEvent.getStart().get(Calendar.MONTH));
			}
			if(pointer.hasNext())
				pointer.next();

		}
	}

}
