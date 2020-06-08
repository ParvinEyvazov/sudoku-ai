package SudokuWithCSP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SudokuCSP {

    public static int[][] firstSudoku = {

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

    public static Cell[][] cellSudoku = new Cell[9][9];

    public static final int size = 9;

    public static final boolean showStatistic = false;


    public static void main(String[] args) {

        //----------------TURN NUMBER BASED SUDOKU TO CELL BASED SUDOKU----------------
        for (int row= 0;row< size; row++){
            for (int col = 0; col< size;col++){
                //eger bosluqdusa, yeni deger yoxdursa
                if (firstSudoku[row][col] == 0){

                    //temp numbers
                    ArrayList<Integer> numbers = new ArrayList<>();
                    numbers.add(1);
                    numbers.add(2);
                    numbers.add(3);
                    numbers.add(4);
                    numbers.add(5);
                    numbers.add(6);
                    numbers.add(7);
                    numbers.add(8);
                    numbers.add(9);

                    Cell temp = new Cell(false,row,col,false,0,numbers);
                    cellSudoku[row][col] = temp;
                }
                //eger deger varsa
                else {
                    ArrayList<Integer> noneNumber = new ArrayList<>();
                    Cell temp = new Cell(false,row,col,true,firstSudoku[row][col],noneNumber);
                    cellSudoku[row][col] = temp;
                }

            }
        }


        int iterationCounter = 0;
        displaySudoku(cellSudoku);

        long startMilliSecond = System.currentTimeMillis();

        while (!isOver(cellSudoku)){
            iterationCounter++;
            //eliminate start--------------------
            eliminate(cellSudoku);
            singleCellDestroyer(cellSudoku);
            eliminate(cellSudoku);
            //eliminate stop--------------------

            //only-row start--------------------
            only_choice_row(cellSudoku);
            //only-row stop--------------------

            //eliminate start--------------------
            eliminate(cellSudoku);
            singleCellDestroyer(cellSudoku);
            eliminate(cellSudoku);
            //eliminate stop--------------------

            //only-col start--------------------
            only_choice_col(cellSudoku);
            //only-col stop--------------------

            //eliminate start--------------------
            eliminate(cellSudoku);
            singleCellDestroyer(cellSudoku);
            eliminate(cellSudoku);
            //eliminate stop--------------------

            //only-box start--------------------
            only_choice_box(cellSudoku);
            //only-box stop--------------------

            //eliminate start--------------------
            eliminate(cellSudoku);
            singleCellDestroyer(cellSudoku);
            eliminate(cellSudoku);
            //eliminate stop--------------------
        }

        long stopMilliSecond = System.currentTimeMillis();

        checkSudoku(cellSudoku);

        if (checkSudoku(cellSudoku)){
            System.out.println("\n\nSUDOKU IS CORRECTLY SOLVED!!!");
        }else {
            System.out.println("\n\nSUDOKU IS NOT CORRECTLY SOLVED!!!");
        }

        displaySudoku(cellSudoku);

        System.out.println("Sudoku Solved in : " + (stopMilliSecond-startMilliSecond) + " ms");


    }

    // ---------------- DESTROY WHO HAS 1 POSSIBLE NUMBER ----------------
    public static Cell[][] singleCellDestroyer(Cell[][] cellSudoku){

        for (int i=0; i< size; i++){
            for (int j=0;j< size;j++){
                //if any of them`s possible number is just 1number, then put it here
                if (cellSudoku[i][j].possibleNumbers.size() == 1){
                    if (showStatistic){
                        System.out.println(i+ " : " +j + " = "+ cellSudoku[i][j].possibleNumbers.get(0) +
                                " -> solved with Single Possible Number Condition");
                    }
                    cellSudoku[i][j].found = true;
                    cellSudoku[i][j].value = cellSudoku[i][j].possibleNumbers.get(0);
                    cellSudoku[i][j].possibleNumbers.clear();
                }
            }
        }
        return cellSudoku;

    }


    // ---------------- Eliminate non suitable numbers ----------------
    public static Cell[][] eliminate(Cell[][] cellSudoku){

        //Check EVERY SINGLE CELL
        for (int row=0; row< size;row++){
            for (int col = 0; col< size ;col++){


                //if this cell is nonFound
                if (cellSudoku[row][col].found == false){

                    //collect numbers in this arraylist
                    ArrayList<Integer> alreadyHaveNums = new ArrayList<>();

                    //add all nonPossible numbers to this arraylist
                    //add from ROW
                    for (int i=0;i<size;i++){
                        if (cellSudoku[row][i].found == true){
                            //eger arraylistede varsa tekrar eklemesin
                            if (!alreadyHaveNums.contains(cellSudoku[row][i].value)){
                                alreadyHaveNums.add(cellSudoku[row][i].value);
                            }
                        }
                    }

                    //add from COLUMN
                    for (int i=0;i<size;i++){
                        if (cellSudoku[i][col].found == true){
                            //eger arraylistede varsa tekrar eklemesin
                            if (!alreadyHaveNums.contains(cellSudoku[i][col].value)){
                                alreadyHaveNums.add(cellSudoku[i][col].value);
                            }
                        }
                    }

                    //add form 3x3
                    int r = row - row % 3;
                    int c = col - col % 3;

                    for (int i = r; i < r + 3; i++){
                        for (int j = c; j < c + 3; j++){
                            if (cellSudoku[i][j].found == true){
                                if (!alreadyHaveNums.contains(cellSudoku[i][j].value)){
                                    alreadyHaveNums.add(cellSudoku[i][j].value);
                                }
                            }
                        }
                    }



                    //delete this nonPossible numbers from this cell`s possibleNums arraylist (main elimination)
                    for (int i =0; i<alreadyHaveNums.size();i++){
                        //if cell`s possibleNums contains any of this nonPossible numbers
                        if (cellSudoku[row][col].possibleNumbers.contains(alreadyHaveNums.get(i))){
                            cellSudoku[row][col].possibleNumbers.remove(cellSudoku[row][col].possibleNumbers.indexOf(alreadyHaveNums.get(i)));
                        }
                    }

                    alreadyHaveNums.clear();
                }
            }
        }

        return cellSudoku;

    }

    // ---------------- Select Only Suitable number in 3x3 BOX ----------------
    public static Cell[][] only_choice_box(Cell[][] cellSudoku){
        for (int row=0; row<size ; row++){
            for (int col=0; col<size ; col++){
                boolean shouldContinuo2 = true;
                //go every single nonFound
                if (cellSudoku[row][col].found == false){
                    //make this cell currentWorking cell (for ignoring duplicate)
                    cellSudoku[row][col].current = true;
                    for (int i =0; i< cellSudoku[row][col].possibleNumbers.size(); i++){
                        //if it can
                        if (shouldContinuo2){
                            //will look every cell`s possibleNums in 3x3 box, whether found&current is false in same time
                            int r = row - row % 3;
                            int c = col - col % 3;
                            boolean shouldContinue = true;
                            for (int a = r; a< r+3; a++){
                                for (int b = c; b< c+3; b++) {

                                    if (shouldContinue) {
                                        if (cellSudoku[a][b].found == false) {
                                            if (cellSudoku[a][b].current == false) {
                                                //if any cell in 3x3 box contain this number as possible number,
                                                // break for changing temporary possible number
                                                if (cellSudoku[a][b].possibleNumbers.contains(cellSudoku[row][col].possibleNumbers.get(i))) {
                                                    //use shouldCountinue for not to get inside again
                                                    shouldContinue = false;
                                                    break;
                                                }}}
                                        //if in the last cell of box(end) and not found any common number
                                        if (a == r + 2 && b == c + 2) {
                                            //make this cell as found
                                            if (showStatistic){
                                                System.out.println(row + " : " + col + " = " + cellSudoku[row][col].possibleNumbers.get(i)
                                                        + " -> solved with Only Choice in Box Condition");
                                            }
                                            cellSudoku[row][col].current = false;
                                            cellSudoku[row][col].found = true;
                                            cellSudoku[row][col].value = cellSudoku[row][col].possibleNumbers.get(i);
                                            cellSudoku[row][col].possibleNumbers.clear();
                                            shouldContinuo2 = false;
                                        }}}}
                        }else {
                            break;
                        }
                    }
                    //work finished on this cell
                    cellSudoku[row][col].current = false;
                }
            }
        }
        return cellSudoku;
    }

    // ---------------- Select Only Suitable number in 3x3 COLUMN ----------------
    public static Cell[][] only_choice_col(Cell[][] cellSudoku){

        for (int row=0; row<size;row++){
            for (int col =0; col< size; col++){
                boolean shouldContinuo = true;
                //go every single nonFound cell
                if (cellSudoku[row][col].found == false){
                    //make this cell currentWorking cell (for ignoring duplicate)
                    cellSudoku[row][col].current = true;

                    for (int i =0; i< cellSudoku[row][col].possibleNumbers.size(); i++){
                        if (shouldContinuo){
                            //will look every cell`s possibleNums in this column,
                            // whether found&current is false in same time
                            for (int c = 0; c<size; c++){
                                if (cellSudoku[c][col].found == false){
                                    if (cellSudoku[c][col].current == false){
                                        //if any cell in this column contain this number as possible number, break for
                                        // changing temporary possible number
                                        if (cellSudoku[c][col].possibleNumbers.contains(cellSudoku[row][col].possibleNumbers.get(i))){
                                            break;
                                        }
                                    }
                                }
                                //if in the last cell of this column and not found any common possibleNumber
                                if (c == 8){
                                    //make this cell as found
                                    if (showStatistic){
                                        System.out.println(row + " : " + col + " = "+ cellSudoku[row][col].possibleNumbers.get(i)
                                                + " -> solved with Only Choice in Column Condition");
                                    }
                                    cellSudoku[row][col].current = false;
                                    cellSudoku[row][col].found = true;
                                    cellSudoku[row][col].value = cellSudoku[row][col].possibleNumbers.get(i);
                                    cellSudoku[row][col].possibleNumbers.clear();
                                    shouldContinuo = false;
                                }
                            }
                        }else {
                            break;
                        }
                    }
                    //work finished on this cell
                    cellSudoku[row][col].current = false;
                }
            }
        }
        return cellSudoku;
    }

    // ---------------- Select Only Suitable number in 3x3 ROW ----------------
    public static Cell[][] only_choice_row(Cell[][] cellSudoku){

        for (int row=0; row < size; row++){
            for (int col =0; col < size; col++){
                boolean shouldContinuo = true;
                //go every single nonFound
                if (cellSudoku[row][col].found == false){
                    //make this cell currentWorking cell (for ignoring duplicate)
                    cellSudoku[row][col].current = true;

                    for (int i=0;i< cellSudoku[row][col].possibleNumbers.size();i++){

                        if (shouldContinuo){

                            //will look every cell`s possibleNums in this row, whether found&current is false in same time
                            for (int r = 0; r<size; r++){
                                if (cellSudoku[row][r].found == false){
                                    if (cellSudoku[row][r].current == false){
                                        //if any cell in this row contain this number as possible number, break for changing temporary possible number
                                        if (cellSudoku[row][r].possibleNumbers.contains(cellSudoku[row][col].possibleNumbers.get(i))){
                                            break;
                                        }
                                    }
                                }
                                //if in the last cell of this row and not found any common possibleNumber
                                if (r == 8){
                                    //make this cell as found
                                    if (showStatistic){
                                        System.out.println(row + " : " + col + " = "+ cellSudoku[row][col].possibleNumbers.get(i) +
                                                " -> solved with Only Choice in Row Condition");
                                    }
                                    cellSudoku[row][col].current = false;
                                    cellSudoku[row][col].found = true;
                                    cellSudoku[row][col].value = cellSudoku[row][col].possibleNumbers.get(i);
                                    cellSudoku[row][col].possibleNumbers.clear();
                                    shouldContinuo = false;
                                }
                            }
                        }else {
                            break;
                        }
                    }
                    //work finished on this cell
                    cellSudoku[row][col].current = false;
                }
            }
        }
        return cellSudoku;
    }



    //-------------------SUDOKU CHECKER-------------------

    public static boolean checkSudoku(Cell[][] cellSudoku){

        if (checkSudokuRow(cellSudoku) && chechSudokuColumn(cellSudoku)){
            return true;
        }else {
            return false;
        }

    }

    public static boolean checkSudokuRow(Cell[][] cellSudoku){

        //store allNumbers of this row in this list
        for (int row=0;row < size ; row++){
            ArrayList<Integer> numberList = new ArrayList<>();
            for (int i = 0; i<size ;i++){
                //if there is going to be any duplicate
                if (numberList.contains(cellSudoku[row][i].value)){
                    return false;
                }else {
                    //else just add it this list
                    numberList.add(cellSudoku[row][i].value);
                }
            }
        }

        return true;

    }

    public static boolean chechSudokuColumn(Cell[][] cellSudoku){

        for (int col=0; col<size; col++){
            ArrayList<Integer> numberList = new ArrayList<>();
            for (int i = 0; i< size ; i++){
                //if there is going to be any duplicate
                if (numberList.contains(cellSudoku[i][col].value)){
                    return false;
                }else {
                    //else just add it this list
                    numberList.add(cellSudoku[i][col].value);
                }
            }
        }
        return true;

    }

    public static boolean isOver(Cell[][] cellSudoku){

        for (int i=0;i<size;i++){
            for (int j =0; j<size; j++){
                if (cellSudoku[i][j].found == false){
                    return false;
                }
            }
        }
        return true;

    }



    //----------------DISPLAY SUDOKU DIFFERENT----------------

    //Display all values of Sudoku in cell
    public static void displaySudoku(Cell[][] cellSudoku){

        System.out.println("---------------------------------------------");
        for (int i=0;i<size;i++){
            for (int j=0; j<size;j++){
                System.out.print(" " + cellSudoku[i][j].value);
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------");
        System.out.println();

    }

    public static void displayPossibleNumber(Cell[][] cellSudoku){

        System.out.println("---------------------------------------------");
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                System.out.print(cellSudoku[i][j].possibleNumbers + " ");
            }
            System.out.println();
        }
        System.out.println("---------------------------------------------");

    }

}