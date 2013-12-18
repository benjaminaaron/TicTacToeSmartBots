package tk.agarsia.tictac2.model.player.human;

import tk.agarsia.tictac2.model.Game;
import tk.agarsia.tictac2.model.player.AbstractPlayer;
import tk.agarsia.tictac2.model.player.bot.tree.TreeBuilder;

public class HumanLocal extends AbstractPlayer{
	
	private int interval;
	private Timer timer;
	
	public HumanLocal(String name, Game game) {
		super(game);
		setPlayerType(0);
		setName(name);	
		interval = game.getInterval();
		timer = new Timer(this, interval);
	}

	/**
	 * Tells game that we are waiting for a click. Once that click comes in, game is calling the myChoice method below directly.
	 * So this object doesn't influence the choice anymore - but given the fact that the local physical player has clicked, there is no need to modify that choice :)
	 */
	@Override
	public void myTurn() {
		
		game.awaitingClick();
			
		timer.restart();
		
/*		if(turnCount > 3)
			game.awaitingClick();
		else{
			if(turnCount == 0)
				myChoice(2, 1);
			if(turnCount == 1)
				myChoice(0, 2);
			if(turnCount == 2)
				myChoice(1, 0);
			if(turnCount == 3)
				myChoice(3, 1);
						
			turnCount ++;	
		}*/
				
	}
	
	

	@Override
	public boolean myChoice(int row, int column) {
		return game.placeMark(row, column);
	}

	public void intervalPassed() {
		myChoice(-1, -1);		
	}	
}
