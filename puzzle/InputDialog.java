package puzzle;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class InputDialog extends JDialog
{
	static final int minRow = 2;
	static final int minCol = 2;
	
	static final int maxRow = 20;
	static final int maxCol = 20;
	
	private JLabel rowLabel = new JLabel("Br. vrsta");
	private JLabel colLabel = new JLabel("Br. kolona");
	
	private JTextField rowField = new JTextField();
	private JTextField colField = new JTextField();
	
	private JButton done = new JButton("Gotovo");
	
	
	public InputDialog(SliderPuzzle parent)
	{
		super.setLocationRelativeTo(parent);
		this.setSize(200, 300);
		this.setLayout(new GridLayout(5, 1));
		this.setResizable(false);
		this.setModal (true);
		this.setModalityType (ModalityType.APPLICATION_MODAL);
		
		this.add(rowLabel);
		this.add(rowField);
		
		this.add(colLabel);
		this.add(colField);
		
		this.add(done);
		
		//parent.setEnabled(false);
		
		InputDialog dialog = this;
		
		done.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int rows = 0;
				int columns = 0;
				
				boolean validRow = true, validCol = true;
				
				try
				{
					rows = Integer.valueOf(rowField.getText());
					if( rows < minRow || rows > maxRow) throw new Exception();
				}
				catch(Exception invalidPar1)
				{
					validRow = false;
				}
				
				try
				{
					columns = Integer.valueOf(colField.getText());
					if( columns < minCol || columns > maxCol) throw new Exception();
				}
				catch(Exception invalidPar1)
				{
					validCol = false;
				}
				
				if(!validRow) {rowField.setText("Unesite ceo broj izmedju " + minRow + " i " + maxRow);}
				
				if(!validCol) {colField.setText("Unesite ceo broj izmedju " + minCol + " i " + maxCol);}
				
				if(!validRow || !validCol) { return; }
				
				parent.rows = rows;
				parent.columns = columns;
				
				//parent.setEnabled(true);
				
				dialog.dispose();
			}
		});
		
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				//parent.setEnabled(true);
				
				dialog.dispose();
			}
		});
	}
	
}
