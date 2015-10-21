package puzzle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Ranking
{

	private SliderPuzzle sliderPuzzle;
	
	private final static int numOfRanks = 10;
	private static final int resultsSize = 20;
	
	private String newName = null;
	private String topPlayers[] = new String[numOfRanks];
	
	
	public Ranking(SliderPuzzle sliderPuzzle)
	{
		this.sliderPuzzle = sliderPuzzle;
	}
	
	public boolean isBestTime(long time, int rows, int columns)
	{
		boolean timeFound = false, foundDim = false;
		
		try
		{
			FileReader fr = new FileReader("Times.txt");

			BufferedReader br = new BufferedReader(fr);
			
			String str;
			
			while( (str = br.readLine()) != null)
			{
				if(str.compareTo("dim") == 0)
				{
					str = br.readLine();
					if( Integer.parseInt(str) == rows)
					{
						str = br.readLine();
						if( Integer.parseInt(str) == columns)
						{
							foundDim = true;
							
							str = br.readLine(); //empty line
							
							for(int i = 0; i < numOfRanks; i++)
							{
								str = br.readLine(); //time
								if(str != null)
								{
									long tmpTime = Long.parseLong(str);
									
									if( time < tmpTime)
									{
										timeFound = true;
										break;
									}
									str = br.readLine(); //name
								}
								else
								{
									timeFound = true;
									break;
								}
							}
							if(timeFound) break;
						}
					}
				}
			}
			
			br.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		if(!foundDim) return true;
		return timeFound;
	}

	public void updateTimes(String name, long time, int rows, int columns)
	{
		try
		{
			FileReader fr = new FileReader("Times.txt");

			BufferedReader br = new BufferedReader(fr);
			
			StringBuffer buffer = new StringBuffer("");
			String str;
			
			boolean foundDim = false;
			
			while( (str = br.readLine()) != null)
			{
				if(str.compareTo("dim") == 0)
				{
					buffer.append(str + "\n");
					
					str = br.readLine();
					buffer.append(str + "\n");
					
					if( Integer.parseInt(str) == rows)
					{
						str = br.readLine();
						buffer.append(str + "\n");
						
						if( Integer.parseInt(str) == columns)
						{
							foundDim = true;
							
							str = br.readLine();
							buffer.append(str + "\n");  //empty line
							
							boolean changed = false;
							
							for(int i = 0; i < numOfRanks; i++)
							{
								str = br.readLine();
								if(str != null)
								{
									if( !changed )
									{
										long tmpTime = Long.parseLong(str);
										
										if( time < tmpTime)
										{
											buffer.append(time);
											buffer.append("\n");
											if( i != numOfRanks - 1) buffer.append(" ");
											buffer.append(Integer.toString(i + 1) + ". ");
											buffer.append(name);
											String dots = new String("........................................"); //40 dots
											buffer.append(dots, name.length(), resultsSize );
											buffer.append( convertTime(time) + "\n");
											changed = true;
										}
									}
									if(!(i == numOfRanks - 1 && changed))
									{
										buffer.append(str + "\n"); //time
										str = br.readLine();
										if(changed)
										{
											str = str.substring(2, str.length());
											if( i != numOfRanks - 2) buffer.append(" ");
											buffer.append(Integer.toString(i + 2));
										}
										buffer.append(str + "\n"); //name
									}
									else str = br.readLine(); //discard last name
								}
								else if( !changed )
								{
									buffer.append(time);
									buffer.append("\n");
									if( i != numOfRanks - 1) buffer.append(" ");
									buffer.append(Integer.toString(i + 1) + ". ");
									buffer.append(name);
									String dots = new String("........................................"); //40 dots
									buffer.append(dots, name.length(), resultsSize );
									buffer.append( convertTime(time) + "\n");
									changed = true;
									break;
								}
							}
						}
					}
				}
				else buffer.append(str + "\n");
			}
			
			br.close();
			
			if(!foundDim)
			{
				buffer.append("\n" + "dim" + "\n");
				buffer.append(Integer.toString(sliderPuzzle.rows) + "\n");
				buffer.append(Integer.toString(sliderPuzzle.columns) + "\n");
				buffer.append("\n" + Long.toString(time) + "\n");
				buffer.append(" 1. ");
				buffer.append(name);
				String dots = new String("........................................"); //40 dots
				buffer.append(dots, name.length(), resultsSize );
				buffer.append( convertTime(time) + "\n");
			}
			
			FileWriter fw=new FileWriter(new File("Times.txt"));
			fw.write(buffer.toString());
			fw.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void getTopPlayers(int rows, int columns)
	{
		try
		{
			FileReader fr = new FileReader("Times.txt");

			BufferedReader br = new BufferedReader(fr);
			
			String str;
			
			while( (str = br.readLine()) != null)
			{
				if(str.compareTo("dim") == 0)
				{
					str = br.readLine();
					if( Integer.parseInt(str) == rows)
					{
						str = br.readLine();
						if( Integer.parseInt(str) == columns)
						{
							str = br.readLine(); //empty line
							
							for(int i = 0; i < numOfRanks; i++)
							{
								str = br.readLine();
								if( str != null)
								{
									str = br.readLine();
									topPlayers[i]  = str;
								}
								else break;
							}
						}
					}
				}
			}
			
			br.close();
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private String convertTime(long time)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		
		int min = (int) (time / 60 / 1000);
		if(min < 10) sb.append("0");
		sb.append(Integer.toString(min));
		
		sb.append(" : ");
		
		int sec = (int) ( (time / 1000) % 60);
		if(sec < 10) sb.append("0");
		sb.append(Integer.toString(sec));
		
		return sb.toString();
	}
	
	public void onGameCompleted(long time)
	{
		if(isBestTime(time, sliderPuzzle.rows, sliderPuzzle.columns))
		{
			InputNameDialog nameDialog = new InputNameDialog(sliderPuzzle, this);
			nameDialog.setVisible(true);
			if( newName == null || newName.isEmpty()) newName = "AAA";
			updateTimes(newName, time, sliderPuzzle.rows, sliderPuzzle.columns);
		}
		
		for(int i = 0; i < numOfRanks; i++)
			topPlayers[i] = null;
		
		getTopPlayers(sliderPuzzle.rows, sliderPuzzle.columns);
		
		RankingsDialog rankDialog = new RankingsDialog(sliderPuzzle, this);
		rankDialog.setVisible(true);
	}
	
	
	
	private class InputNameDialog extends JDialog
	{
		private JLabel label = new JLabel("Unesite vase ime");
		private JTextField textField = new JTextField();
		private JButton button = new JButton("Gotovo");
		
		public InputNameDialog(SliderPuzzle parent, Ranking ranks)
		{
			super.setLocationRelativeTo(parent);
			this.setSize(200, 150);
			this.setLayout(new GridLayout(3, 1));
			this.setResizable(false);
			this.setModal (true);
			this.setModalityType (ModalityType.APPLICATION_MODAL);
			
			this.add(label);
			this.add(textField);
			this.add(button);
			
			InputNameDialog dialog = this;
			
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ranks.newName = textField.getText();
					if(newName.length() > resultsSize)
						newName = newName.substring(0, resultsSize); 
					
					if( newName.isEmpty()) newName = "AAA";
					
					dialog.dispose();
				}
			});
			
			this.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					ranks.newName = textField.getText();
					if(newName.length() > resultsSize)
						newName = newName.substring(0, resultsSize); 
					
					if( newName.isEmpty()) newName = "AAA";
					
					dialog.dispose();
				}
			});
		}
	}
	
	private class RankingsDialog extends JDialog
	{
		public RankingsDialog(SliderPuzzle parent, Ranking ranks)
		{
			super.setTitle("Najbolji rezultati");
			super.setLocationRelativeTo(null);
			this.setSize(400, 500);
			this.setLayout(new GridLayout(ranks.numOfRanks, 1));
			this.setResizable(false);
			this.setModal (true);
			this.setModalityType (ModalityType.APPLICATION_MODAL);
			this.setBackground(Color.white);
			
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screenSize = toolkit.getScreenSize();
			int x = (screenSize.width - this.getWidth()) / 2;
			int y = (screenSize.height - this.getHeight()) / 2;
			this.setLocation(x, y);
			
			for(int i = 0; i < numOfRanks; i++)
			{
				if( topPlayers[i] != null)
				{
					JLabel tmpLabel = new JLabel(topPlayers[i]);
					tmpLabel.setFont(new Font("monospaced", Font.BOLD,20));
					this.add(tmpLabel);
				}
				else this.add(new JLabel());
			}
			
			RankingsDialog dialog = this;
			
			this.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					dialog.dispose();
				}
			});
		}
	}
	
}
