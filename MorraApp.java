import java.util.Scanner;

public class MorraApp {

  public static void main(String args[]) {

    // DECLARE AND CREATE VARIABLES
    // Variables used as sentinels
    boolean gameState = true;
    boolean gameContinue = true;
    boolean teamSelection = false;
    boolean numberSelection = false;
    int continueDecision = -1;

    // Variables to store user inputs
    String userTeam = "";
    int userNumber = -1;

    // Variables used as counters for rounds and games
    int roundCounter = 0;
    int gameCounter = 0;

    // Create instances of classes needed
    Morra myObj = new Morra();
    Scanner input = new Scanner(System.in);

    // Play Game while True
    while (gameState) {

      // New Round Announcement
      System.out.println("\nRound " + (roundCounter + 1) + " is about to begin!");

      // Add a 1.5 second delay when playing the game
      try {
        Thread.sleep(1500);
      } catch (InterruptedException error) {
        error.printStackTrace();
      }

      // Select team
      while (!teamSelection) {
        System.out.println("Do you want to play odds or evens?");
        userTeam = input.nextLine();
        teamSelection = myObj.setTeam(userTeam);
      }

      // Select number/finger
      while (!numberSelection) {
        System.out.println("Please select a number between 1 and 10.");
        // Avoid the buffer issue with nextInt() and the line break
        try {
            userNumber = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException error) {
          System.out.println("Incorrect choice");
        }
        numberSelection = myObj.setNumber(userNumber, roundCounter);
      }

      // Invoke the processing method
      myObj.play();

      // Display ongoing scores
      myObj.displayScore();

      // Update the history occuring at the end of each game (history of fingers updated during selection)
      myObj.updateScoreHistory(roundCounter);

      // Check if the game continues for additional rounds (i.e. if there's a winner)
      gameContinue = myObj.checkScore();

      if (!(gameContinue)) {
        // Display the 2 history elements at the end of a game
        myObj.displayScoreHistory(roundCounter, gameCounter);
        myObj.displayFingerHistory(roundCounter, gameCounter);

        // Update the 4 history elements at the end of all games
        myObj.updateGameHistory(gameCounter);
        myObj.updateRoundWonLostHistory(gameCounter);
        myObj.updateOddsEvensHistory(gameCounter);
        myObj.updateAddPointsHistory(gameCounter);

        // Ask the player whether he wants to stop playing at the end of each game
        while(continueDecision != 0 && continueDecision != 1){
          System.out.println("\nIf you want to play another game, press 1.\nIf you want to quit the game, press 0.");
          try {
              continueDecision = Integer.parseInt(input.nextLine());
          } catch (NumberFormatException error) {
              System.out.println("Incorrect choice");
          }
        }

        // If the player quits, the whole game stops and all the history elements are displayed
        if (continueDecision == 0) {
          System.out.println("Thank You for Playing!");
          gameState = false; // Make the main game loop stop
          myObj.displayGameHistory(gameCounter); // Display history of games won and lost
          myObj.displayRoundWonLostHistory(gameCounter); // Display number of rounds won and lost per game
          myObj.displayOddsEvensHistory(gameCounter); // Display number of Odds and Evens per game
          myObj.displayAddPointsHistory(gameCounter); // Display number of additional points

        } else {
          // Reset Sentinels/Variables and update counters for next game
          roundCounter = 0;
          gameContinue = true;
          gameCounter++;
          teamSelection = false;
          numberSelection = false;
          userTeam = "";
          userNumber = -1;
          continueDecision = -1;
        }
      } else {
        // Reset Sentinels/Variables and update counters for next round
        roundCounter++;
        teamSelection = false;
        numberSelection = false;
        userTeam = "";
        userNumber = -1;
      }
    }
  }
}
