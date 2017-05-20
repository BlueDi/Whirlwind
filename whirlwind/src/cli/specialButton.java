package cli;

import javax.swing.JButton;

public class specialButton extends JButton{
int row;
int col;
	public specialButton(int r,int c){
		row=r;
		col=c;
	}
	public int getRow(){
		return row;
	}
	public int getCol(){
		return col;
	}
}
