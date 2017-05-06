import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * This class will convert a file (events.txt) to a doubly-linked list.
 * 
 * @author Emerson Ye
 */
public class Nodify {
	/**
	 * This method takes in a file (events.txt) and converts it into an arraylist. 
	 * Each index will contain 3 lines from events.txt
	 * Assumes events in file are sorted chronologically, latest event first
	 * 
	 * @param f The input file (events.txt)
	 * @return A LinkedList of parsed info from input file (events.txt)
	 */
	public LinkedList fileToLinkedList(File f){
		LinkedList events = new LinkedList();
		try{
			BufferedReader scanner = new BufferedReader(new FileReader(f));
			String scanLine = scanner.readLine();
			while(scanLine!=null){
				String[] inputArray = scanLine.split("/");
				int year = Integer.parseInt(inputArray[0].substring(4));
				int month = Integer.parseInt(inputArray[0].substring(0, 2));
				int day = Integer.parseInt(inputArray[0].substring(2, 4));
				int hour = Integer.parseInt(inputArray[2].substring(0, 2));
				int minute = Integer.parseInt(inputArray[2].substring(2));
				GregorianCalendar eventStart = new GregorianCalendar(year,month-1,day,hour,minute);
				if(inputArray.length == 3)//no end time
				{
					events.addLast(new Event(inputArray[1],eventStart));
				}
				else//end time
				{

					events.addLast(new Event(inputArray[1],eventStart,Integer.parseInt(inputArray[3])));
				}
				scanLine = scanner.readLine();
			}
			scanner.close();
		}
		catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
		return events;
	}

	public void linkedListToFile(LinkedList l, File f)
	{
		LinkedList.Pointer pointer = l.listPointer();
		try {
			PrintWriter fileWriter = new PrintWriter(new BufferedWriter(new FileWriter(f))); 			//learned from http://www.c-jump.com/bcc/c257c/Week10/Week10.html#W10_0030_formatted_output_pri
			while(pointer.hasNext())
			{
				Event currentEvent = (Event) pointer.get();
				String title = currentEvent.getTitle();
				int month = currentEvent.getStart().get(Calendar.MONTH);
				int year = currentEvent.getStart().get(Calendar.YEAR);
				int dayOfMonth = currentEvent.getStart().get(Calendar.DAY_OF_MONTH);
				int hour = currentEvent.getStart().get(Calendar.HOUR_OF_DAY);
				int minute = currentEvent.getStart().get(Calendar.MINUTE);
				if(currentEvent.getEndingTime() > 0)//there is an end time
					fileWriter.printf("%02d%02d%04d/%s/%02d%02d/%04d",month+1,dayOfMonth,year,title,hour,minute,currentEvent.getEndingTime());
				else//no end time
					fileWriter.printf("%02d%02d%04d/%s/%02d%02d",month+1,dayOfMonth,year,title,hour,minute);
				fileWriter.write("\n");
				pointer.next();
			}
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}