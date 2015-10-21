package puzzle;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

public abstract class Tile {

	protected BufferedImage img = null;
	protected TexturePaint texPaint = null;
	protected boolean empty;
	
	protected Tile(BufferedImage image, boolean empty)
	{
		img = image;
		this.empty = empty;
	}
	
	public abstract void draw(Graphics2D g2d);
	
	public boolean isEmpty() { return empty; }
	
	public abstract boolean containsPoint(Point point);
}
