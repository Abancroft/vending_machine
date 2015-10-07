package vending_machine;

import java.util.ArrayList;
import java.util.Random;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Machine 
{
	
	private int totalSlots;
	private int filledSlots;
	private ArrayList<String> productList =  new ArrayList<String>();
	private ArrayList<Slot> slotList =  new ArrayList<Slot>();
	private double moneyInserted = 0.0;
	private Random random = new Random();
	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
	private Calendar calendar = Calendar.getInstance();
	private String powerOnTimestamp;
	
	public Machine(int totalSlots) 
	{
		powerOnTimestamp = dateFormat.format(calendar.getTime());
		this.totalSlots = totalSlots;
		
		//Adds a list of dummy product names
		productList.add("Peanuts");
		productList.add("Candy");
		productList.add("Cookies");
		productList.add("Chips");
		productList.add("Cola");
		productList.add("Gum");
		productList.add("Mints");
		productList.add("Lemon-Lime Soda");
		productList.add("Grape Soda");
		productList.add("Orange Soda");
		productList.add("Root Beer");
		productList.add("Crackers");
		productList.add("Snack Cakes");
		productList.add("Water");
		productList.add("Lemonade");
		productList.add("Granola Bars");
		productList.add("Licorice");
		productList.add("Pretzels");
		productList.add("Fruit Snacks");
		productList.add("Trail Mix");
		productList.add("Popcorn");
		
		
		//This fills a random number of slots with products. There is a minimum of 2 filled slots.
		random.setSeed(System.currentTimeMillis());
		this.filledSlots = random.nextInt((totalSlots - 2)+1) + 2;
		
		//Fills the slot list with an empty amount of slots equal to
		//the total number of slots.
		for(int i = 0; i < totalSlots; i++)
		{
			slotList.add(new Slot());
		}
		
		/*Creates a copy of the number of filled slots,
		fills empty slots randomly, and loops until the correct number
		of filled slots is made.
		*/
		int filledCopy = this.filledSlots;
		while(filledCopy > 0)
		{
			int currentSlotNumber = random.nextInt(this.totalSlots);
			
			//Checks to see if slot is empty before trying to fill it.
			if(slotList.get(currentSlotNumber).isEmpty())
			{
				slotList.set(currentSlotNumber, new Slot(productList.get(random.nextInt(productList.size())), Interface.round(.5 + random.nextDouble()), 1 + random.nextInt(12)));
				filledCopy--;
			}
		}
	}

	public Slot getSlotListAt(Object input) throws NumberFormatException
	{
		if (!(input instanceof Integer)) 
		{
	        	throw new NumberFormatException();
	    	}
		else
		{
			return slotList.get((Integer)input);
		}
	}

	public double getMoneyInserted() 
	{
		return moneyInserted;
	}

	public void setMoneyInserted(Object moneyInserted) throws NumberFormatException 
	{
		if (!(moneyInserted instanceof Double)) 
		{
	        	throw new NumberFormatException();
	    	}
		else
		{
			this.moneyInserted = (Double) moneyInserted;
		}
	}

	
	public int getTotalSlots() 
	{
		return totalSlots;
	}
	
	public int getFilledSlots() 
	{
		int increment = 0;
		for(Slot i : slotList)
		{
			if(i.isEmpty())
		    	{
		        	increment++;
		    	}
		}
		filledSlots = totalSlots - increment;
		return filledSlots;
	}

	public String getTimeStarted()
	{
		return powerOnTimestamp;
	}	
}
