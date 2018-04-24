
public class Player {
    MyBoard myBoard;
    char myColor;
    char opponentColor;
    char whoseTurn;
    int xCoord;
    int yCoord;
    String myMove = "";
    int myScore = 0;
    int depth;
    MyBoard [] validBoardList;
    
    //Player(MyBoard board, char myDisk, char playerTurn){
    Player(char myColor, char opponentColor){
        this.myColor = myColor;
        this.opponentColor = opponentColor;
        myScore = 2;            //start out with two disks on the board   
        depth = 5;
        validBoardList = new MyBoard[20];
       
    }
    
    //do I want to copy and pass back a 2D char array or a MyBoard object?
    public MyBoard chooseMove(Board currentBoard, int methodOfPlay){
        
        MyBoard myBoardObject = new MyBoard(currentBoard.boardRows, currentBoard.boardCols, currentBoard.maxMoveTime, currentBoard.board);
        int numEmptyCells = myBoardObject.numEmptyCells;    //right now equals 60 
        //create a new char[][] and make a deep copy
//        char [][] boardCopy = new char[currentBoard.boardRows][currentBoard.boardCols];
//        boardCopy = myBoardObject.copyBoard(currentBoard.board);
        
        int randomMove = 0;
        MyBoard [] initialArray = new MyBoard[20];
        //an array of MyBoard objects that have possible board states from the boardCopy that was passed to it
        //validBoardList = getValidBoards(myBoardObject, 5);
        myBoardObject.validBoardArray = getValidBoards(myBoardObject, 5, initialArray);
        
        myBoardObject.displayBoard(validBoardList[0]);
        
        //depending on the method of Play, one of the board objects from validBoardList will be chosen
        //the gameBoard of that object will be returned
        if (methodOfPlay == 1){         //intelligent
            //get the board with the highest heursitic value from the list
        }
        else if( methodOfPlay == 2){    //manual
            
        }
        else if (methodOfPlay == 3){    //random
            //min + (int)(Math.random() * ((Max-Min) +1))
            randomMove = (int)(Math.random() * (validBoardList.length + 1));
            myBoardObject = validBoardList[0];
        }
        
        return myBoardObject;
    }
    
    public MyBoard[] getValidBoards(MyBoard myBoardObject, int depth, MyBoard[] validBoardArray){
        boolean moveMade = false;
        int index = 0;
       
        if ((depth == 0) || (myBoardObject.numEmptyCells == 0)){    //or if cells left = 0 or if time left = 0
            //return myBoardObject.array;
        }
        
        //create a new array for each currentBoard that holds all of its children
//        MyBoard[] validBoardArray = new MyBoard[20];
        //create a new board object for each recursive call; for each possible game board state
        MyBoard validBoardObj = new MyBoard(myBoardObject.rowsInBoard, myBoardObject.colsInBoard, myBoardObject.moveTime, myBoardObject.gameBoard);
        
        //loop through the board UNTIL a move is found
        search : {      
            for(int r = 0; r < myBoardObject.rowsInBoard; r++){
                for(int c = 0; c < myBoardObject.colsInBoard; c++){
                    //if the cell is empty, begin the search to determine if it qualifies as a legal move
                    //if the cell is not empty, can't do anything and legal is returned as false 
                    //the next cell will be checked
                    if(validBoardObj.gameBoard[r][c] == ' '){
                        //initialize variables
                        int coordX, coordY;
                        boolean exhausted;
                        char current;

                        //look in each direction
                        for(int x = -1; x <= 1; x++){
                            for(int y = -1; y <= 1; y++){
                                //variables that check in each direction of given position
                                coordX = r + x;
                                coordY = c + y;
                                exhausted = false;
                                //if coordinates are out of bounds, move on to next cell
                                if((coordX < 0) ||(coordY < 0) || (coordX > 7) || (coordY > 7)){
                                    continue;
                                }
                                current = validBoardObj.gameBoard[coordX][coordY];

                                //if current is empty or the same color, move on to the next location to check
                                if((current == ' ') || (current == myColor)){   
                                    continue;
                                }
                                //otherwise the cell with [coordX][coordY] = opponentColor
                                //check along that direction to see if one of your colors is on the other side to make it a legal move
                                while(!exhausted){
                                    //System.out.println("row = " + r + " col = " + c);
                                    coordX += x;
                                    coordY += y;
                                    if((coordX < 0) ||(coordY < 0) || (coordX > 7) || (coordY > 7)){
                                        continue;
                                    }
                                    current = validBoardObj.gameBoard[coordX][coordY];

                                    //if current is my color then stop the loop and make the legal move
                                    if(current == myColor){    
                                        exhausted = true;
                                        moveMade = true;
                                        //flip the appropriate disks
                                        coordX -= x;
                                        coordY -= y;
                                        current = validBoardObj.gameBoard[coordX][coordY];

                                        //continue flipping disks until you reach the original location
                                        while(current != ' '){
                                           validBoardObj.gameBoard[coordX][coordY] = myColor;   //currentBoard.whoseTurn;
                                            coordX -= x;
                                            coordY -= y;
                                            current = validBoardObj.gameBoard[coordX][coordY];
                                        }
                                        //remember placement of the coordinates of the move made
                                        validBoardObj.gameBoard[coordX][coordY] = myColor;
                                        validBoardObj.rowPlacement = coordX;
                                        validBoardObj.colPlacement = coordY;
                                        validBoardObj.myMove = "" + (char)(validBoardObj.rowPlacement + 65) + (char)(validBoardObj.colPlacement + 65);

                                        //print out the row and col of the move that was made as well as the value of that move
                                        System.out.println("row = " + coordX + " and col = " + coordY);

                                        validBoardObj.validBoardArray[index] = validBoardObj;
                                        index++;
                                    }
                                    if (moveMade){
                                         break search;
                                    }
                                    //end loop if you reach an empty cell or go out of bounds
                                    else if((current == ' ') || (coordX >= 0) || (coordX < 9) || (coordY >= 0) || (coordY < 9)){
                                        exhausted = true;
                                    }
                                }
                                //breaks out of both loops if a move has been made
                                if (moveMade){
                                      break search;
                                }
                            }
                        }
                    }
                }
            }
        }
        validBoardObj.numDisksPlayed++;     //increment disks played
        validBoardObj.numEmptyCells--;      //decrement number of available cells
        
        //recursive call to get the possible boards after the move that was just made       
        getValidBoards(validBoardObj, depth - 1, validBoardObj.validBoardArray);
        return validBoardArray;
    }
    
    
    
    public boolean hasValidMoves(Player currentPlayer){
        if(validBoardList.length >= 1){
            return true;            
        }
        else{
            return false;
        }
    }
    
    public int getTurnNumber(){
        int total = -3;         //since at the 0th turn there are already 4 pieces on the board
        for (int r = 0; r < myBoard.rowsInBoard; r++){
            for (int c = 0; c < myBoard.colsInBoard; c++){
                if (!myBoard.isEmpty(r, c)){
                    total++;
                }     
            }
        }
        return total;
    }
    
    public char[][] copyBoard(char[][] board){
        char[][] boardCopy = new char[board.length][board.length];
        
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < 8; col++){
                boardCopy[row][col] = board[row][col];
            }
        }
        return boardCopy;
    }
}
