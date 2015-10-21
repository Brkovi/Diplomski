package puzzle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import puzzle.StateMachine.State;

public class ControlPanel extends JPanel implements Updatable
{
	
	private SliderPuzzle sliderPuzzle;
	
	JButton start = new JButton("START");
	JButton mix = new JButton("PROMESAJ");
	JButton again = new JButton("IZ POCETKA");
	JButton pause = new JButton("PAUZIRAJ");
	JButton stop = new JButton("PREKINI");
	JLabel timeLabel = new JLabel();
	
	private long time = 0; //in milisec
	
	private int width, height;
	
	private StateMachine stateMachine = null;
	
	private boolean paused = false;

	public ControlPanel(SliderPuzzle p,int width, int height)
	{
		sliderPuzzle = p;
		
		this.width = width;
		this.height = height;
		this.setLayout(null);
		this.setPreferredSize(new Dimension(width, height));
		
		this.setBackground(Color.gray);
		
		init();
	}
	
	private void init()
	{
		this.add(start);
		this.add(mix);
		this.add(again);
		this.add(pause);
		this.add(stop);
		this.add(timeLabel);
		
		timeLabel.setFont(new Font("Serif", Font.BOLD,37));
		
		start.setSize(110, 30);
		mix.setSize(110, 30);
		timeLabel.setSize(110, 30);
		again.setSize(110, 30);
		pause.setSize(110, 30);
		stop.setSize(110, 30);
		
		showTime();
		
		changeSize(width, height);
		
		start.setEnabled(true);
		mix.setEnabled(false);
		again.setEnabled(false);
		pause.setEnabled(false);
		stop.setEnabled(false);
		
		start.addActionListener(startListener);
		mix.addActionListener(mixListener);
		again.addActionListener(againListener);
		pause.addActionListener(pauseListener);
		stop.addActionListener(stopListener);
	}
	
	public void changeSize(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		this.setSize(width, height);
		
		start.setLocation((width - start.getWidth()) / 2, 20);
		mix.setLocation((width - mix.getWidth()) / 2, 20 + height / 6);
		timeLabel.setLocation((width - timeLabel.getWidth()) / 2, 40 + 2 * height / 6);
		again.setLocation((width - again.getWidth()) / 2, 50 + 3 * height / 6);
		pause.setLocation((width - pause.getWidth()) / 2, 20 + 4 * height / 6);
		stop.setLocation((width - stop.getWidth()) / 2, -10 + 5 * height / 6);
	}
	
	@Override
	public void update(long dt)
	{
		addTime(dt);
		showTime();
	}
	
	public void setTime(long t) 	{ time = t; }
	public long getTime() 			{ return time; }
	public void addTime(long dt) 	{ time += dt; }
	
	public void showTime()
	{
		StringBuilder sb = new StringBuilder();
		
		int min = (int) (time / 60 / 1000);
		if(min < 10) sb.append("0");
		sb.append(Integer.toString(min));
		
		sb.append(" : ");
		
		int sec = (int) ( (time / 1000) % 60);
		if(sec < 10) sb.append("0");
		sb.append(Integer.toString(sec));
		
		String s = sb.toString();
		
		timeLabel.setText(s);
	}

	public void addStateMachine(StateMachine sm) { stateMachine = sm; }
	
	private ActionListener startListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if(stateMachine != null)
				stateMachine.executeState();
		}
	};
	
	private ActionListener mixListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if(stateMachine != null)
				stateMachine.executeState();
		}
	};

	private ActionListener againListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			setTime(0);
			showTime();
			
			stateMachine.state = State.INIT;
			stateMachine.executeState();
		}
	};
	private ActionListener pauseListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			stateMachine.state = State.PAUSE;
			stateMachine.executeState();
			
			paused = !paused;
			
			if(paused) pause.setText("NASTAVI");
			else pause.setText("PAUZIRAJ");
		}
	};
	private ActionListener stopListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			stateMachine.state = State.STOP;
			stateMachine.executeState();
		}
	};
}
