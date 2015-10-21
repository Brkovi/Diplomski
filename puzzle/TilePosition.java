package puzzle;

public class TilePosition
{
	
	private int x = 0, y = 0;
	private double doubleX, doubleY;
	
	public TilePosition(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		this.doubleX = x;
		this.doubleY = y;
	}
	
	public TilePosition(double doubX, double doubY)
	{
		this.doubleX = doubX;
		this.doubleY = doubY;
		
		this.x = (int) doubX;
		this.y = (int) doubY;
	}
	
	public void onResize(double ratio)
	{
		doubleX *= ratio;
		x = (int) (doubleX);
		
		doubleY *= ratio;
		y = (int) (doubleY);
	}
	
	public void moveX(double dx)
	{
		doubleX += dx;
		x = (int) doubleX;
	}
	
	public void moveY(double dy)
	{
		doubleY += dy;
		y = (int) doubleY;
	}
	
	public int x() { return x; }
	
	public int y() { return y; }
	
	public double doubleX() { return doubleX; }
	
	public double doubleY() { return doubleY; }
	
}
