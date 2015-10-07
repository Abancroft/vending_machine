package vending_machine;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.Random;

public class Interface {
    
	static Random random = new Random();
        static Machine machine = new Machine(random.nextInt(20 - 10)+1 + 10);
	static Scanner user_input = new Scanner(System.in);
	static double dollars = 10.0;
	static String input;

	public static double round(double input)
	{
		DecimalFormat twoDecimals = new DecimalFormat("#.00");
		return Double.valueOf(twoDecimals.format(input));
	}
	
	public static void help()
	{
        	System.out.println("--------------------");
		System.out.println("INSERT    Inserts money to the machine.");
		System.out.println("BUY    Buys from a specified slot.");
		System.out.println("CHECK    Returns information about the item slot.");
		System.out.println("CHECK ALL    Returns information about all item slots.");
		System.out.println("CHANGE    If money is currently inserted, change will be returned.");
		System.out.println("AUDIT    Checks machine-wide information and returns a txt record of the machine if possible.");
		System.out.println("OFF    Turns off machine and exits program.");
	}
	
	public static void insert()
	{
		//Checks to make sure the amount is non-zero.
		if(machine.getMoneyInserted() == 0.0)
		{
			System.out.println("Insert how much money?");
			input = user_input.nextLine();
			
			//Checks to make sure the amount is actually a number.
			try 
			{
				//Checks to make sure the user can afford to put money in
				if(Double.parseDouble(input) <= dollars && Double.parseDouble(input) > 0)
				{
					double checkedInput = round(Double.parseDouble(input));
					dollars = round(dollars - checkedInput);
					machine.setMoneyInserted(checkedInput);
					System.out.println("Inserted $" + checkedInput + ".");
				}
				else
				{
					System.out.println("Incorrect amount or insufficient funds. Returning to menu...");
				}
			} catch (NumberFormatException e) 
			{
				System.out.println("Not a number. Returning to menu...");
			}
		}
		else
		{
			System.out.println("You have $" + machine.getMoneyInserted() + " in the machine. Insert more? (Y/N)");
			input = user_input.nextLine();
			if(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("YES"))
			{
				System.out.println("Insert how much money?");
				input = user_input.nextLine();
				try 
				{
					if(Double.parseDouble(input) <= dollars && Double.parseDouble(input) > 0)
					{
						double checkedInput = round(Double.parseDouble(input));
						dollars = round(dollars - checkedInput);
						machine.setMoneyInserted(machine.getMoneyInserted() + checkedInput);
						System.out.println("Inserted $" + checkedInput + ".");
					}
					else
					{
						System.out.println("Incorrect amount or insufficient funds. Returning to menu...");
					}
				} catch (NumberFormatException e) 
				{
					System.out.println("Not a number. Returning to menu...");
				}
			}
		}
	}
	
	public static void change()
	{
		//Checks if money is left in machine
		if(machine.getMoneyInserted() > 0)
		{
			System.out.println("Returned $" + machine.getMoneyInserted() + ".");
			dollars  = dollars + machine.getMoneyInserted();
			machine.setMoneyInserted(0.0);
		}
		else
		{
			System.out.println("No money to return.");	
		}
	}
	
	public static void check()
	{
		System.out.println("Check which slot?");
		String input = user_input.nextLine();
		
		//Checks to make sure the input is a number
		try 
		{
			//Checks to make sure the slot exists
			if(Integer.parseInt(input) >= 0 && Integer.parseInt(input) < machine.getTotalSlots())
		    	{
				int checkedInput = Integer.parseInt(input);
				Slot currentSlot = machine.getSlotListAt(checkedInput);
				if(!currentSlot.isEmpty())
			    	{
			    		System.out.println("Slot " + checkedInput + " contains " + currentSlot.getQuantity() + " item(s) of " +
			    		currentSlot.getName() + ".");
			    		System.out.println("One item costs $" + currentSlot.getCost() + ".");
			    	}
			    	else
			    	{
			        	System.out.println("Slot " + checkedInput + " is empty. Returning to menu...");   
			    	}
		    	}
		    	else
		    	{
				System.out.println("Slot not found. Returning to menu...");
		    	}
		} catch (NumberFormatException e) 
		{
			System.out.println("Not a number. Returning to menu...");
		}
	}
	
	public static void check_all()
	{
		for(int i = 0; i < machine.getTotalSlots(); i++)
		{
			//Checks if slot is empty
			if(machine.getSlotListAt(i).getQuantity() != 0)
			{
				System.out.println("********************");
				System.out.println("Slot "+ i + ": ");
				System.out.println("Item in slot: " + machine.getSlotListAt(i).getName());
				System.out.println("Current amount of items: " + machine.getSlotListAt(i).getQuantity());
				System.out.println("Price of item: $" + machine.getSlotListAt(i).getCost());
			}
			else
			{
				System.out.println("********************");
				System.out.println("Slot "+ i + " is empty.");
			}
		}
		System.out.println("********************");
	}
	
	public static void audit()
	{
		System.out.println("--------------------");
		System.out.println("This machine has been on since " + machine.getTimeStarted());
		System.out.println(machine.getFilledSlots() + " out of " + machine.getTotalSlots() + " are filled with products.");
		
		//If machine is more than half empty, signal to contact resupplier
		if(machine.getFilledSlots() <= (machine.getTotalSlots()/2))
		{
			System.out.println("Resupply needed. Contact resupplier.");
		}
		System.out.println();
		int highestValue = -1;
		int lowestValue = -1;
		double totalMachineRevenue = 0;
		int totalItemCount = 0;
		
		for(int i = 0; i < machine.getTotalSlots(); i++)
		{
			System.out.println("********************");
			System.out.println("Slot "+ i + ": ");
			System.out.println("Item in slot: " + machine.getSlotListAt(i).getName());
			System.out.println("Current amount of items: " + machine.getSlotListAt(i).getQuantity());
			System.out.println("Current cost of items left: $" + (machine.getSlotListAt(i).getQuantity() * machine.getSlotListAt(i).getCost()));
			if(machine.getSlotListAt(i).getTotalSales() != 0)
			{
				for(String v : machine.getSlotListAt(i).getSlotHistory())
				{
					System.out.println(v);
				}
				
				//Tracks the best selling item without including zeros. Based on the reasoning that if a zero came up for best selling item,
				//the information is irrelevant anyways.
				if(highestValue == -1)
				{
					highestValue = i;
				}
				else
				{
					if(machine.getSlotListAt(highestValue).getTotalRevenue() < machine.getSlotListAt(i).getTotalRevenue())
					{
						highestValue = i;
					}
				}
			}
			
			//Seperated out from previous if statement based on the reasoning that a zero is more important in least sold than most sold.
			if(lowestValue == -1)
			{
				lowestValue = i;
			}
			else
			{
				if(machine.getSlotListAt(lowestValue).getTotalRevenue() > machine.getSlotListAt(i).getTotalRevenue())
				{
					lowestValue = i;
				}
			}
			System.out.println("Total number of sales: " + machine.getSlotListAt(i).getTotalSales());
			System.out.println("Total number of revenue: $" + machine.getSlotListAt(i).getTotalRevenue());
			System.out.println("********************");
			System.out.println();
			totalMachineRevenue = totalMachineRevenue + machine.getSlotListAt(i).getTotalRevenue();
			totalItemCount = totalItemCount + machine.getSlotListAt(i).getQuantity();
		}
		if(highestValue != -1)
		{
			System.out.println("Slot " + highestValue + " has sold the most with $" + machine.getSlotListAt(highestValue).getTotalRevenue() + " in revenue.");
		}
		if(lowestValue != -1)
		{
			System.out.println("Slot " + lowestValue + " has sold the least with $" + machine.getSlotListAt(lowestValue).getTotalRevenue() + " in revenue.");
		}
		
		System.out.println("The average revenue per slot is $" + round(totalMachineRevenue/machine.getTotalSlots()) +".");
		System.out.println("The average revenue per item is $" + round(totalMachineRevenue/totalItemCount) +".");
	}
	
	public static void buy()
	{
		//Checks to make sure money is inserted
		if(machine.getMoneyInserted() == 0.0)
		{
			System.out.println("No money in the machine. Please insert money.");
		}
		else
		{
			System.out.println("Buy from which slot?");
		    	String input = user_input.nextLine();
		    	try 
		    	{
			    	if(Integer.parseInt(input) >= 0 && Integer.parseInt(input) < machine.getTotalSlots())
		        	{
			        	int checkedInput = Integer.parseInt(input);
			        	Slot currentSlot = machine.getSlotListAt(checkedInput);
			        
					//Checks to make sure slot isn't empty
					if(!currentSlot.isEmpty())
			        	{
			            		System.out.println("Slot " + checkedInput + " contains " + currentSlot.getQuantity() + " item(s) of " +
			            		currentSlot.getName() + ".");
			        	 	System.out.println("One item costs $" + currentSlot.getCost() + ".");
			        	 	System.out.println("How many items would you like to buy?");
			            
						try
			        		{
			               			input = user_input.nextLine();
			               			checkedInput = Integer.parseInt(input);
			               			if(checkedInput < 0)
			               			{
			                   			System.out.println("Not a valid quantity. Returning to menu...");
			               			}
			        			 else if(checkedInput == 0)
			               			{
			                			System.out.println("0 for quantity. Cancelling purchase...");
			               			}
			        			 else
			               			{
			                   			//Checks to see if there is enough of the product
							   	if(currentSlot.getQuantity() - checkedInput >= 0)
			                   			{
			                       				if((checkedInput * currentSlot.getCost()) <= machine.getMoneyInserted())
			                       				{
			                            				currentSlot.transaction(checkedInput);
			                            				System.out.println("Purchased " + checkedInput + " item(s) for $" + (checkedInput * currentSlot.getCost()) + ".");
			                        				machine.setMoneyInserted(machine.getMoneyInserted() - (checkedInput * currentSlot.getCost()));
			                            				change();
			                       				}
			                       				else
			                       				{
			                            				System.out.println("Insufficient funds. Returning to menu..."); 
			                       				}
			                   			}
			                   
							   	//If there is not enough, it will try to buy what is left if the user has enough money.
							   	else
			                   			{
			                        			System.out.println("Not enough of slot's product to proceed with transaction.");
			                        			System.out.println("Would you like to buy all of the remaining product? (Y/N)");
			                        			input = user_input.nextLine();
									if(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("YES"))
									{
										if((currentSlot.getQuantity() * currentSlot.getCost()) <= machine.getMoneyInserted())
										{
											double costCopy = (currentSlot.getQuantity() * currentSlot.getCost());
											System.out.println("Purchased " + currentSlot.getQuantity() + " item(s) for $" + costCopy + ".");
											currentSlot.transaction(currentSlot.getQuantity());
											machine.setMoneyInserted(machine.getMoneyInserted() - costCopy);
											change();
										}
										else
										{
			                            					System.out.println("Insufficient funds. Returning to menu..."); 
										}
									}
			                   			}
			               			}		
			            		} catch (NumberFormatException e)
			            		{
			        			System.out.println("Not a valid quantity. Returning to menu...");
			            		}
			        	}
			        	else
			        	{
			            	System.out.println("Slot " + checkedInput + " is empty. Returning to menu...");   
			        	}
		        	}
		        	else
		        	{
			        	System.out.println("Slot not found. Returning to menu...");
		        }
		    } catch (NumberFormatException e) 
		    {
			    System.out.println("Not a number. Returning to menu...");
		    }
		}
	}
	
	public static void menuMessage()
	{
		if(machine.getMoneyInserted() != 0.0)
	    	{
	        	System.out.println("--------------------");
		    	System.out.println("Welcome to the vending machine.");
		    	System.out.println("There are " + machine.getTotalSlots() + " total item slots, of which " + machine.getFilledSlots() + " have items.");
		    	System.out.println("You currently have $" + dollars + ", and there is currently $" + machine.getMoneyInserted() + " in the machine.");
		    	System.out.println("--------------------");
	    	}
	    	else
	    	{
	    		System.out.println("--------------------");
		    	System.out.println("Welcome to the vending machine.");
		    	System.out.println("There are " + machine.getTotalSlots() + " total item slots, of which " + machine.getFilledSlots() + " have items.");
		    	System.out.println("You currently have $" + dollars + ".");
		    	System.out.println("--------------------");
	    	}
	}
	
	
	public static void main(String[] args) 
	{
		random.setSeed(System.currentTimeMillis());
	    	System.out.println("Starting machine...");
		menuMessage();
		System.out.println("Type 'help' for command list.");
		
		String input = user_input.nextLine();
		
		while(!input.equalsIgnoreCase("OFF"))
		{
			if(input.equalsIgnoreCase("HELP"))
			{
				help();
			}
			else if(input.equalsIgnoreCase("INSERT"))
			{
				insert();
			}
			else if(input.equalsIgnoreCase("CHANGE"))
			{
				change();
			}
			else if(input.equalsIgnoreCase("CHECK"))
			{
				check();
			}
			else if(input.equalsIgnoreCase("CHECK ALL"))
			{
				check_all();
			}
			else if(input.equalsIgnoreCase("AUDIT"))
			{
				audit();
			}
			else if(input.equalsIgnoreCase("BUY"))
			{
				buy();
			}
			else
			{
			    System.out.println("Unknown input...");
			}
			
			menuMessage();
			input = user_input.nextLine();
		}
		System.out.println("Turning machine off now. Bye.");
		user_input.close();
	}
}
