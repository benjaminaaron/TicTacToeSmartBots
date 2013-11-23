package tk.agarsia.tictac2.model.board;

import java.util.ArrayList;

public class Board {

	private final int boardDim;
	private final int winLength;
	private ArrayList<Field> fields = new ArrayList<Field>();
	private Field[][] fields2D;
	private String history;
	private boolean winState = false;
	private ArrayList<Island> islands = new ArrayList<Island>();
	private Island winningIsland;
	
	public Board(int boardDim, int winLength){
		this.boardDim = boardDim;
		this.winLength = winLength;
		history = boardDim + "x" + boardDim + " field:\n";

		fields2D = new Field[boardDim][boardDim];			
		reset();
	}
	
	public void reset(){		
		fields.clear();			
		Field nextField = new Field(this);	
		//for(int i = 1; i < boardDim * boardDim; i++) // have them fill it up themselves using a "smart" constructor
		//	nextField = new Field(fields.get(fields.size() - 1));
		
		
		for(Field field : fields)
			System.out.println(field.show());
		
	}
	
	public void addField(Field field){
		fields.add(field);	
		fields2D[field.getRow()][field.getColumn()] = field;
	}
	
	public void addIsland(Island island){
		System.out.println(island.show());
		islands.add(island);	
		
		if(island.getLength() >= winLength){
			winState = true;
			winningIsland = island;
		}
	}
	
	public String getWinnersPositions() {		
		return winningIsland.show();
	}
	
	public boolean getWinState(){
		return winState;
	}
	
	public int getBoardDim(){
		return boardDim;
	}
	
	public int getWinLength(){
		return winLength;
	}
	
	
	public Field getField(int row, int column){
		if(row < 0 || row >= boardDim || column < 0 || column >= boardDim)
			return null;		
		return fields2D[row][column];
	}
	
	public boolean setField(int playerIndex, int row, int column){
		if(fields2D[row][column].isFree()){
			fields2D[row][column].setValue(playerIndex);		
			history += playerIndex + " [" + row + ", " + column + "]\n";			
			return true;
		}
		else 
			return false;		
	}
	
	public String getHistory(){
		return history;
	}
	
	public int getEmptyFieldCount(){
		int temp = 0;
		for(int i = 0; i < boardDim; i++)	
			for(int j = 0; j < boardDim; j++)
				if(fields2D[i][j].getValue() == 0)
					temp ++;
		return temp;		
	}
	
	public boolean full(){
		return getEmptyFieldCount() == 0;
	}
	
	public Field getChosenFreeField(int choice) {
		int count = 0;
		for(int i = 0; i < boardDim; i++)	
			for(int j = 0; j < boardDim; j++){
				if(fields2D[i][j].getValue() == 0){
					count ++;
					if(count == choice)
						return fields2D[i][j];
				}
			}	
		return null;	
	}
	
	public String show(){
		String buffer = "";
		
		String columnNumbers = "    ";
		String dashLine = "    ";
		for(int i = 0; i < boardDim; i++){
			columnNumbers += i + " ";
			dashLine += "__";
		} dashLine = dashLine.substring(0, dashLine.length() - 1);
		
		buffer += columnNumbers + "\n" + dashLine + "\n";
		
		for(int i = 0; i < boardDim; i++){
			buffer += i + "  |";
			for(int j = 0; j < boardDim; j++)
				buffer += fields2D[i][j].getValue() + " ";
			buffer += "\n";
		}
		return buffer;
	}
}
