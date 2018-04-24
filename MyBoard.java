
public class MyBoard {
    int rowsInBoard;            //8
    int colsInBoard;            //8
    int rowPlacement;           //the row where the disk was placed for that turn on that board
    int colPlacement;           //'' for columns
    int heuristicValue;         //the value of a given board based on the evaluation function
    char[][] gameBoard;         //a copy of the currentBoard from his Board Class
    char currentColor;          //current player to move
    String myMove = "";
    boolean gameOver = false;   //determines if there are still moves to be made
    int numDisksPlayed = 4;     //keeps track of the number of disks on the board  
    int numEmptyCells = 60;
    int moveTime;
    MyBoard[] validBoardArray = new MyBoard[20];
    int[][] cellValues = {
        {25,-1, 5, 2, 2, 5,-1,25},
        {-1,-5, 1, 1, 1, 1,-5,-1},
        { 5, 1, 1, 1, 1, 1, 1, 5},
        { 2, 1, 1, 2, 2, 1, 1, 2},
        { 2, 1, 1, 2, 2, 1, 1, 2},
        { 5, 1, 1, 1, 1, 1, 1, 5},
        {-1,-5, 1, 1, 1, 1,-5,-1},
        {25,-1, 5, 2, 2, 5,-1,25}};
    
    //creates a duplicate of thisBoard
    MyBoard(MyBoard thisBoard){
        gameBoard = thisBoard.gameBoard;
        currentColor  = thisBoard.currentColor;
        numDisksPlayed = thisBoard.numDisksPlayed;
        rowsInBoard = thisBoard.rowsInBoard;
        colsInBoard = thisBoard.colsInBoard;
        gameOver = thisBoard.gameOver;
        heuristicValue = thisBoard.heuristicValue;        
    }
    MyBoard(int rows, int columns, int maxMoveTime, char[][] board){
        rowsInBoard = rows;
        colsInBoard = columns;
        moveTime = maxMoveTime;
        gameBoard = board;
    }
    
    
    public void displayBoard(MyBoard gameBoard){
         int r,c;
        System.out.print(" ");                         //column identifiers
        for (c = 0; c < colsInBoard; c++){
            System.out.print("  "+(char)(c+65));
        }
        System.out.println();
        
        //top border
        System.out.print("  "+(char)9484);                   //top left corner \u250C
        for (c = 0; c < colsInBoard - 1; c++){
            System.out.print((char)9472);               //horizontal \u2500
            System.out.print((char)9516);               //vertical T \u252C
        }
        System.out.print((char)9472);                   //horizontal \u2500
        System.out.println((char)9488);                 //top right corner \u2510
       
        //board rows
        for (r = 0; r < rowsInBoard; r++) {
            System.out.print(" "+(char)(r+65));         //row identifier
            System.out.print((char)9474);               //vertical \u2502
            for (c = 0; c < colsInBoard; c++){
                System.out.print(gameBoard.gameBoard[r][c]);
                System.out.print((char)9474);           //vertical \u2502
            }
            System.out.println();
            
            //insert row separators
            if (r < rowsInBoard - 1) {
                System.out.print("  "+(char)9500);           //left T \u251C
                for (c = 0; c < colsInBoard - 1; c++){
                    System.out.print((char)9472);       //horizontal \u2500
                    System.out.print((char)9532);       //+ (cross) \u253C
                }
                System.out.print((char)9472);           //horizontal \u2500
                System.out.println((char)9508);         //right T \u2524
            }
        }

        //bottom border
        System.out.print("  "+(char)9492);                   //lower left corner \u2514
        for (c = 0; c < colsInBoard - 1; c++){
            System.out.print((char)9472);               //horizontal \u2500
            System.out.print((char)9524);               //upside down T \u2534
        }
        System.out.print((char)9472);                   //horizontal \u2500
        System.out.println((char)9496);                 //lower right corner \u2518
        
        return;
    }
    
     //***************************************************************************************************
   	//Method:		copyBoard
	//Description:	makes a deep copy of the current status of the board from the Board Class
	//Parameters:	none
    //Returns:		nothing
	//Calls:        nothing
    
    public char[][] copyBoard(char[][] currentBoard){
        char[][] boardCopy = new char[currentBoard.length][currentBoard.length];
        
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                boardCopy[row][col] = currentBoard[row][col];
            }
        }
        return boardCopy;
    } 
    
    public int getHeuristicValue(MyBoard gameBoard){
        
        
        return heuristicValue;
    }
    
    //***************************************************************************************************
   	//Method:		evaluateBoard
	//Description:	adds all the values of the cells that have the given players pieces on it and subtracts the sum of the values 
    //              of any cells that have an opponent piece on them.
	//Parameters:	board       - the current board to be evaluated
    //Returns:		score       - the current players score
	//Calls:        nothing
    public int evaluateBoard(char [][] board, Player currentPlayer){
        int score = 0;
        
        for (int r = 0; r < board.length; r++){
            for (int c = 0; c < board.length; c++){
                if (board[r][c] == currentPlayer.myColor){
                    score += cellValues[r][c];
                }
                else if (board[r][c] == currentPlayer.opponentColor){
                    score -= cellValues[r][c];
                }
            }
        }
        
        return score;
    }
    //***************************************************************************************************
   	//Method:		setCellValues
	//Description:	assigns values to each location on the game board according to the strategic advantage of that location
	//Parameters:	none
    //Returns:		nothing
	//Calls:        nothing
   
    public void setCellValues(){
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                //set all positions to a value of 4
                cellValues[row][col] = 4;
                //on the edge
                if((row == 0) || (col == 0) || (row == 7) || (col == 7)){   
                    cellValues[row][col] = 9;
                }
                //spots one away from the edge
                if((row == 1) || (row == 6) || (col == 1) || (col == 6)){
                    cellValues[row][col] = 1;
                }
                //locations surrounding the corner
                if(((row == 1) && (col == 0)) || ((row == 1) && (col == 1)) || ((row == 0) && (col == 1)) ||
                        ((row == 6) && (col == 0)) || ((row == 6) && (col == 1)) || ((row == 7) && (col == 1)) ||
                        ((row == 0) && (col == 6)) || ((row == 1) && (col == 6)) || ((row == 1) && (col == 7)) ||
                        ((row == 7) && (col == 6)) || ((row == 6) && (col == 6)) || ((row == 6) && (col == 7))){
                    cellValues[row][col] = 0;
                }
                //corners
                if(((row == 0) && (col == 0)) || ((row == 0) && (col == 7)) || ((row == 7) && (col == 7)) || ((row == 7) && (col == 0))){
                    cellValues[row][col] = 10;
                }
                //middle 4
                if(((row == 3) && (col == 3)) || ((row == 3) && (col == 4)) || ((row == 4) && (col == 3)) || ((row == 4) && (col == 4))){
                    cellValues[row][col] = 8;
                }
            }   
        }
    } 
    //***************************************************************************************************
   	//Method:		getCellValues
	//Description:	assigns values to each location on the game board according to the strategic advantage of that location
	//Parameters:	row
    //              col
    //Returns:		cellValue       - the value of the cell with coordinates row and col given relative to its strategic placement on the board
	//Calls:        nothing
    
    public int getCellValues(int row, int col){
        return cellValues[row][col];
    } 
    
    public boolean isEmpty(int r, int c){       
        return gameBoard[r][c] == ' ';
    }
    
    public boolean isWhite(int r, int c){       
        return gameBoard[r][c] == 'W';
    }
     
    public boolean isBlack(int r, int c){       
        return gameBoard[r][c] == 'B';
    }
    
    
}
