import java.util.Scanner;
import java.util.Random;
public class ShutTheBox {
   //This method contains the entire program
   public static void main(String[] args) {
      //Instantiate Scanner object
      Scanner in = new Scanner(System.in);
      
      //Define initial match conditions, then print welcome message
      boolean okToGo = true;
      int checkAll = 0; //used in tile error detection
      int game = 1;
      int seed = 0;
      int sum = 0;
      int sumRoll = 0;
      int wins = 0;
      String runShutBox = "yes";
      System.out.println("Welcome to the 'Shut the Box' Game.\n");
      
      //OUTER-LOOP, contains full game (except title screen)
      do {
         //Initialize and set NEW GAME variables
         okToGo = true;
         boolean error = true;
         String seedString = "";
         boolean[] status = {true, true, true, true, true, true, true, true,
            true};
         System.out.println("\nBegin playing game #" + game + ".");
         
         //Input seed and create random object including error detection
         do {
            System.out.println("Enter a seed:");
            seedString = in.nextLine();
            for (int i = 0; i < seedString.length(); i++) {
               if (!(((seedString.charAt(i) > 47) && (seedString.charAt(i) < 58)
                  ) || seedString.charAt(i) == 45) || seedString.indexOf(".") >=
                  0) {
                  System.out.println("Error! The entry \"" + seedString + "\"" +
                     " is not an integer.");
                  break;
               }  else {
                  //Extra check for letters inside String
                  for (int j = seedString.length() - 1; j >= 0; j--) {
                     if (!((seedString.charAt(j) > 47 && seedString.charAt(j) <
                         58) || seedString.charAt(j) == 45)) {
                        break;
                     }  else if (j == 0) {
                        seed = Integer.parseInt(seedString);
                        error = false;
                     }
                  }
               }
            }
         } while (error == true);
         Random rand = new Random(seed);
         
         //INNER-LOOP, contains the full game (except title, and seed input)
         while (okToGo == true) {
            //Check if game is complete before next roll is made
            int allTiles = 0;
            for (int count = 0; count < status.length; count++) {
               if (status[count] == false) {
                  allTiles++;
               }
            }
            if (allTiles == 9) {
               break;
            }
         
            //If statement in place in case of error
            if (checkAll == 0) {
               System.out.print("\nThe available number(s) are: ");
               for (int i = 0; i < status.length; i++) {
                  if (status[i] == true) {
                     System.out.print((i + 1) + " ");
                  }
               }
            
               //Roll the dice (1 or 2) conditions and input
               if (status[6] == true || status[7] == true || status[8] == true){
                  int firstRoll = rand.nextInt(6) + 1;
                  int secondRoll = rand.nextInt(6) + 1;
                  sumRoll = firstRoll + secondRoll;
                  System.out.println("\n\nThe dice roll is " + firstRoll + " " +
                     "and " + secondRoll + " for a sum of " + sumRoll + ".");
               }  else {
                  System.out.println("\n\nThe player may opt to roll 1 die or" +
                     " 2 dice because tiles 7, 8, and 9 are \"closed\".\nEnte" +
                     "r \"1\" to roll 1 only die or \"2\" (or any other key) " +
                     "to roll 2 dice.");
                  String numberOfDice = in.nextLine();
                  if (numberOfDice.equals("1")) {
                     sumRoll = rand.nextInt(6) + 1;
                     System.out.println("\nThe die roll is " + sumRoll + ".");
                  }  else {
                     int firstRoll = rand.nextInt(6) + 1;
                     int secondRoll = rand.nextInt(6) + 1;
                     sumRoll = firstRoll + secondRoll;
                     System.out.println("\nThe dice roll is " + firstRoll +
                        " and " + secondRoll + " for a sum of " + sumRoll + ".");
                  }
               }
            }
            
            //Checks dice roll conditions before continuing, or ending game
            okToGo = ShutTheBox_HelperClass.continuePlaying(status, sumRoll);
            
            //Set tile conditions
            boolean tileError = true;
            int tileSum = 0;
            int restart = 0;
            checkAll = 0;
            char tile = ' ';
            
            //Error detection
            if (okToGo == true) {
               while (tileError == true) {
                  System.out.println("Which tile(s) should be \"closed\"? Sep" +
                     "arate multiple tiles with a space.");
                  tileSum = 0;
                  restart = 0;
                  String tiles = in.nextLine();
                  for (int i = 0; i < tiles.length(); i++) {
                     tile = tiles.charAt(i);
                     if (!(tile > 48 && tile < 58 || tile == 32)) {
                        System.out.println("Error! The entry \"" + tiles + "\""+
                           " is not a single-digit!");
                        checkAll++;
                        tileSum = -1;
                        break;
                     }  else if (tile > 48 && tile < 58) { 
                        tileSum = tileSum + tile - '0';
                     }
                  }
                  
                  if (tileSum == -1) {
                     break;
                  }  else if (tileSum < sumRoll) {
                     System.out.println("Error! The selected tile(s) (" + tiles+ 
                        ") that sum to " + tileSum + " does not exhaust the to"+
                        "tal die/dice roll (" + sumRoll + ").");
                     checkAll++;
                     break;
                  }  else if (tileSum > sumRoll) {
                     System.out.println("Error! The selected tile(s) (" + tiles+ 
                        ") that sum to " + tileSum + " exceeds the total die/d"+
                        "ice roll (" + sumRoll + ").");
                     checkAll++;
                     break;
                  }  else if (tileSum == sumRoll) {
                     for (int i = 0; i < tiles.length(); i++) {
                        tile = tiles.charAt(i);
                        if (tile != 32 && (status[tile - '1'] == false)) {
                           System.out.println("Error! Not all of the selected "+
                              "tile(s) are available to \"close\"!");
                           checkAll++;
                           restart = 1;
                           break;
                        }
                     }
                     for (int i = 0; i < tiles.length(); i++) {
                        if (restart == 1) {
                           break;
                        }
                        tile = tiles.charAt(i);
                        if (tile > 48 && tile < 58) {
                           status[tile - '1'] = false;
                        }
                     }
                     restart = 0;
                  }
                  tileError = false;
               }
            }  else {
               break;
            }
         }
         
         //Check for win/lose condition
         boolean gameSetWin = true;
         int loseSum = 0;
         for (int j = 0; j < status.length; j++) {
            if (status[j] == true) {
               gameSetWin = false;
               loseSum = loseSum + j + 1;
            }
         }
         
         
         //Calculate point sum then display game results
         sum = sum + loseSum;
         if (gameSetWin == true) {
            wins++;
            System.out.println("Congratulations, you won!\n");
         }  else {
            System.out.println("You lost with " + loseSum + " points.\n");
         }
         
         //Offer to play again
         System.out.println("\nDo you wish to play again? Enter \"Yes\" to pla" +
            "y again or anything else to quit.");
         runShutBox = in.nextLine();
         
         //Increase game number for next game
         if (runShutBox.equalsIgnoreCase("yes")) {
            game++;
         }
      }  while (runShutBox.equalsIgnoreCase("yes"));
      
      //Closing message + results
      System.out.println("\nThank you for playing \"Close the Box\"! The tota" +
         "l score is " + sum + " with " + wins + " win(s) after " + game + 
         " round(s).");
      
      //Close scanner
      in.close();
   }
}
