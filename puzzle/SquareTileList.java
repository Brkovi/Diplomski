package puzzle;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class SquareTileList extends LinkedList<SquareTile>
{
	private LinkedList<TilePosition> tilePosList;
	private SquareTile markedTile = null;
	
	private PuzzlePanel pp;
	
	public SquareTileList(PuzzlePanel puzzlePanel)
	{
		tilePosList = new LinkedList<TilePosition>();
		
		pp = puzzlePanel;
	}
	
	public void populateList()
	{
		int size = pp.width / pp.columns;
		
		markedTile = null;
		
		for(int i = 0; i < pp.rows; i++)
			for(int j = 0; j < pp.columns; j++)
			{
				int x = size / 2 + j * size;
				int y = size / 2 + i * size;
				
				TilePosition tilePos = new TilePosition(x, y);
				tilePosList.add(tilePos);
				
				int imgW = pp.image.getWidth() / pp.columns;
				int imgH = pp.image.getHeight() / pp.rows;
				
				BufferedImage img = pp.image.getSubimage(imgW * j, imgH * i, imgW, imgH);
				
				boolean empty = (i == pp.rows - 1) && (j == pp.columns - 1);
				
				this.add(new SquareTile( img, empty, tilePos, size));
			}
	}
	
	public void onResize(double ratio)
	{
		for(TilePosition t : tilePosList)
			t.onResize(ratio);
		
		for(SquareTile st : this)
			st.onResize(ratio);
	}

	public void drawTiles(Graphics2D g2d)
	{
		for(SquareTile st : this)
			st.draw(g2d);
		
		if(markedTile != null) markedTile.draw(g2d);
	}
	
	public void mixTiles()
	{
		LinkedList<TilePosition> tmpList = new LinkedList<TilePosition>();
		tmpList.addAll(tilePosList);
		
		for(SquareTile tile : this)
		{
			int max = tmpList.size();
			int index = (int) (Math.random() * max);
			
			tile.setCurrPos(tmpList.remove(index));
		}
	}

	public void clearList()
	{
		this.clear();
		tilePosList.clear();
	}

	public void onUpdate(long dt)
	{
		for(SquareTile st : this)
			st.onUpdate(dt);
	}
	
	public boolean tilesMoving()
	{
		for(SquareTile st : this)
			if( st.isMoving()) return true;
		
		return false;
	}
	
	public void onClicked(Point point)
	{
		SquareTile clicked = getClickedTile(point);
		
		if(markedTile != null)
		{
			markedTile.disableGreenBorder();
			markedTile.disableRedBorder();
		}
		
		if(clicked != null)
		{
			
			if(markedTile != null)
			{
				if(markedTile.isNeighbour(clicked) && clicked.isEmpty() )
				{
					markedTile.swapWith(clicked);
					markedTile.enableGreenBorder();
				}
				else
					markedTile = null;
			}
			
			if(markedTile == null)
			{
				markedTile = clicked;
				boolean nextToEmpty = false;
				for(SquareTile tmp : this)
					if(markedTile.isNeighbour(tmp))
						if ( tmp.isEmpty() ) 
						{
							nextToEmpty = true;
							break;
						}
							
				if(nextToEmpty) markedTile.enableGreenBorder();
				else markedTile.enableRedBorder();
			}
		}
	}
	
	private SquareTile getClickedTile(Point point)
	{
		for(SquareTile st : this)
			if(st.containsPoint(point))
				return st;
		
		return null;
	}
	
	public boolean puzzleCompleted()
	{
		boolean ret = true;
		
		for(SquareTile st : this)
			if(st.currPos != st.initPos)
				ret = false;
		
		return ret;
	}
	
}
