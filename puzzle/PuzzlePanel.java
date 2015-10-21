package puzzle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import puzzle.StateMachine.State;

public class PuzzlePanel extends JPanel implements MouseListener, Updatable
{
	static final int indent = 5;
	
	BufferedImage image = null;
	private TexturePaint texPaint = null;
	
	private StateMachine stateMachine = null;
	
	int width, height;
	int rows =  1, columns = 1;
	
	boolean showTiles = false;
	boolean isActive = false, paused = false;
	
	private SquareTileList sqtList;
	
	private Ticker puzzleTicker = null;
	
	
	public PuzzlePanel(int w, int h)
	{
		width = w;
		height = h;
		
		this.setLayout(null);
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.black);
		
		sqtList = new SquareTileList(this);
	
		addMouseListener(this);
	}
	
	public void addStateMachine(StateMachine st) { stateMachine = st; }
	public void addPuzzleTicker(Ticker t) { puzzleTicker = t; }
	
	public void setImage(BufferedImage img)
	{
		image = img;
		if(image != null) texPaint = new TexturePaint( image,
                new Rectangle2D.Double( 0, 0, width, height) );
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		
		if(paused)
		{
			g2d.setPaint(texPaint);
			Rectangle2D imgRect = new Rectangle2D.Double( 0, 0, width, height);
			g2d.fill(imgRect);
		}
		
		if(showTiles)
		{
			sqtList.drawTiles(g2d);
		}
	}
	
	@Override
	public void update(long dt)
	{
		sqtList.onUpdate(dt);
		repaint();
		
		if( !sqtList.tilesMoving() )
		{
			isActive = true;
			puzzleTicker.pause();
		}
		
		if(sqtList.puzzleCompleted())
		{
			stateMachine.state = State.COMPLETED;
			stateMachine.executeState();
			isActive = false;
		}
			
	}
	
	public void showTiles()
	{
		showTiles = true;
		repaint();
	}
	
	public void changeSize(int w, int h)
	{
		int oldSize = width;
		
		int wt = w / columns;
		int ht = h / rows;
		
		int size = Math.min(wt, ht);
		
		width = size * columns - indent;
		height = size * rows;// - indent * 2;
		
		this.setSize(width, height);
		
		this.setLocation((w - width) / 2, (h - height) / 2);
		
		if(image != null) texPaint = new TexturePaint( image,
                new Rectangle2D.Double( 0, 0, width, height) );
		
		double ratio = (double)width / (double)oldSize;
		sqtList.onResize(ratio);
		
		repaint();
	}
	
	public void setRows(int r) { rows = r; }
	public void setColumns(int c) { columns = c; }
	
	public void initTiles() { sqtList.populateList(); }
	
	public void mixTiles() { sqtList.mixTiles(); }

	public void clearList() { sqtList.clearList(); }
	
	@Override
	public void mouseClicked(MouseEvent mouseEvent)
	{
		if(isActive)
		{
			sqtList.onClicked(mouseEvent.getPoint());
			repaint();
			
			if(sqtList.tilesMoving())
			{
				isActive = false;
				puzzleTicker.tick();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
}
