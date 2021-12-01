import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Made by Koy Wilson on December 1st, 2021.
 */
public class MainCode 
{
	// Create private instance variable to make it easier to read.
	private final static String DATA = "Datasets/shots_data.csv";
	private final static int TEAM_ELEMENT = 0;
	private final static int X_ELEMENT = 1;
	private final static int Y_ELEMENT = 2;
	private final static int SHOT_MADE_ELEMENT = 3;
	private final static int X_THRESHOLD = 22;
	private final static double Y_THRESHOLD = 7.8;

	// Create ArrayLists to store the shots for Team A, shots for Team B, and information for both.
	private static ArrayList<String> teamAShots = new ArrayList<String>();
	private static ArrayList<String> teamBShots = new ArrayList<String>();
	private static ArrayList<String[]> shotInformation = new ArrayList<String[]>();
	
	// Main method to calculate shot information and output to the user.
	public static void main(String[] args) throws FileNotFoundException
	{
		// Seriously, Josh Giddy is hilarious online. Dude is a goofball.
		System.out.println("Please tell Josh Giddey he is my favorite TikToker, Thank you.\n");
		readFile();
		assignShots();
		printTeamAShots();
		printTeamBShots();
	}
	
	// Method to assign shot information to teamAShots and teamBShots
	public static void assignShots()
	{
		// For each loop to go through each element of shotInformation
		for(String[] currentShot : shotInformation)
		{
			// Parse the elements of the String[] into Doubles and Integers.
			Double xCoordinate = Double.parseDouble(currentShot[X_ELEMENT]);
			Double yCoordinate = Double.parseDouble(currentShot[Y_ELEMENT]);
			Integer isMade = Integer.parseInt(currentShot[SHOT_MADE_ELEMENT]);
			
			// If the first element of the String[] has an A,
			// Assign the shot into the ArrayList for Team A.
			if(currentShot[TEAM_ELEMENT].equals("A"))
			{
				teamAShots.add(shot(xCoordinate, yCoordinate, isMade));
			}
			// Otherwise, assign the shot into the ArrayList for Team B.
			else
			{
				teamBShots.add(shot(xCoordinate, yCoordinate, isMade));
			}
		}
	}
	
	public static void printTeamAShots()
	{
		// Create a double[] of the array returned from shotsPerZone for Team A.
		double[] shotSelection = shotsPerZone(teamAShots);
		
		// Output the information for Team A to the user.
		System.out.println("The statistics for Team A:");
		System.out.println("The percentage of two pointers attempted: " + shotSelection[0] + "%.");
		System.out.println("The percentage of corner threes attempted: " + shotSelection[1] + "%.");
		System.out.println("The percentage of noncorner threes attempted: " + shotSelection[2] + "%.");
		System.out.println("The overall effective field goal percentage: " + getEffectiveFG(teamAShots) + "%.\n");
	}
	
	public static void printTeamBShots()
	{
		// Create a double[] of the array returned from shotsPerZone for Team B.
		double [] shotSelection = shotsPerZone(teamBShots);
		
		// Output the information for Team B to the user.
		System.out.println("The statistics for Team B:");
		System.out.println("The percentage of two pointers attempted: " + shotSelection[0] + "%.");
		System.out.println("The percentage of corner threes attempted: " + shotSelection[1] + "%.");
		System.out.println("The percentage of noncorner threes attempted: " + shotSelection[2] + "%.");
		System.out.println("The overall effective field goal percentage: " + getEffectiveFG(teamBShots) + "%.");
	}
	
	public static double[] shotsPerZone(ArrayList<String> input)
	{
		// Create a new double array of three elements.
		double[] returnedArray = new double[3];
		
		// Create accumulators for each zone.
		double numTwos = 0.0;
		double numCornerThrees = 0.0;
		double numNonCornerThrees = 0.0;
		
		// Enhanced for loop to go through each element of input.
		for(String currentShot : input)
		{
			// If the String contains "NC3" then it is a non corner three.
			if(currentShot.contains("NC3"))
			{
				++numNonCornerThrees;
			}
			// If the String contains "2PT" then it is a two pointer.
			else if(currentShot.contains("2PT"))
			{
				++numTwos;
			}
			// Otherwise, the shot selected was a corner three.
			else
			{
				++numCornerThrees;
			}
		}
		
		// Set each element of the array into a percentage.
		returnedArray[0] = numTwos / input.size() * 100;
		returnedArray[1] = numNonCornerThrees / input.size() * 100;
		returnedArray[2] = numCornerThrees / input.size() * 100;
		
		// Return the array
		return returnedArray;
	}
	
	public static double getEffectiveFG(ArrayList<String> input)
	{
		// Create variables for the total shots, threes made, and twos made.
		int totalShots = input.size();
		int threesMade = 0;
		int twosMade = 0;
		
		// Enhance for loop to go through each element of the ArrayList.
		for(String currentShot : input)
		{
			// First, check if the shot selected was a three pointer.
			if(currentShot.contains("3"))
			{
				// If the String contains a 1, then the shot was made.
				// Meaning to increment threesMade.
				if(currentShot.contains("1"))
				{
					++threesMade;
				}
			}
			// Otherwise, a two pointer was shot.
			else
			{
				// If the String contains a 1, then the shot was made.
				// Meaning to increment twosMade.
				if(currentShot.contains("1"))
				{
					++twosMade;
				}
			}
		}
		
		// Calculate the numerator based on the equation in the PDF.
		double numerator = twosMade + (0.5 + threesMade);
		
		// Return the numerator divided by the total shots. Multipled by 100 for a percentage.
		return numerator / totalShots * 100;
	}
	
	public static void readFile() throws FileNotFoundException
	{
		// Create a Scanner to read from the shots file and add a comma Delimiter
		Scanner input = new Scanner(new File(DATA));
		
		// Skip the first line of the file
		input.nextLine();
		
		// While loop to go through each line of the shots_data.csv
		while(input.hasNextLine())
		{
			input.next();
			shotInformation.add(input.next().split(","));
			input.nextLine();
		}
		
		input.close();
	}
	
	// Calculates the distance with two given points.
	public static double findDistance(double x, double y)
	{
		// Return the distance of the origin in comparison to the origin.
		return (double)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public static String shot(double x, double y, int isMade)
	{
		// Get the absolute value of the coordinates in case they're on the left side of the y axis.
		double actX = Math.abs(x);
		double actY = Math.abs(y);
		
		// If the y is less than 7.8, determine if it is a corner three.
		if(actY <= Y_THRESHOLD)
		{
			// If the x is greater than 22, it is a corner three and return the info.
			if(actX > X_THRESHOLD)
			{
				return "C3" + isMade;
			}
			else // Otherwise, the shot selected was a two pointer.
			{
				return "2PT" + isMade;
			}
		}
		else
		{
			// Find if the distance from the origin to the x and y is greater than 23.75
			// If it is, return that a non corner three was taken.
			if(findDistance(actX, actY) > 23.75)
			{
				return "NC3" + isMade;
			}
			else // Otherwise, the shot selected was a two pointer.
			{
				return "2PT" + isMade;
			}
		}
	}
}
