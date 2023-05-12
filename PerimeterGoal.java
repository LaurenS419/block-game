package assignment3;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] b = board.flatten();

		int row = 0;
		int score = 0;
		int col = 0;

		while(row < b.length){
			for(int c = 0; c < b[0].length; c++){
				if(b[row][c] == targetGoal){
					if(row == 0){
						if(c == 0){
							score++;
						} else if (c == b[0].length-1) {
							score++;
						}
					} else if (row == b.length-1) {
						if(c == 0){
							score++;
						} else if (c == b[0].length-1) {
							score++;
						}
					}
					score++;
				}
			}
			row = row + (b.length - 1);
		}

		while(col < b[0].length){
			for(int r = 1; r < b[0].length-1; r++){
				if(b[r][col] == targetGoal){
					score++;
				}
			}
			col = col + (b[0].length - 1);
		}


		return score;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
