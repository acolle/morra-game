import java.util.Random;

public class Morra {

  // INSTANCE VARIABLES
  // Instance variables for team selection
  private String playerTeam;
  private String computerTeam;

  // Instance variables for number/finger selection
  private int[] authorisedNumberValues = {1,2,3,4,5,6,7,8,9,10};
  private int playerNumber = -1; // Use a default value for validation in the setNumber method
  private int computerNumber;

  // Instance variables to store scores and result
  private int playerScore;
  private int computerScore;
  private int result;

  // Instance variables to update the different arrays used for history
  private int playerRoundWon = 0;
  private int playerRoundLost = 0;
  private int playerOdds = 0;
  private int playerEvens = 0;
  private int computerOdds = 0;
  private int computerEvens = 0;
  private int additionalPointsPlayer = 0;
  private int additionalPointsComputer = 0;

  // Instance variables used to update history of games won and lost
  private boolean playerWinner = false;
  private boolean computerWinner = false;
  private boolean gameIsDraw = false;

  // Arrays used to store history elements displayed at the end of a game
  private int[][] fingerHistory = new int[3][4]; // Store the history of fingers selected
  private int[][] scoreHistory = new int[3][4]; // Store the history of scores

  // Arrays used to store history elements displayed at the end of all games
  private String[][] gameHistory = new String[3][50];
  private int[][] roundWonLostHistory = new int[3][50];
  private int[][] oddsEvensHistory = new int[5][50];
  private int[][] addPointsHistory = new int[3][50];


  // Create instance of the Random class
  Random randomObject = new Random();

  // SET METHODS - INPUTS
  // Team Selection
  public boolean setTeam(String userTeam) {

    boolean teamSelection = false;

    if (userTeam.equalsIgnoreCase("odds")) {
      playerTeam = userTeam;
      computerTeam = "evens";
      playerOdds += 1;
      computerEvens += 1;
      teamSelection = true;
    } else if (userTeam.equalsIgnoreCase("evens")) {
      playerTeam = userTeam;
      computerTeam = "odds";
      playerEvens += 1;
      computerOdds += 1;
      teamSelection = true;
    } else {
      System.out.println("Please enter a correct choice. [odds] or [evens] only.");
    }
    // Return True if one of the two correct teams has been selected
    return teamSelection;
  }

  // Finger/Number Selection
  public boolean setNumber(int userNumber, int round) {

    boolean numberSelection = false;

    // Check if number has been correctly selected
    for (int i = 0; i < authorisedNumberValues.length; i++) {
      if (userNumber == authorisedNumberValues[i]) {
        playerNumber = userNumber;
        computerNumber = randomObject.nextInt(9) + 1; // Exclude 0 which is included by default while maintaining an uniform distribution

        if (playerNumber != computerNumber) {
          // Store the fingers/numbers selected
          fingerHistory[0][round] = round + 1;
          fingerHistory[1][round] = playerNumber;
          fingerHistory[2][round] = computerNumber;
          numberSelection = true;
        } else {
          // If player and computer chose the same number, additional points cannot be given
          // To solve this, the player is prompted to choose another (or the same) number
          System.out.println("Oops it seems that your opponent and you have chosen the same number, please choose again.");
          numberSelection = false;
        }
      }
    }
    if (playerNumber == -1){
      System.out.println("You can only select a number comprised between 1 and 10.");
    }
    return numberSelection;
  }

  // PROCESS
  // Compute Result
  public void computeResult() {
    result = playerNumber + computerNumber;
  }

  // Main Game Dynamics - Compute result and determine winner from inputs
  public void play(){
    computeResult();

    // Add a 1.5 second delay
    try {
      Thread.sleep(1500);
    } catch (InterruptedException error) {
      error.printStackTrace();
    }

    System.out.println("\nYou are playing " + playerTeam + " and you chose " + playerNumber);
    System.out.println("The computer plays " + computerTeam + " and chose " + computerNumber);
    System.out.println("The result is " + result);

    // Main Points
    if (result % 2 == 1) {
      // When result is odds
      if (playerTeam.equalsIgnoreCase("odds")) {
        playerScore += 2;
        playerRoundWon += 1;
        System.out.println("\nCongrats! " + result + " is odd. You win 2 points.");
      } else {
        computerScore += 2;
        playerRoundLost += 1;
        System.out.println("\nSorry, " + result + " is odd. Computer wins 2 points.");
      }
    } else {
      // When result is even
      if (playerTeam.equalsIgnoreCase("evens")) {
        playerScore += 2;
        playerRoundWon += 1;
        System.out.println("\nCongrats! " + result + " is even. You win 2 points.");
      } else {
        computerScore += 2;
        playerRoundLost += 1;
        System.out.println("\nSorry, " + result + " is even. Computer wins 2 points.");
      }
    }

    // Additional Points
    if (result - playerNumber < result - computerNumber) {
      playerScore += 1;
      additionalPointsPlayer += 1;
      System.out.println("Also, " + playerNumber + " is closer to " + result + " than " + computerNumber + " is. You win 1 extra point.");
    } else {
      computerScore += 1;
      additionalPointsComputer += 1;
      System.out.println("Also, " + computerNumber + " is closer to " + result + " than " + playerNumber + " is. The computer wins 1 extra point.");
    }

  }

  // Display ongoing scores
  public void displayScore(){
    System.out.println();
    System.out.println("Your current score: " + playerScore);
    System.out.println("Computer's score: " + computerScore);
  }

  // Check if the game continues for additional rounds (i.e. if there's a winner)
  public boolean checkScore(){

    boolean gameContinue = true;

    if (playerScore >= 6 && computerScore >= 6){
      System.out.println("The game is a draw! You both win or you both lose, as you wish.");
      gameIsDraw = true; // Allows to track the games that are draw
      playerScore = computerScore = 0; // Reset scores
      gameContinue = false; // Allows the App to finish the current game when false
    } else if (playerScore >= 6){
      System.out.println("Congrats! You win the game with " + playerScore + " points.");
      playerWinner = true; // Allows to track wins and losses
      playerScore = computerScore = 0;
      gameContinue = false;
    } else if (computerScore >= 6) {
      System.out.println("You lose! The computer wins the game with " + computerScore + " points.");
      computerWinner = true;
      playerScore = computerScore = 0;
      gameContinue = false;
    }
      return gameContinue;
  }


  // HISTORY - END OF A GAME
  // Update history of scores
  public void updateScoreHistory(int round){
    scoreHistory[0][round] = round + 1;
    scoreHistory[1][round] = playerScore;
    scoreHistory[2][round] = computerScore;
  }

  // Display history of scores at the end of each game
  public void displayScoreHistory(int roundCounter, int gameCounter){

    // // Traditional Display
    // System.out.println();
    // System.out.println("Scores for Game " + (gameCounter + 1));
    // for (int i = 0; i < scoreHistory.length; i++){
    //   for (int j = 0; j < roundCounter+1; j++) {
    //     System.out.print(scoreHistory[i][j]);
    //   }
    //   System.out.println();
    // }

    // Table Display
    System.out.println("\nScores for Game " + (gameCounter + 1));
		System.out.printf("Round\t\tPlayer\t\tComputer\n");
		for (int j = 0; j < roundCounter+1; j++ ) {
			System.out.printf("%d\t\t%d\t\t%d\n", scoreHistory[0][j], scoreHistory[1][j], scoreHistory[2][j]);
		}
  }

  // Display history of fingers at the end of each game
  public void displayFingerHistory(int roundCounter, int gameCounter){

    // // Traditional Display
    // System.out.println();
    // System.out.println("Fingers Selected for Game " + (gameCounter + 1));
    // for (int i = 0; i < fingerHistory.length; i++){
    //   for (int j = 0; j < roundCounter+1; j++) {
    //     System.out.print(fingerHistory[i][j]);
    //   }
    //   System.out.println();
    // }

    // Table Display
    System.out.println("\nFingers Selected for Game " + (gameCounter + 1));
		System.out.printf("Round\t\tPlayer\t\tComputer\n");
		for (int j = 0; j < roundCounter+1; j++ ) {
			System.out.printf("%d\t\t%d\t\t%d\n", fingerHistory[0][j], fingerHistory[1][j], fingerHistory[2][j]);
		}
  }


  // HISTORY - END OF ALL GAMES
  // Update history of games won and lost
  public void updateGameHistory(int gameCounter){
    // Specifiy the Round with String formatting
    gameHistory[0][gameCounter] = String.format("%d", gameCounter + 1);

    if (gameIsDraw){
      gameHistory[1][gameCounter] = "D";
      gameHistory[2][gameCounter] = "D";
      gameIsDraw = false;
    } else if (playerWinner) {
      gameHistory[1][gameCounter] = "W";
      gameHistory[2][gameCounter] = "L";
      playerWinner = false;
    } else if (computerWinner) {
      gameHistory[1][gameCounter] = "L";
      gameHistory[2][gameCounter] = "W";
      computerWinner = false;
    }
  }

  // Update history of rounds won and lost
  public void updateRoundWonLostHistory(int gameCounter){
    roundWonLostHistory[0][gameCounter] = gameCounter + 1;
    roundWonLostHistory[1][gameCounter] = playerRoundWon;
    roundWonLostHistory[2][gameCounter] = playerRoundLost;
    playerRoundWon = playerRoundLost = 0; // Reset rounds won and lost
  }

  // Update history of odds and evens selected
  public void updateOddsEvensHistory(int gameCounter){
    oddsEvensHistory[0][gameCounter] = gameCounter + 1;
    oddsEvensHistory[1][gameCounter] = playerOdds;
    oddsEvensHistory[2][gameCounter] = playerEvens;
    oddsEvensHistory[3][gameCounter] = computerOdds;
    oddsEvensHistory[4][gameCounter] = computerEvens;
    playerOdds = playerEvens = computerOdds = computerEvens = 0;
  }

  // Update history of additional points
  public void updateAddPointsHistory(int gameCounter){
    addPointsHistory[0][gameCounter] = gameCounter + 1;
    addPointsHistory[1][gameCounter] = additionalPointsPlayer;
    addPointsHistory[2][gameCounter] = additionalPointsComputer;
    additionalPointsPlayer = additionalPointsComputer = 0;
  }


  // Display history of games won and lost
  public void displayGameHistory(int gameCounter){

    // // Traditional Display
    // System.out.println("History of Games");
    // for (int i = 0; i < gameHistory.length; i++){
    //   for (int j = 0; j < gameCounter+1; j++) {
    //     System.out.print(gameHistory[i][j]);
    //   }
    //   System.out.println();
    // }

    // Table Display
    System.out.println("\nHistory of Games Played (W: Win - L: Defeat - D: Draw)");
		System.out.printf("Game\t\tPlayer\t\tComputer\n");
		for (int j = 0; j < gameCounter+1; j++ ) {
			System.out.printf("%s\t\t%s\t\t%s\n", gameHistory[0][j], gameHistory[1][j], gameHistory[2][j]);
		 }
  }

  // Display number of rounds won and lost per game
  public void displayRoundWonLostHistory(int gameCounter){

    // // Traditional Display
    // System.out.println("History of Rounds Won and Lost");
    // for (int i = 0; i < roundWonLostHistory.length; i++){
    //   for (int j = 0; j < gameCounter+1; j++) {
    //     System.out.print(roundWonLostHistory[i][j]);
    //   }
    //   System.out.println();
    // }

    // Table Display
    System.out.println("\nHistory of Number of Rounds Won and Lost by the Player");
		System.out.printf("Game\t\tRound Won\tRound Lost\n");
		for (int j = 0; j < gameCounter+1; j++ ) {
			System.out.printf("%d\t\t%d\t\t%d\n", roundWonLostHistory[0][j], roundWonLostHistory[1][j], roundWonLostHistory[2][j]);
		}
  }

  // Display number of Odds and Evens per game
  public void displayOddsEvensHistory(int gameCounter){

    // // Traditional Display
    // System.out.println("History of Odds and Evens Selected");
    // for (int i = 0; i < oddsEvensHistory.length; i++){
    //   for (int j = 0; j < gameCounter+1; j++) {
    //     System.out.print(oddsEvensHistory[i][j]);
    //   }
    //   System.out.println();
    // }

    // Table Display
    System.out.println("\nHistory of Number of Odds and Evens Selected");
		System.out.printf("Game\t\tPlayer Odds\tPlayer Evens\tComputer Odds\tComputer Evens\n");
		for (int j = 0; j < gameCounter+1; j++ ) {
			System.out.printf("%d\t\t%d\t\t%d\t\t%d\t\t%d\n", oddsEvensHistory[0][j], oddsEvensHistory[1][j], oddsEvensHistory[2][j], oddsEvensHistory[3][j], oddsEvensHistory[4][j]);
		}
  }

  // Display number of additional points
  public void displayAddPointsHistory(int gameCounter){

    // // Traditional Display
    // System.out.println("History of Additional Points");
    // for (int i = 0; i < addPointsHistory.length; i++){
    //   for (int j = 0; j < gameCounter+1; j++) {
    //     System.out.print(addPointsHistory[i][j]);
    //   }
    //   System.out.println();
    // }

    // Table Display
    System.out.println("\nHistory of Additional Points received per Game");
		System.out.printf("Game\t\tPlayer\t\tComputer\n");
		for (int j = 0; j < gameCounter+1; j++ ) {
			System.out.printf("%d\t\t%d\t\t%d\n", addPointsHistory[0][j], addPointsHistory[1][j], addPointsHistory[2][j]);
		}
  }
}
