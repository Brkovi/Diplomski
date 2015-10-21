package puzzle;

public class Ticker implements Runnable
{
	boolean work = false;
	
	private long oldTime, currentTime, newTime;
	
	private Updatable updatable;
	
	public Ticker(Updatable u)
	{
		updatable = u;
		oldTime = currentTime = System.currentTimeMillis();
	}
	
	@Override
	public void run()
	{
		while (true) {
			try {
				synchronized(this) { if(!work) wait(); }
				
				oldTime = currentTime;
				currentTime = System.currentTimeMillis();
				
				updatable.update(currentTime - oldTime);
				
				newTime = currentTime + 25;
				Thread.sleep(Math.max(0, newTime - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void tick()
	{
		oldTime = currentTime = System.currentTimeMillis();
		work = true;
		synchronized(this) { notify(); }
	}
	
	public void pause() { work = false; }
	
	public void reset() { oldTime = currentTime = System.currentTimeMillis(); }

}
