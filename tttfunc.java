package NEURAL_NETWORKS;

import java.util.ArrayList;

public class tttfunc {
	
	public static void printBoard(double[] st) {
		String[] XO_REPRESENT = new String[st.length];
		for (int i = 0;i<st.length;i++) {
			if(st[i]==1) XO_REPRESENT[i]="X";
			if(st[i]==0) XO_REPRESENT[i]=" ";
			if(st[i]==-1) XO_REPRESENT[i]="O";
		}
		System.out.println("\n\n\n\nBOARD:");
		System.out.println("  "+XO_REPRESENT[0]+"  |  " + XO_REPRESENT[1] + "  |  " + XO_REPRESENT[2]);
		System.out.println("---------------");
		System.out.println("  "+XO_REPRESENT[3]+"  |  " + XO_REPRESENT[4] + "  |  " + XO_REPRESENT[5]);
		System.out.println("---------------");
		System.out.println("  "+XO_REPRESENT[6]+"  |  " + XO_REPRESENT[7] + "  |  " + XO_REPRESENT[8]);

	}
	
	public static int gameResult(double[] st) {
		int[][] winningStates = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
		for (int i = 0;i<winningStates.length;i++) {
			if (st[winningStates[i][0]] != 0 && st[winningStates[i][0]] == st[winningStates[i][1]] && st[winningStates[i][1]] == st[winningStates[i][2]]) {
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
