package SudokuWithBacktracking;


public class SudokuBT {

    // we define a simple grid to solve. Grid is stored in a 2D Array
    public static int[][] GRID_TO_SOLVE = {

            {5,0,1,0,0,0,6,0,4},
            {0,9,0,3,0,6,0,5,0},
            {0,0,0,0,9,0,0,0,0},
            {4,0,0,0,0,0,0,0,9},
            {0,0,0,1,0,9,0,0,0},
            {7,0,0,0,0,0,0,0,6},
            {0,0,0,0,2,0,0,0,0},
            {0,8,0,5,0,7,0,6,0},
            {1,0,3,0,0,0,7,0,2},

    };

    private int[][] board;
    public static final int EMPTY = 0; // empty cell
    public static final int SIZE = 9; // size of our Sudoku grids

    public SudokuBT(int[][] board) {
        this.board = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    // we check if a possible number is already in a row
    private boolean isInRow(int row, int number) {
        for (int i = 0; i < SIZE; i++)
            if (board[row][i] == number)
                return true;

        return false;
    }

    // we check if a possible number is already in a column
    private boolean isInCol(int col, int number) {
        for (int i = 0; i < SIZE; i++)
            if (board[i][col] == number)
                return true;

        return false;
    }

    // we check if a possible number is in its 3x3 box
    private boolean isInBox(int row, int col, int number) {
        int r = row - row % 3;
        int c = col - col % 3;

        for (int i = r; i < r + 3; i++)
            for (int j = c; j < c + 3; j++)
                if (board[i][j] == number)
                    return true;

        return false;
    }

    // combined method to check if a number possible to a row,col position is ok
    private boolean isOk(int row, int col, int number) {
        return !isInRow(row, number)  &&  !isInCol(col, number)  &&  !isInBox(row, col, number);
    }

    // recursive BackTracking algorithm.
    public boolean solve() {
        //look for every cell in sudoku
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {

                // searching an empty cell
                if (board[row][col] == EMPTY) {
                    // start to try each posiible number
                    for (int number = 1; number <= SIZE; number++) {
                        if (isOk(row, col, number)) {
                            // if this number is okay, go recursive , otherwise do it empty(0) again
                            board[row][col] = number;

                            if (solve()) { // recursively part
                                return true;
                            } else { // if return false, change the way of the game tree
                                board[row][col] = EMPTY;
                            }
                        }
                    }
                    return false; // we return false
                }
            }
        }
        return true; // sudoku solved
    }

    public void display() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(" " + board[i][j]);
            }

            System.out.println();
        }

        System.out.println();
    }

    public static void main(String[] args) {
        SudokuBT sudoku = new SudokuBT(GRID_TO_SOLVE);
        System.out.println("Sudoku grid to solve");
        sudoku.display();

        long startMilliSecond = System.currentTimeMillis();
        long stopMilliSecond = 0;
        // we try resolution
        if (sudoku.solve()) {
            System.out.println("Sudoku Grid solved with simple BT");
            stopMilliSecond = System.currentTimeMillis();
            sudoku.display();
        } else {
            System.out.println("Unsolvable");
        }

        System.out.println("Sudoku Solved in : " + (stopMilliSecond-startMilliSecond) + " ms");
    }

}