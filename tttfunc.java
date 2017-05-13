package NEURAL_NETWORKS;

import java.util.ArrayList;
import java.util.Arrays;

public class tttfunc {
	
	public static void print(double[] st, int boardWidth) {
		String[] XO_REPRESENT = new String[st.length];
		for (int i = 0;i<st.length;i++) {
			if(st[i]==1) XO_REPRESENT[i]="X";
			if(st[i]==0) XO_REPRESENT[i]=" ";
			if(st[i]==-1) XO_REPRESENT[i]="O";
		}
		String dash = "------";
		String actual = "";
		for (int i = 0;i<boardWidth;i++) {
			actual += dash;
		}
		System.out.println("\n\n\n\nBOARD:");
		for (int i = 0;i<boardWidth-1;i++) {
			for (int j =0;j<boardWidth;j++) {
				if (j<boardWidth-1) {
					System.out.print("  "+XO_REPRESENT[i*boardWidth+j]+"  |");
				}
				else {
					System.out.print("  "+XO_REPRESENT[i*boardWidth+j]);
				}
			}
			System.out.println("\n" + actual);
		}
		for (int j =0;j<boardWidth;j++) {
			if (j<boardWidth-1) {
				System.out.print("  "+XO_REPRESENT[(boardWidth-1)*boardWidth+j]+"  |");
			}
			else {
				System.out.print("  "+XO_REPRESENT[(boardWidth-1)*boardWidth+j]);
			}
		}
	}

	public static int[][] winningStates(double[] st, int boardWidth, int inARow, boolean print) {
		ArrayList <int[]> winStates = new ArrayList<int[]>();
		//horizontal = can confirm
		for (int i = 0;i<boardWidth*boardWidth;i++) {
			if (i%boardWidth<=(boardWidth-inARow)) {
				int[] newState = new int[inARow];
				for (int j = 0;j<inARow;j++) {
					newState[j]= i+j;
				}
				winStates.add(newState);
			}
		}
		//vertical = can confirm
		for (int i = 0;i<boardWidth*boardWidth;i++) {
			if (i/boardWidth<=(boardWidth-inARow)) {
				int[] newState = new int[inARow];
				for (int j = 0;j<inARow;j++) {
					newState[j]= i+(j*boardWidth);
				}
				winStates.add(newState);
			}
		}
		//diagonal from RB -> LT = can confirm
		for (int i = st.length-1;i>=0;i--) {
			if (i%boardWidth>=inARow-1&&i/boardWidth>=inARow-1) {
				int[] newState = new int[inARow];
				for (int j = 0;j<inARow;j++) {
					newState[j]= i-j-(j*boardWidth);
				}
				winStates.add(newState);
			}
		}
		//diagonal from LB -> RT
		for (int i = st.length-1;i>=0;i--) {
			if (i%boardWidth<=boardWidth-inARow&&i/boardWidth>=inARow-1) {
				int[] newState = new int[inARow];
				for (int j = 0;j<inARow;j++) {
					newState[j]= i+j-(j*boardWidth);
				}
				winStates.add(newState);
			}
		}
		int[][] states = new int[winStates.size()][inARow];
		for (int i = 0;i<winStates.size();i++) {
			states[i] = winStates.get(i);
			if(print) {
				System.out.println(Arrays.toString(states[i]));
			}
		}
		return states;
	}

	public static int gameResult(double[] st,int boardWidth, int inARow) {
		int[][] winningStates = winningStates(st,boardWidth,inARow,false);
		for (int i = 0;i<winningStates.length;i++) {
			if (st[winningStates[i][0]] != 0 && st[winningStates[i][0]] == st[winningStates[i][1]] && st[winningStates[i][1]] == st[winningStates[i][2]]) {
				return (int) st[winningStates[i][0]];
			}
		}
		if (isBoardFilled(st)) return 0;
		return 5;
	}
	
	public static int 五子棋gameResult(double[] st,int boardWidth, int inARow) {
		int[][] winningStates = winningStates(st,boardWidth,inARow,false);
		for (int i = 0;i<winningStates.length;i++) {
			if (st[winningStates[i][0]] != 0 && st[winningStates[i][0]] == st[winningStates[i][1]] && st[winningStates[i][1]] == st[winningStates[i][2]] && st[winningStates[i][2]] == st[winningStates[i][3]] && st[winningStates[i][3]] == st[winningStates[i][4]]) {
				return (int) st[winningStates[i][0]];
			}
		}
		if (isBoardFilled(st)) return 0;
		return 5;
	}

	public static boolean isLegal(double[] st, int index) {
		if (st[index] == 0) return true;
		return false;
	}
	public static int[] possibleActions(double[] st) {
		ArrayList<Integer> na = new ArrayList<Integer>();
		for (int i = 0;i<st.length;i++) {
			if (st[i]==0) {
				na.add(i);
			}
		}
		int[] possibleMoves = new int[na.size()];
		for (int i = 0;i<na.size();i++) {
			possibleMoves[i]=na.get(i);
		}
		return possibleMoves;
	}

	public static boolean isBoardFilled(double[] st) {
		for (int i = 0;i<st.length;i++) {
			if(st[i]==0) return false;
		}
		return true;
	}

	public static boolean isPossible(int index, int[] possibleMoves) {
		for (int i = 0;i<possibleMoves.length;i++) {
			if (index == possibleMoves[i]) return true;
		}
		return false;
	}

	public static int chosenMove(double[] prediction, double[] st) {
		int chosenMove = possibleActions(st)[0];
		for (int i = chosenMove+1;i<prediction.length;i++) {
			if (prediction[i]>prediction[chosenMove] && isPossible(i,possibleActions(st))) {
				chosenMove = i;
			}
		}
		return chosenMove;
	}

	public static double[] makeMove(double[] a, int square, int val) {
		double[] NEW_st = a;
		if (a[square] == 0) {
			NEW_st[square] = val;
		}
		else {
			System.out.println("Invalid move entered.");
		}
		return NEW_st;
	}

	public static double[] humanMove(double[] st, int square) {
		if (!isBoardFilled(st)) {
			return makeMove(st,square,-1);
		}
		return st;
	}

	public static double[] computerMove(double[] st, TTTNNBrain a) {
		if (!isBoardFilled(st)) {
			double[] prediction = a.predict(st);
			return makeMove(st,chosenMove(prediction,st),1);
		}
		return st;
	}

	public static int[] saveState(double[] st) {
		int[] oldState = new int[st.length];
		for (int i = 0;i<st.length;i++) {
			oldState[i]=(int) st[i];
		}
		return oldState;
	}

	public static int[] actionCommitted(int[] st1,int[] st2) {
		int[] NEW_ARRAY = new int[st1.length];
		for (int i = 0;i<st1.length;i++) {
			NEW_ARRAY[i] = st1[i]-st2[i];
		}
		return NEW_ARRAY;
	}

}
