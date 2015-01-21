import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MenuPanel extends JPanel implements KeyListener, GameConstants{
	//initialize variable
	private int selectedButton;
	
	//constructor
	public MenuPanel(){
		//initial selected button will be the first button
		selectedButton = 0;
		
		this.addKeyListener(this);
	}
	
	//paint component class
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//draw the menu image
		//draws a different image depending on which option is selected (selected option will be highlighted)
		g.drawImage(MENU_IMGS[selectedButton].getImage(), 0, 0, null);
	}
	
	public void keyTyped(KeyEvent e){
	}
	
	//keypressed method
	public void keyPressed(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE)
			SoundEffect.SELECT.play();		
			//when the up/down key is pressed, the selection changes
		//the selection will loop back up/down if the user presses up on the top choice/down on the bottom choice
		if (e.getKeyCode() == KeyEvent.VK_UP){
			selectedButton--;
			if (selectedButton == -1){
				selectedButton = 2;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN){
			selectedButton = (selectedButton+1)%3;
		}if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE){
			//if the user selects enter or space, the selected option will be selected
			Panel.changeCard(selectedButton+1);
		}
		repaint();
	}
	
	public void keyReleased(KeyEvent e){
	}
	
	//method resets the selected button to the first button
	public void resetMenu(){
		selectedButton = 0;
	}
}