import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InstructionsPanel extends JPanel implements KeyListener, GameConstants{
	
	private int selection;
	
	public InstructionsPanel(){
		this.addKeyListener(this);
		selection = 0;
	}
	
	//draws pictures, depending on the selection
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (selection == 0)
			g.drawImage(MENU_INSTRUCTIONS[selection].getImage(), 0, 0, null);
		else
			g.drawImage(MENU_INSTRUCTIONS[selection].getImage(), 0, 0, null);
	}
	
	public void keyPressed(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT){
			selection++;
			if (selection > 1)
				selection = 0;
			SoundEffect.SELECT.play();
		}else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE){
			System.out.println(selection);
			SoundEffect.SELECT.play();
			Panel.changeCard(selection);
		}
	}
	
	public void keyReleased(KeyEvent e){
	}
	
	public void keyTyped(KeyEvent e){
	}
}
