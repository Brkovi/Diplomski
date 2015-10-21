package puzzle;

public class StateMachine
{
	enum State { INIT, MIX, RUN, PAUSE, STOP, COMPLETED;
	public State getNext() {
		return this.ordinal() < State.values().length - 1
				? State.values()[this.ordinal() + 1] : INIT;
		}
	}
	State state = State.INIT;
	
	private SliderPuzzle sliderPuzzle;
	private ControlPanel controlPanel;
	private Ticker timeTicker;
	private PuzzlePanel puzzlePanel;
	private Ticker puzzleTicker;
	
	private boolean paused = false;
	
	
	public StateMachine(SliderPuzzle sp,ControlPanel cp, Ticker tt, PuzzlePanel pp, Ticker pt)
	{
		sliderPuzzle = sp;
		controlPanel = cp;
		timeTicker = tt;
		puzzlePanel = pp;
		puzzleTicker = pt;
	}
	
	public void executeState()
	{
		switch(state)
		{
			case INIT:
				if( imageLoaded() && gridSet() )
				{
					timeTicker.pause();
					puzzleTicker.pause();
					
					sliderPuzzle.loadPicture.setEnabled(false);
					sliderPuzzle.gridInput.setEnabled(false);
					
					controlPanel.start.setEnabled(false);
					
					controlPanel.mix.setEnabled(true);
					
					controlPanel.again.setEnabled(false);
					controlPanel.pause.setEnabled(false);
					controlPanel.stop.setEnabled(false);
					
					puzzlePanel.setRows(sliderPuzzle.rows);
					puzzlePanel.setColumns(sliderPuzzle.columns);
					sliderPuzzle.componentResized(null);
					puzzlePanel.isActive = false;
					puzzlePanel.clearList();
					puzzlePanel.initTiles();
					puzzlePanel.showTiles();
					
					nextState();
				}
				break;
				
			case MIX:
				controlPanel.setTime(0);
				//ticker.reset();
				
				controlPanel.mix.setEnabled(false);
				
				controlPanel.again.setEnabled(true);
				controlPanel.pause.setEnabled(true);
				controlPanel.stop.setEnabled(true);
				
				timeTicker.tick();
				//puzzleTicker.tick();
				
				puzzlePanel.mixTiles();
				puzzlePanel.isActive = true;
				puzzlePanel.showTiles();
				
				nextState();
				break;
				
			case RUN:
				break;
				
			case PAUSE:
				if(paused) 
				{
					timeTicker.tick();
					puzzleTicker.tick();
					state = State.RUN;
					
					puzzlePanel.paused = false;
					puzzlePanel.showTiles = true;
					puzzlePanel.isActive = true;
					
					puzzlePanel.repaint();
				}
				else 
				{
					timeTicker.pause();
					puzzleTicker.pause();
					puzzlePanel.paused = true;
					puzzlePanel.showTiles = false;
					puzzlePanel.isActive = false;
					
					puzzlePanel.repaint();
				}
					
				paused = !paused;
				//executeState();
				break;
				
			case STOP:
				controlPanel.setTime(0);
				controlPanel.showTime();
				
				sliderPuzzle.loadPicture.setEnabled(true);
				sliderPuzzle.gridInput.setEnabled(true);
				
				controlPanel.start.setEnabled(true);
				
				controlPanel.again.setEnabled(false);
				controlPanel.pause.setEnabled(false);
				controlPanel.stop.setEnabled(false);
				
				timeTicker.pause();
				
				puzzlePanel.isActive = false;
				
				//nextState();
				state = State.INIT;
				break;
				
			case COMPLETED:
				timeTicker.pause();
				
				sliderPuzzle.ranks.onGameCompleted(controlPanel.getTime());
				
				controlPanel.setTime(0);
				controlPanel.showTime();
				
				sliderPuzzle.loadPicture.setEnabled(true);
				sliderPuzzle.gridInput.setEnabled(true);
				
				controlPanel.start.setEnabled(true);
				
				controlPanel.again.setEnabled(false);
				controlPanel.pause.setEnabled(false);
				controlPanel.stop.setEnabled(false);
				
				//puzzlePanel.isActive = false;
				
				state = State.INIT;
				break;
				
		}
	}
	
	public void nextState()
	{
		state = state.getNext();
	}
	
	public boolean imageLoaded() { return sliderPuzzle.image != null; }
	
	public boolean gridSet() 
	{ 
		return sliderPuzzle.rows >= InputDialog.minRow && sliderPuzzle.rows <= InputDialog.maxRow 
				&& sliderPuzzle.columns >= InputDialog.minCol && sliderPuzzle.columns <= InputDialog.maxCol; 
	}
}
