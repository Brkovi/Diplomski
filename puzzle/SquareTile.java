package puzzle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class SquareTile extends Tile
{
	private static final double tileSpeed = 0.3; //in sec
	
	TilePosition currPos, initPos, transitPos = null, srcPos = null, dstPos = null;
	
	private int size;
	private double doubleSize;
	private boolean greenBorder = false, redBorder = false;
	private boolean moving = false;
	
	private Rectangle2D rectangle;

	protected SquareTile(BufferedImage image, boolean empty,
			TilePosition position, int size)
	{
		super(image, empty);
		initPos = currPos = position;
		this.size = size;
		this.doubleSize = size;
		
		rectangle = new Rectangle2D.Double( -size / 2, -size / 2, size, size);
		
		this.texPaint = new TexturePaint(image, rectangle);
	}

	@Override
	public void draw(Graphics2D g2d)
	{
		AffineTransform backup = new AffineTransform();
		backup.setTransform( g2d.getTransform() );
		
//		AffineTransform transform = new AffineTransform();
//		transform.setToIdentity();
//		g2d.setTransform(transform);
		
		g2d.translate(currPos.x() + PuzzlePanel.indent/2, currPos.y() + PuzzlePanel.indent/2);
		
		if(!empty) g2d.setPaint(texPaint);
		else g2d.setPaint(Color.black);
		g2d.fill(rectangle);
		
		g2d.setStroke(new BasicStroke(5));
		if(greenBorder) g2d.setColor(Color.green);
		else if(redBorder) g2d.setColor(Color.red);
		else g2d.setColor(Color.black);
		
		if(empty) g2d.setColor(Color.black);
		
		g2d.draw(rectangle);
		
		g2d.setTransform(backup);
	}

	public void onResize(double ratio)
	{
		doubleSize *= ratio;
		size = (int) (doubleSize);
		rectangle.setFrame( -size / 2, -size / 2, size, size);
		this.texPaint = new TexturePaint(img, rectangle);
		
		if(transitPos != null) transitPos.onResize(ratio);
	}

	public boolean isNeighbour(SquareTile tile)
	{
		int x = this.currPos.x() - tile.currPos.x();
		int y = this.currPos.y() - tile.currPos.y();
		
		double distance = Math.sqrt(x*x) + Math.sqrt(y*y);
		
		if( ( (double) size) >= distance - 1 && ((double) size) <= distance + 1) return true;
		else return false;
	}
	
//	public void swapWith(SquareTile tile)
//	{
//		TilePosition tmp = this.currPos;
//		this.currPos = tile.currPos;
//		tile.currPos = tmp;
//	}
	
	public void swapWith(SquareTile swapTile)
	{
		this.moving = true;
		swapTile.moving = true;
		
		this.srcPos = this.currPos;
		swapTile.srcPos = swapTile.currPos;
		
		this.transitPos = new TilePosition(this.currPos.doubleX(), this.currPos.doubleY());
		swapTile.transitPos = new TilePosition(swapTile.currPos.doubleX(), swapTile.currPos.doubleY());
		
		this.dstPos = swapTile.currPos;
		swapTile.dstPos = this.currPos;
		
		this.currPos = this.transitPos;
		swapTile.currPos = swapTile.transitPos;
	}
	
	public void move(long dt)
	{
		if(moving)
		{
			double dx = ( dstPos.x() - srcPos.x() ) * ( ((double) dt) / (tileSpeed * 1000));
			double dy = ( dstPos.y() - srcPos.y() ) * ( ((double) dt) / (tileSpeed * 1000));
			
			transitPos.moveX(dx);
			transitPos.moveY(dy);
			
			boolean overflowX = !( 
					(srcPos.doubleX() <= transitPos.doubleX() && transitPos.doubleX() <= dstPos.doubleX() )
					|| (srcPos.doubleX() >= transitPos.doubleX() && transitPos.doubleX() >= dstPos.doubleX() )
					);
					
			boolean overflowY = !( 
					(srcPos.doubleY() <= transitPos.doubleY() && transitPos.doubleY() <= dstPos.doubleY() )
					|| (srcPos.doubleY() >= transitPos.doubleY() && transitPos.doubleY() >= dstPos.doubleY() )
					);
			
			if( overflowX || overflowY)
			{
				moving = false;
				
				currPos = dstPos;
				
				transitPos = null;
				srcPos = null;
				dstPos = null;
			}
		}
	}
	
	public boolean isMoving() { return moving; }
	
	public void onUpdate(long dt)
	{
		move(dt);
	}
	
	public void setCurrPos(TilePosition position) { currPos = position; }

	@Override
	public boolean containsPoint(Point point)
	{
		Point p = (Point) point.clone();
		p.translate( -currPos.x(), -currPos.y() );
		
		if(rectangle != null) return rectangle.contains(p);
		
		return false;
	}
	
	public void enableGreenBorder() { greenBorder = true; }
	public void disableGreenBorder() { greenBorder = false; }
	
	public void enableRedBorder() { redBorder = true; }
	public void disableRedBorder() { redBorder = false; }
}
