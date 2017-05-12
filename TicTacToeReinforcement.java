package NEURAL_NETWORKS;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TicTacToeReinforcement {

	/*Tic-Tac-Toe Reinforcement

	We will use a 9 integer array to represent the state of the board. 1 stands for computer (CPU), 0 for blank, -1 for human
	i.e. {-1,1,1,-1,1,-1,0,1,0}

	THIS REPRESENTS:

	  O |  X  |  X
	----------------
	  O |  X  |  O
	----------------
	    |  O  |  

	The compActions possible will be represented by an array of numbers. If the board is represented by thus:

	  0 |  1  |  2
	----------------
	  3 |  4  |  5
	----------------
	  6 |  7  |  8

	 Then the possible moves would be {6,8}

	Essentially the computer will have a neural network with 9 fixed outputs from 0 to 1, which represent the confidence/probability
	of putting a piece down in a certain position. It will rank the probabilities accordingly, and then choose the best possible move.

	If the computer loses, then it reverses the pieces and back propagates in order to learn how to play based on what the user does.
	If the computer wins, then it back propagates with those results once again.

	All the states and the good compActions are kept in an ArrayList database. It only takes databases that win. Draws...well...we'll see.

	 */

	private static Scanner a;

	public static void main(String[] args) {

		ArrayList<int[]> compGameLog = new ArrayList<int[]>();
		ArrayList<int[]> compActions = new ArrayList<int[]>();
		ArrayList<int[]> userGameLog = new ArrayList<int[]>();
		ArrayList<int[]> userActions = new ArrayList<int[]>();

		//Training Data
		ArrayList<int[]> inputStates = new ArrayList<int[]>();
		ArrayList<int[]> smartActions = new ArrayList<int[]>();


		//DECLARATION OF NEURAL NETWORK
		int hiddenLayer = 10;
		double learningRate = 0.1;	
		TTTNNBrain AI = new TTTNNBrain(9,9,hiddenLayer,learningRate);
		
		int generations = 100;
		int gamesPerGeneration = 10;
		
		for (int x = 0;x<generations;x++) {
			int games = 0;
			while (games<gamesPerGeneration) {

				games++;
				double computerMoveFirst = Math.random();

				//HERE IS A STATE. 1 is computer, -1 is human.
				double[] state = {0,0,0,0,0,0,0,0,0};

				if (computerMoveFirst>0.5) {
					compGameLog.add(tttfunc.saveState(state));
				}
				else {
					userGameLog.add(tttfunc.saveState(state));
				}
				int gameResult = 5;

				//GAME
				if(computerMoveFirst>0.5) {
					while (gameResult == 5) {
						tttfunc.computerMove(state,AI);
						userGameLog.add(tttfunc.saveState(state));
						compActions.add(tttfunc.actionCommitted(userGameLog.get(userGameLog.size()-1),compGameLog.get(compGameLog.size()-1)));
						tttfunc.printBoard(state);
						gameResult = tttfunc.gameResult(state);

						if (gameResult == 5) {
							boolean madeMove = false;
							a = new Scanner(System.in);
							while(!madeMove) {
								System.out.println("Choose box.");
								int yes = a.nextInt();
								if (tttfunc.isLegal(state,yes)) {
									tttfunc.humanMove(state, yes);
									madeMove = true;
								}
							}
							compGameLog.add(tttfunc.saveState(state));
							userActions.add(tttfunc.actionCommitted(compGameLog.get(compGameLog.size()-1),userGameLog.get(userGameLog.size()-1)));
							tttfunc.printBoard(state);
							gameResult = tttfunc.gameResult(state);
						}
					}
				}
				else {
					while (gameResult == 5) {

						boolean madeMove = false;
						a = new Scanner(System.in);
						while(!madeMove) {
							System.out.println("Choose box.");
							int yes = a.nextInt();
							if (tttfunc.isLegal(state,yes)) {
								tttfunc.humanMove(state, yes);
								madeMove = true;
							}
						}

						if (gameResult == 5) {
							compGameLog.add(tttfunc.saveState(state));
							userActions.add(tttfunc.actionCommitted(compGameLog.get(compGameLog.size()-1),userGameLog.get(userGameLog.size()-1)));
							tttfunc.printBoard(state);
							gameResult = tttfunc.gameResult(state);
							tttfunc.computerMove(state,AI);
							userGameLog.add(tttfunc.saveState(state));
							compActions.add(tttfunc.actionCommitted(userGameLog.get(userGameLog.size()-1),compGameLog.get(compGameLog.size()-1)));
							tttfunc.printBoard(state);
							gameResult = tttfunc.gameResult(state);
						}
					}
				}

				//Creates valid training data

				while (compGameLog.size()>compActions.size()) {
					compGameLog.remove(compGameLog.size()-1);
				}
				while (userGameLog.size()>userActions.size()) {
					userGameLog.remove(userGameLog.size()-1);
				}

				System.out.println("\nCOMP GAME LOG");
				for (int i = 0;i<compGameLog.size();i++) {
					System.out.println(Arrays.toString(compGameLog.get(i)));
				}
				System.out.println("\nCOMP ACTION LOG");
				for (int i = 0;i<compActions.size();i++) {
					System.out.println(Arrays.toString(compActions.get(i)));
				}

				System.out.println("\nUSER GAME LOG");
				for (int i = 0;i<userGameLog.size();i++) {
					System.out.println(Arrays.toString(userGameLog.get(i)));
				}
				System.out.println("\nUSER ACTION LOG");
				for (int i = 0;i<userActions.size();i++) {
					System.out.println(Arrays.toString(userActions.get(i)));
				}

				System.out.println("\n\nCURRENT STATE: " + (Arrays.toString(state)));
				System.out.println("WIN: " + gameResult);

				//if game result is 1, we feed the computers moves as it is. if game result is -1, we switch -1 and 1s. If game = 0, then disregard.
				if (gameResult == 1) {
					inputStates.addAll(compGameLog);
					smartActions.addAll(compActions);
					compGameLog.clear();
					compActions.clear();
					userGameLog.clear();
					userActions.clear();
				}
				else if (gameResult == -1) {
					//replacement
					for (int i = 0;i<userGameLog.size();i++) {
						for (int j = 0;j<userGameLog.get(i).length;j++) {
							int[] data = userGameLog.get(i);
							if (data[j] == -1) data[j] = 3;
							if (data[j] == 1) data[j] = -1;
							if (data[j] == 3) data[j] = 1;
							userGameLog.set(i, data);
						}
					}
					for (int i = 0;i<userActions.size();i++) {
						for (int j = 0;j<userActions.get(i).length;j++) {
							int[] data = userActions.get(i);
							if (data[j] == -1) data[j] = 1;
							userActions.set(i, data);
						}
					}
					inputStates.addAll(userGameLog);
					smartActions.addAll(userActions);
					//problem is here.
					int[] lastState = compGameLog.get(compGameLog.size()-1);
					int[] correction = userActions.get(userActions.size()-1);
					//inputStates.remove(inputStates.size()-1);
					//smartActions.remove(smartActions.size()-1);
					inputStates.add(lastState);
					smartActions.add(correction);
					compGameLog.clear();
					compActions.clear();
					userGameLog.clear();
					userActions.clear();
				}

			}

			//convert the Array list into a matrix of double values for training
			int[][] rawTrainingStates = new int[inputStates.size()][inputStates.get(0).length];
			for (int i = 0;i<rawTrainingStates.length;i++) {
				rawTrainingStates[i]=inputStates.get(i);
			}
			double[][] trainingStates = new double[rawTrainingStates.length][rawTrainingStates[0].length];
			for (int i = 0;i<trainingStates.length;i++) {
				for (int j = 0;j<trainingStates[0].length;j++) {
					trainingStates[i][j]=rawTrainingStates[i][j];
				}
			}

			int[][] rawTrainingOutput = new int[smartActions.size()][smartActions.get(0).length];
			for (int i = 0;i<rawTrainingOutput.length;i++) {
				rawTrainingOutput[i]=smartActions.get(i);
			}
			double[][] trainingOutput = new double[rawTrainingOutput.length][rawTrainingOutput[0].length];
			for (int i = 0;i<trainingOutput.length;i++) {
				for (int j = 0;j<trainingOutput[0].length;j++) {
					trainingOutput[i][j]=rawTrainingOutput[i][j];
				}
			}
			//training states completed.
			System.out.println("\nINPUT STATES");
			for (int i = 0;i<trainingStates.length;i++) {
				System.out.println(Arrays.toString(trainingStates[i]));
			}
			System.out.println("\nSMART ACTIONS");
			for (int i = 0;i<trainingOutput.length;i++) {
				System.out.println(Arrays.toString(trainingOutput[i]));
			}

			AI.trainNetwork(30000, trainingStates, trainingOutput);
		}
	}

}