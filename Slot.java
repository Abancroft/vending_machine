package vending_machine;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;

public class Slot {

    private int totalSales;
    private double totalRevenue;
	private boolean isEmpty;
	private String itemName;
	private double cost;
	private int quantity;
	private ArrayList<String> slotHistory =  new ArrayList<String>();
	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
	
	//Constructs an empty slot.
	public Slot() 
	{
		this.isEmpty = true;
		this.itemName = "";
		this.cost = 0.0;
		this.quantity = 0;
		this.totalSales = 0;
		this.totalRevenue = 0;
	}
	
	//Constructs a filled slot.
	public Slot(String itemName, double cost, int quantity) 
	{
		this.isEmpty = false;
		this.itemName = itemName;
		this.cost = cost;
		this.quantity = quantity;
		this.totalSales = 0;
		this.totalRevenue = 0;
	}

	public boolean isEmpty() 
	{
		return this.isEmpty;
	}
	
	public int getQuantity() 
	{
		return this.quantity;
	}
	
	public String getName() 
	{
		return this.itemName;
	}
	
	public double getCost() 
	{
		return this.cost;
	}
	
	public double getTotalRevenue() 
	{
		return totalRevenue;
	}
	
	public int getTotalSales() 
	{
		return totalSales;
	}
	
	public void transaction(int quantity) 
	{
		this.quantity = this.quantity - quantity;
		this.totalRevenue = this.totalRevenue + (this.cost * quantity);
		this.totalSales++;
		
		//marks date and time of transaction for records
		Calendar calendar = Calendar.getInstance();
		slotHistory.add(quantity + " item(s) bought at " + dateFormat.format(calendar.getTime()) + " for $" + Interface.round(this.cost * quantity)  + ".");
		
		//Potential signal for resupply
		if(this.quantity == 0)
		{
		    this.isEmpty = true;
		    System.out.println("Slot has become empty. Please signal for resupply if possible.");
		}
	}

	public ArrayList<String> getSlotHistory()
	{
		return slotHistory;
	}
}
