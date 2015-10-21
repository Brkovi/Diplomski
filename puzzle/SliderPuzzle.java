package puzzle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SliderPuzzle extends JFrame implements ComponentListener
{

	private long oldTime, currentTime, newTime;

	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu = new JMenu("Meni");
	JMenuItem loadPicture = new JMenuItem("Izaberi sliku");
	JMenuItem gridInput = new JMenuItem("Unesi dimenzije");

	private JFileChooser fc = new JFileChooser();
	private FileFilter filter = new FileNameExtensionFilter("image file", "jpg", "png",
			"gif", "bmp");
	
	private ControlPanel controlPanel = new ControlPanel(this, 200, 600);
	private Ticker timeTicker = new Ticker(controlPanel);
	Thread timeThread = new Thread(timeTicker);
	private PuzzlePanel puzzlePanel = new PuzzlePanel(600, 600);
	private Ticker puzzleTicker = new Ticker(puzzlePanel);
	Thread puzzleThread = new Thread(puzzleTicker);
	private StateMachine stateMachine = new StateMachine(this, controlPanel, timeTicker, puzzlePanel, puzzleTicker);
	Ranking ranks = new Ranking(this);
	
	BufferedImage image = null;
	
	private int width = 800, height = 600;
	int rows = 0, columns = 0;

	public SliderPuzzle()
	{
		super("Slider Puzzle");

		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(filter);

		puzzlePanel.addStateMachine(stateMachine);
		puzzlePanel.addPuzzleTicker(puzzleTicker);
		
		initializeMenuBar();
		
		initFrame();
	}

	public static void main(String[] s)
	{
		SliderPuzzle frame = new SliderPuzzle();

		frame.timeThread.start();
		frame.puzzleThread.start();
		
	}
	
	private void initFrame()
	{
		this.setLayout(null);
		this.setLocation(200, 200);
		this.setSize(new Dimension(800, 600));
		this.getContentPane().setBackground(Color.black);
		this.setVisible(true);
		
		this.add(controlPanel);
		controlPanel.addStateMachine(stateMachine);
		this.add(puzzlePanel);
		
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		
		this.addComponentListener(this);
		this.addWindowStateListener(new WindowStateListener() {
			   public void windowStateChanged(WindowEvent arg0) {
				   componentResized(null);
			   }
		});
		
	}

	private void initializeMenuBar()
	{
		this.setJMenuBar(menuBar);

		menuBar.add(menu);

		menu.add(loadPicture);
		menu.add(gridInput);

		menuBar.setVisible(true);
		menuBar.setEnabled(true);

		JFrame frame = this;

		loadPicture.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int returnVal = fc.showOpenDialog(frame);

				if (returnVal == JFileChooser.APPROVE_OPTION)
				{
					File file = fc.getSelectedFile();
					try {
					    image = ImageIO.read(file);
					    puzzlePanel.setImage(image);
					} catch (IOException imageError) { imageError.printStackTrace();}
				}
			}
		});

		SliderPuzzle parent = this;
		
		gridInput.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				InputDialog dialog = new InputDialog(parent);
				dialog.setVisible(true);
			}
		});
		
	}

	@Override
	public void componentResized(ComponentEvent arg0)
	{	
		width = this.getWidth();
		height = this.getHeight();
		
		controlPanel.changeSize(200, height - 61);
		controlPanel.setLocation(width - 216, 0);
		
		puzzlePanel.changeSize(width - 216, height - 61);
		//puzzlePanel.setLocation(0, 0);
	}
	
	@Override
	public void componentHidden(ComponentEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

}
