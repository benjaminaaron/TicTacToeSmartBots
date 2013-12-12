package tk.agarsia.tictac2.model.board;

import java.util.ArrayList;
import java.util.Random;

public class Board {

	protected int boardDim;
	protected int winLength;
	protected Field[][] fields2D;
	protected ArrayList<int[]> history;
	protected boolean winState = false;
	//protected ArrayList<Island> islands = new ArrayList<Island>();
	protected Island winningIsland;
	
	/**
	 * Function to set init game board.
	 * 
	 * @param boardDim
	 * @param winLength
	 * 
	 */
	public Board(int boardDim, int winLength){
		this.boardDim = boardDim;
		this.winLength = winLength;

		reset();
	}
	
	/**
	 * Copy Constructor of Board.
	 */
        @SuppressWarnings("unchecked")
	public Board(Board board){ // copy-constructor
		this.boardDim = board.boardDim;
		this.winLength = board.winLength;

        this.history = (ArrayList<int[]>) board.history.clone();

		fields2D = new Field[boardDim][boardDim];
		new Field(this);
		
		//here is where the "deep copy" happens
		for(int i = 0; i < boardDim; i++)	
			for(int j = 0; j < boardDim; j++)
				fields2D[i][j].setValue(board.getField(i, j).getValue());	
	}

	/**
	 * Function to reset game board.
	 */
	public void reset(){		
        history = new ArrayList<int[]>();
		fields2D = new Field[boardDim][boardDim];	
		
		new Field(this); //pure awesomeness... the whole board is building itself when just the first Field is instantiated :)
				
/*		for(int i = 0; i < boardDim; i++)	
			for(int j = 0; j < boardDim; j++)
				System.out.println(fields2D[i][j].show()); // proof
*/	}
	
	/**
	 * Function to add a field to the game board.
	 */
	public void addField(Field field){
		fields2D[field.getRow()][field.getColumn()] = field;
		//System.out.println("added " + field.getPathPos() + " at " + field.getRow() + ", " + field.getColumn());
	}
	
/*	public void addIsland(Island island){
		//islands.add(island);	
		
		if(island.getSize() >= winLength){
			winState = true;
			winningIsland = island;
		}
	}*/
	
	/**
	 * Everytime a Field is set by a player - it checks into all directions if a winningIsland has come together. 
	 * If that's the case it calls board using this method to deliver the winning Island
	 * @param winning island that field has assembled in its winCheck-process
	 */
	public void islandWon(Island island){
		winState = true;
		winningIsland = island;
	}
	
	/**
	 * Once a winningIsland has been saved we can ask it where it's Field-members are positioned.
	 * @return a string representation of the field-coordinates that caused the win
	 */
	public String getWinnersPositions() {		
		return winningIsland.showPos();
	}
	
	public boolean getWinState(){
		return winState;
	}
	
	public Island getWinningIsland(){
		return winningIsland;
	}
	
	public int getBoardDim(){
		return boardDim;
	}
	
	public int getWinLength(){
		return winLength;
	}
	
	/**
	 * Gives access to a field based on its coordinates.
	 * @param row: of requested field
	 * @param column: of requested field
	 * @return the requested field
	 */
	public Field getField(int row, int column){
		if(row < 0 || row >= boardDim || column < 0 || column >= boardDim)
			return null;		
		return fields2D[row][column];
	}
	
	/**
	 * Sets a value one a field, based on its coordinates.
	 * 
	 * @param playerIndex: index of the player who is placing its value on the field
	 * @param row: of field where to place the mark
	 * @param column: of field where to place the mark
	 * @return true: mark was placed, false: field is already taken
	 */
	public boolean setField(int playerIndex, int row, int column){
		if(fields2D[row][column].isFree()){
			fields2D[row][column].setValue(playerIndex);		         
            history.add(new int[] {playerIndex,row,column});   			
			return true;
		}
		else 
			return false;		
	}
	
	public ArrayList<int[]> getHistory(){
		return history;
	}
	
	/**
	 * Gives access to all the Fields that are not being marked yet.
	 * It collects them by asking the first field ([0][0]) to collect them along the "snakePath"
	 * @return ArrayList of Fields that are not marked yet.
	 */
	public ArrayList<Field> getFreeFields(){
/*		ArrayList<Field> collect = new ArrayList<Field>();		
		for(int i = 0; i < boardDim; i++)	
			for(int j = 0; j < boardDim; j++)
				if(fields2D[i][j].getValue() == 0)
					collect.add(fields2D[i][j]);
		return collect;*/
		
		return fields2D[0][0].getFreeFields();
	}
	
	/**
	 * Gives access to an Integer-Array representation of the Board. 
	 * This form of "compression" is great to improve performance when smart bots will later generate
	 * huge decision trees.
	 * It is created by asking the first field ([0][0]) to write the array walking along the "snakePath"
	 * 
	 * Example:
	 * 
	 * 1 2 0
	 * 0 1 2
	 * 2 1 0
	 * 
	 * turns into:
	 * 
	 * [1, 2, 0, 0, 1, 2, 2, 1, 0]
	 * 
	 * @return Integer-Array representation of the board
	 */
	public int[] getBoardAsArr(){
		//return fields2D[0][0].getBoardAsArrBuilder();
		
		int[] boardArr = new int[boardDim * boardDim];
		
		for(int i = 0; i < boardDim * boardDim; i++)
			boardArr[i] = fields2D[i / boardDim][i % boardDim].getValue();
		
		return boardArr;
	}
	
	/**
	 * @return number of free fields on the board
	 */
	public int getFreeFieldCount(){		
		return getFreeFields().size();
	}
	
	/**
	 * @return true: board full, false: board not full
	 */
	public boolean full(){
		return getFreeFieldCount() == 0;
	}
	
	/**
	 * Places the mark of the currentPlayer randomly on one of the free fields.
	 * Random bot is using this method. It could also have been done there, but it seems smarter to leave the 
	 * method embedded in here instead of pulling all data out and then pushing the choice back in
	 * @param currentPlayerIndex
	 */
	public void placeRandomly(int currentPlayerIndex) {
		int choice = (int) (new Random().nextDouble() * getFreeFieldCount());	

        int row = getFreeFields().get(choice).getRow();
        int column = getFreeFields().get(choice).getColumn();

		getFreeFields().get(choice).setValue(currentPlayerIndex);
                
        history.add(new int[] {currentPlayerIndex,row,column});   
	}
	
	public void depositAtSpecificFreeField(int currentPlayerIndex, int target){		        
        int row = getFreeFields().get(target - 1).getRow();
        int column = getFreeFields().get(target - 1).getColumn();
                
        getFreeFields().get(target - 1).setValue(currentPlayerIndex);

       	history.add(new int[] {currentPlayerIndex,row,column});      
	}
	
	public Field getSpecificFreeField(int freeIndexTarget){
		return getFreeFields().get(freeIndexTarget);
	}
	
	/**
	 * Gives access to a string-representation of the board.
	 * 
	 * @param numbering: true: numbering is on, false: numbering is off
	 * @return a string-representation of the board
	 */
	public String show(boolean numbering){
		String buffer = "";
		
		if(numbering){		
			String columnNumbers = "    ";
			String dashLine = "    ";
			for(int i = 0; i < boardDim; i++){
				columnNumbers += i + " ";
				dashLine += "__";
			} dashLine = dashLine.substring(0, dashLine.length() - 1);
			
			buffer += columnNumbers + "\n" + dashLine + "\n";	
		}
		for(int i = 0; i < boardDim; i++){
			if(numbering) 
				buffer += i + "  |";
			for(int j = 0; j < boardDim; j++)
				buffer += fields2D[i][j].getValue() + " ";
			buffer = buffer.substring(0, buffer.length() - 1) + "\n";
		}
		return buffer;
	}

}
