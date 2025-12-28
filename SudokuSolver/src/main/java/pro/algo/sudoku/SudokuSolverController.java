package pro.algo.sudoku;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class SudokuSolverController {
    @FXML private GridPane sudokuBoard;
    @FXML private Button solveButton;

    //Remove comment to use timeout (complex sudoku require a lot of backtracking
    //private int backtrackCounter = 0;
    //private static final int maxBacktracks = 50000;

    public void toggleNumber(ActionEvent e) {
        Object source = e.getSource();

        Button clickedButton = (Button) source;
        int initialNum = Integer.parseInt(clickedButton.getText());

        if (initialNum < 9) {
            initialNum++;
        } else {
            initialNum = 0;
        }

        clickedButton.setText(Integer.toString(initialNum));
    }

    public void resetSudoku() {
        for (Node block : sudokuBoard.getChildren()) {
            if (block instanceof GridPane localBox) {

                for (Node btn : localBox.getChildren()) {
                    if (btn instanceof Button cell) {
                        cell.setText("0");
                    }
                }
            }
        }
    }

    public void getAndSolveSudoku() {
        int[][] sudoku = getSudoku();

        // 1. Check for immediate user input errors
        if (!isBoardValid(sudoku)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Board");
            alert.setContentText("The current board violates Sudoku rules (duplicates found).");
            alert.show();
            return;
        }

        solveButton.setDisable(true);

        // 2. Create a background Task
        javafx.concurrent.Task<Boolean> solverTask = new javafx.concurrent.Task<>() {
            @Override
            protected Boolean call() {
                //backtrackCounter = 0; // Reset counter (Remove comment if use timeout)
                return solveSudoku(sudoku);
            }
        };

        // 3. Handle success/failure on the UI Thread
        solverTask.setOnSucceeded(e -> {
            solveButton.setDisable(false);
            boolean solved = solverTask.getValue();

            if (solved) {
                updateBoard(sudoku);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Unsolvable");

                // Differentiate between "Timeout" and "Mathematically impossible"
                /*if (backtrackCounter > maxBacktracks) {
                    alert.setContentText("Calculation timed out. This sudoku is too complex or unsolvable.");
                } else {
                    alert.setContentText("This sudoku is unsolvable.");
                }
                alert.show();*/
            }
        });

        new Thread(solverTask).start();
    }

    private boolean isBoardValid(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int num = board[row][col];
                if (num != 0) {
                    board[row][col] = 0;
                    if (!isValidOption(board, num, row, col)) {
                        board[row][col] = num;
                        return false;
                    }
                    board[row][col] = num;
                }
            }
        }
        return true;
    }

    public boolean solveSudoku(int[][] sudoku) {
        //Remove this comment if use timeout
        /*backtrackCounter++;

        if (backtrackCounter > maxBacktracks) {
            return false;
        }*/

        int[] bestCell = findBestCell(sudoku);
        int row = bestCell[0];
        int col = bestCell[1];

        if (row == -1 || col == -1) {
            return true;
        }

        for (int num = 1; num <= 9; num++) {
            if (isValidOption(sudoku, num, row, col)) {
                sudoku[row][col] = num;

                if (solveSudoku(sudoku)) {
                    return true;
                } else {
                    sudoku[row][col] = 0;
                }
            }
        }

        return false;
    }

    public void updateBoard(int[][] solvedSudoku) {
        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                GridPane localBox = (GridPane) getNodeByRowColumn(blockRow, blockCol, sudokuBoard);

                for (int innerBlockRow = 0; innerBlockRow < 3; innerBlockRow++) {
                    for (int innerBlockCol = 0; innerBlockCol < 3; innerBlockCol++) {
                        Button numButton = (Button) getNodeByRowColumn(innerBlockRow, innerBlockCol, localBox);

                        int actualRow = blockRow * 3 + innerBlockRow;
                        int actualCol = blockCol * 3 + innerBlockCol;

                        numButton.setText(String.valueOf(solvedSudoku[actualRow][actualCol]));
                    }
                }
            }
        }
    }

    public int[][] getSudoku() {
        int[][] sudoku = new int[9][9];

        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                GridPane localBox = (GridPane) getNodeByRowColumn(blockRow, blockCol, sudokuBoard);

                for (int innerBlockRow = 0; innerBlockRow < 3; innerBlockRow++) {
                    for (int innerBlockCol = 0; innerBlockCol < 3; innerBlockCol++) {

                        // Get the specific button
                        Button numButton = (Button) getNodeByRowColumn(innerBlockRow, innerBlockCol, localBox);

                        // 3. Calculate the actual Logic Coordinates (0-8)
                        int actualRow = blockRow * 3 + innerBlockRow;
                        int actualCol = blockCol * 3 + innerBlockCol;

                        // Store it in our easy-to-use array
                        sudoku[actualRow][actualCol] = Integer.parseInt(numButton.getText());
                    }
                }
            }
        }
        return sudoku;
    }

    public Node getNodeByRowColumn(int row, int col, GridPane board) {
        for (Node block : board.getChildren()) {
            Integer r = GridPane.getRowIndex(block);
            Integer c = GridPane.getColumnIndex(block);
            int rValue = (r == null) ? 0 : r;
            int cValue = (c == null) ? 0 : c;
            if(rValue == row && cValue == col){
                return block;
            }
        }
        return null;
    }

    static int[] findBestCell(int[][] sudoku) {
        int bestRow = -1;
        int bestCol = -1;
        int minOptions = 10;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (sudoku[r][c] == 0) {
                    int possibleOptions = countValidOption(sudoku, r, c);

                    if (possibleOptions < minOptions) {
                        minOptions = possibleOptions;
                        bestRow = r;
                        bestCol = c;
                    }

                    if (possibleOptions <= 1) return new int[] {bestRow, bestCol};
                }
            }
        }

        return new int[] {bestRow, bestCol};
    }

    static int countValidOption(int[][] sudoku, int row, int col) {
        int validCounter = 0;
        for (int i = 1; i <= 9; i++) {
            if (isValidOption(sudoku, i, row, col)) validCounter++;
        }
        return validCounter;
    }

    static boolean isValidOption(int[][] sudoku, int num, int row, int col) {
        int localBoxRow = row - (row % 3);
        int localBoxCol = col - (col % 3);
        for (int r = localBoxRow; r < localBoxRow + 3; r++) {
            for (int c = localBoxCol; c < localBoxCol + 3; c++) {
                if (sudoku[r][c] == num) return false;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (sudoku[row][i] == num || sudoku[i][col] == num) return false;
        }

        return true;
    }
}
