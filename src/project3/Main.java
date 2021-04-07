package project3;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;

/*
 * COSC 311 Project #3
 * Due March 11, 2021
 * Written by Paul Gudan
 */

/*
   This program simulates a call center modem bank. There are two types of queues for callers,
   a priority queue and a regular queue. The program takes input from the user for the simulation
   length, average time between dial-in attempts, average connection time (call length),
   number of modems in the modem bank, and the size of the waiting queue.

   The time between dial-in attempts and the connection times are determined using a Poisson distribution,
   based on the user averages entered. At the end of the simulation, the results are outputted to the screen
   and to the file report.txt. These results include the simulation parameters, the percentage of modem usage,
   the average wait time, and the number of users in the queue at the end of the simulation.
 */

public class Main {
	public static void main(String[] args)
	{
		// Initialize Variables
		boolean quit = false; // Trigger to exit program
		boolean loop; // Trigger to exit a while loop
		boolean outputFileCreated = false;
		String userInput; // Used to take user input
		char input; // First letter of input
		double simulationLength = 0.0;
		double averageTimeBetweenAttempts = 0.0;
		double averageCallLength = 0.0;
		int numberOfModems = 0;
		int queueSize = 0;
		
		Scanner kbInput = new Scanner(System.in); // Keyboard input scanner
		Random random = new Random();
		
		System.out.println("Thank you for using the Call Center Simulator!");
		System.out.println("");
		while (!quit)
		{
			// Simulation Variables
			
			int currentTime = 1; // Time of running the simulation, starts at 1
			int availableModems = 0; // Number of available modems
			int totalWaitTime = 0;
			int numberOfCallsTaken = 0;
			int callDuration = 0; // Duration of an individual call
			double modemUsagePercentage = 0.0;
			double averageWaitTime = 0.0;
			int numberOfUsersLeft = 0;
			int totalModemUsage = 0;
			String modemUsageRate = "";
			
			loop = false;
			while(!loop) // Loop to get simulation length
			{
				System.out.println("Please enter the length of the simulation: ");
				try
				{
					simulationLength = kbInput.nextDouble();
					if (simulationLength <= 0)
					{
						System.out.println("Input error, input must be positive. Please try again.");
					}
					else
					{
						loop = true;
					}
				}
				catch (InputMismatchException e)
				{
					System.out.println("Input error, expected a number. Please try again.");
					kbInput.nextLine(); // Clear input for next input
				}
			} // End while loop to get simulation length
			
			loop = false;
			while(!loop) // Loop to get average time between dial-in attempts
			{
				System.out.println("Please enter the average time between calls: ");
				try
				{
					averageTimeBetweenAttempts = kbInput.nextDouble();
					if (averageTimeBetweenAttempts <= 0)
					{
						System.out.println("Input error, input must be positive. Please try again.");
					}
					else
					{
						loop = true;
					}
				}
				catch (InputMismatchException e)
				{
					System.out.println("Input error, expected a number. Please try again.");
					kbInput.nextLine(); // Clear input for next input
				}
			} // End while loop for average time between calls
			
			loop = false;
			while(!loop) // Loop to get average call duration
			{
				System.out.println("Please enter the average call duration: ");
				try
				{
					averageCallLength = kbInput.nextDouble();
					if (averageCallLength <= 0)
					{
						System.out.println("Input error, input must be positive. Please try again.");
					}
					else
					{
						loop = true;
					}
				}
				catch (InputMismatchException e)
				{
					System.out.println("Input error, expected a number. Please try again.");
					kbInput.nextLine(); // Clear input for next input
				}
			} // End while loop for average call duration
			
			loop = false;
			while(!loop) // Loop to get number of modems
			{
				System.out.println("Please enter the number of modems in the modem bank: ");
				try
				{
					numberOfModems = kbInput.nextInt();
					if (numberOfModems <= 0)
					{
						System.out.println("Input error, input must be positive. Please try again.");
					}
					else
					{
						loop = true;
					}
				}
				catch (InputMismatchException e)
				{
					System.out.println("Input error, expected an integer. Please try again.");
					kbInput.nextLine(); // Clear input for next input
				}
			} // End while loop to get number of modems
			
			loop = false;
			while(!loop) // Loop to get waiting queue size (-1 for infinite queue)
			{
				System.out.println("Please enter the waiting queue size (enter -1 for infinite queue): ");
				try
				{
					queueSize = kbInput.nextInt();
					if (queueSize <= 0)
					{
						System.out.println("Input error, input must be positive. Please try again.");
					}
					else
					{
						loop = true;
					}
				}
				catch (InputMismatchException e)
				{
					System.out.println("Input error, expected an integer. Please try again.");
					kbInput.nextLine(); // Clear input for next input
				}
			} // End while loop to get waiting queue size

			Priority<Priority.Node> priorityEvents = new Priority<Priority.Node>(); // Create singly linked list
			
			for (currentTime = 1; currentTime <= simulationLength; currentTime++) // Poisson distribution for the number of calls for each time unit
			{
				int callsPerTimeUnit = -1;
				for(double i = 0.0; i < 1.0; i += -Math.log(random.nextDouble()) * averageTimeBetweenAttempts)
				{
					callsPerTimeUnit++;
				}
				
				if (callsPerTimeUnit > 0)
				{
					for (int j = 0; j < callsPerTimeUnit; j++)
					{
						priorityEvents.append(priorityEvents, currentTime, EventType.DialIn);
					}
				}
			}
			
			Regular regularEvents = new Regular(queueSize);
			availableModems = numberOfModems;
			priorityEvents.append(priorityEvents, (int) (simulationLength + 20), EventType.DialIn);
			
			for (currentTime = 1; currentTime <= simulationLength; currentTime++) // For loop for each time unit for simulation
			{
				while(priorityEvents.getFirstTime(priorityEvents) == currentTime) // Priority events
				{
					if (priorityEvents.getFirstType(priorityEvents) == EventType.DialIn) // Dial In event
					{
						regularEvents.add(currentTime);
					}
					else // Hang Up event
					{
						availableModems++;
					}
					priorityEvents.removeFirst(priorityEvents);
				}
				
				while ((availableModems > 0) && (regularEvents.getFirst() != -1)) // Available modem and waiting caller
				{
					totalWaitTime += (currentTime - regularEvents.getFirst());
					regularEvents.remove();
					
					callDuration = -1;
					for (double j = 0.0; j < 1.0; j += -Math.log(random.nextDouble()) / averageCallLength)
					{
						callDuration++;
					}
					
					priorityEvents.add(priorityEvents, currentTime + callDuration, EventType.HangUp);
					availableModems--;
					numberOfCallsTaken++;
					
					if (currentTime + callDuration <= simulationLength)
					{
						totalModemUsage += callDuration;
					}
					else
					{
						totalModemUsage += (simulationLength - currentTime);
					}
				}
			} // End simulation for loop
			
			while (regularEvents.getFirst() != -1)
			{
				numberOfUsersLeft++;
				regularEvents.remove();
			}
			
			// Calculate Results
			try
			{
				averageWaitTime = totalWaitTime / numberOfCallsTaken;
			}
			catch (ArithmeticException e)
			{
				averageWaitTime = 0.0;
			}
			modemUsagePercentage = totalModemUsage / (numberOfModems * simulationLength) * 100;
			
			// Display Simulation Results
			System.out.println();
			System.out.println("-- Simulation Results --");
			System.out.printf("Modem usage percentage: ");
			System.out.printf("%.2f", modemUsagePercentage);
			System.out.println();
			System.out.printf("Average wait time: ");
			System.out.printf("%.2f", averageWaitTime);
			System.out.println();
			System.out.println("Users in queue at end of simulation: " + numberOfUsersLeft);
			
			if (!outputFileCreated) // Create new output file
			{
				try
				{
					FileWriter fileWriter = new FileWriter("report.txt");
					PrintWriter outputFile = new PrintWriter(fileWriter);
					
					outputFile.println("Simulation Length     Average Time Between Calls     Average Call Duration     Number of Modems     Size of Queue     "
							+ "Modem Usage Percentage     Average Wait Time     Waiting Users at End");
					
					outputFile.close();
				}
				catch (IOException e)
				{
					System.out.println("Error creating report file");
					e.printStackTrace();
				}
			}
			try
			{
				FileWriter fileWriter = new FileWriter("report.txt", true);
				PrintWriter outputFile = new PrintWriter(fileWriter);

				outputFile.printf("     ");
				outputFile.print(simulationLength);
				outputFile.print("                        ");
				outputFile.printf("%.2f", averageTimeBetweenAttempts);
				outputFile.printf("                        ");
				outputFile.printf("%.2f", averageCallLength);
				outputFile.print("                     ");
				outputFile.print(numberOfModems);
				outputFile.print("                   ");
				outputFile.print(queueSize);
				outputFile.print("                    ");
				outputFile.printf("%.2f", modemUsagePercentage);
				outputFile.printf("                    ");
				outputFile.printf("%.2f", averageWaitTime);
				outputFile.printf("                      ");
				outputFile.print(numberOfUsersLeft);
				outputFile.println();

				outputFile.close();
			}
			catch (IOException e)
			{
				System.out.println("Error creating report file");
				e.printStackTrace();
			}
			
			System.out.println();
			System.out.println("Simulation is complete. Summary in file report.txt");
			System.out.println();
			
			loop = false;
			while (!loop) // Loop to get valid user input
			{
				System.out.println("Would you like to run another simulation? (Y/N): ");
				userInput = kbInput.next().toLowerCase();
				input = userInput.charAt(0);

				switch(input)
				{
				case 'y': // Yes
					System.out.println("");
					loop = true;
					break;
				case 'n': // No
					quit = true;
					loop = true;
					break;
				default: // Any other input
					System.out.println("Unrecognized input. Please enter Y for yes or N for no: ");
					break;
				}
			}
		} // End main while loop
		
		System.out.println("Thank you for using the Call Center Simulator. Have a good day! :)");
		kbInput.close();
	}
}