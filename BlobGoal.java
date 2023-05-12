package assignment3;

import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {

		Color[][] b = board.flatten();
		boolean[][] bools = new boolean[b.length][b.length];
		int[] score;
		int maxScore = 0;

		for(int r = 0; r<bools.length;r++){
			for(int c = 0; c<bools[r].length;c++){
				bools[r][c] = false;
			}
		}

		for(int row = 0; row < b.length; row++){

			score = new int[b[row].length];

			for(int col = 0; col < b[row].length; col++){
				score[col] = undiscoveredBlobSize(row, col, b, bools);

				for(int r = 0; r<bools.length;r++){
					for(int c = 0; c<bools[r].length;c++){
						bools[r][c] = false;
					}
				}

				//System.out.println(bools[0][0]);

			}

			for(int i = 0; i < score.length; i++){
				//System.out.println(score[i]);
				if(score[i] > maxScore){
					maxScore = score[i];
				}
			}

		}

		return maxScore;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) 
		+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		int found = 0;
		int n1 = 0;
		int n2 = 0;
		int n3 = 0;
		int n4 = 0;
		boolean cont = false;

		//System.out.println(unitCells.length);

		if ((i <= 0 || visited[i - 1][j]) && (i >= unitCells.length - 1 || visited[i + 1][j])
				&& (j <= 0 || visited[i][j - 1]) && (j >= unitCells[i].length - 1 || visited[i][j + 1])){
			return 0;
		}

		visited[i][j] = true;

		if(unitCells[i][j] == targetGoal) {
			found = 1;
			cont = true;
		}

		if(cont) {
			if (i > 0 && !visited[i - 1][j]) {
				n1 = undiscoveredBlobSize(i - 1, j, unitCells, visited);
			}
			if (i < unitCells.length - 1 && !visited[i + 1][j]) {
				n2 = undiscoveredBlobSize(i + 1, j, unitCells, visited);
			}
			if (j > 0 && !visited[i][j - 1]) {
				n3 = undiscoveredBlobSize(i, j - 1, unitCells, visited);
			}
			if (j < unitCells[i].length - 1 && !visited[i][j + 1]) {
				n4 = undiscoveredBlobSize(i, j + 1, unitCells, visited);
			}
		}

		return found + n1 + n2 + n3 + n4;
	}

}
