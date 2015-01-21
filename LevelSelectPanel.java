import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LevelSelectPanel extends JPanel implements KeyListener, GameConstants{
	private int selectedLevel;
	private int selection;
	private int numPlayers;
	
	//since the white rectangle around the level selected wasn't perfectly rectangular, we had to manually draw the thick rectangle
	private int[][] borderVal = {{34, 68, 134, 140}, {178, 68, 139, 140}, {327, 68, 139, 140}, {476, 68, 140, 140}, {626, 68, 136, 140}, {34, 218, 134, 139}, {178, 218, 139, 139}, {327, 218, 139, 139}, {476, 218, 140, 139}, {626, 218, 136, 139}};

	public LevelSelectPanel(){
		numPlayers = 0;
		selection = 0;
		selectedLevel = 0;
		this.addKeyListener(this);
	}
	
	public void paintComponent(Graphics g){    // selection: 0 - level select   1 - numPlayers select   2 - PLAY
		super.paintComponent(g);						// numPlayers  0    1    2
		if (selection == 0){
			g.drawImage(LEVELSELECT_IMGS[selection].getImage(), 0, 0, null);	
		}else if (selection == 1){
			g.drawImage(LEVELSELECT_IMGS[numPlayers].getImage(), 0, 0, null);    
		}else{
			g.drawImage(LEVELSELECT_IMGS[selection+numPlayers].getImage(), 0, 0, null);    
			System.out.println(selection+" "+numPlayers);
		}
		
		//draw a white border around the currently selected level
		g.setColor(new Color(255,255,255));
		g.fillRect(borderVal[selectedLevel][0], borderVal[selectedLevel][1], borderVal[selectedLevel][2]+6, 4);
		g.fillRect(borderVal[selectedLevel][0], borderVal[selectedLevel][1]+4, 4, borderVal[selectedLevel][3]+2);
		g.fillRect(borderVal[selectedLevel][0]+borderVal[selectedLevel][2]+2, borderVal[selectedLevel][1]+4, 4, borderVal[selectedLevel][3]+2);
		g.fillRect(borderVal[selectedLevel][0]+4, borderVal[selectedLevel][1]+borderVal[selectedLevel][3]+2, borderVal[selectedLevel][2]-2, 4);
		
		repaint();
	}
	
	//changes level selected and the selection depending on what keys are pressed
	public void keyPressed(KeyEvent e){
		if (selection == 0){
			if (e.getKeyCode() == KeyEvent.VK_LEFT){
				SoundEffect.SELECT.play();		
				selectedLevel--;
				if (selectedLevel == -1)
					selectedLevel = 4;
				else if (selectedLevel == 4)
					selectedLevel = 9;	
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
				SoundEffect.SELECT.play();		
				selectedLevel++;
				if (selectedLevel == 5)
					selectedLevel = 0;
				else if (selectedLevel == 10)
					selectedLevel = 4;		
			} else if (e.getKeyCode() == KeyEvent.VK_UP){
				SoundEffect.SELECT.play();		
				selectedLevel-=5;
				if (selectedLevel<0)
					selectedLevel+=10;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN){
				SoundEffect.SELECT.play();		
				selectedLevel+=5;
				if (selectedLevel>9)
					selectedLevel-=10;
			}	
		} else if (selection == 1){
			if (e.getKeyCode() == KeyEvent.VK_LEFT){
				SoundEffect.SELECT.play();		
				numPlayers--;
				if (numPlayers == 0)
					numPlayers = 2;
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
				SoundEffect.SELECT.play();		
				numPlayers++;
				if (numPlayers == 3)
					numPlayers = 1;
			}else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
				SoundEffect.SELECT.play();		
				selection--;
				numPlayers = 0;
			}
		} else if (selection == 2){
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
				SoundEffect.SELECT.play();		
				selection--;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE){
			SoundEffect.SELECT.play();		
			if (selection == 0)
				numPlayers = 1;
			else if (selection == 2){
				System.out.println("LEVEL START!!!!!!");     /// Game Start
				selection = 0;
				Panel.changeCard(4);
			}
			selection++;
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
			Panel.changeCard(0);
		}
		repaint();
	}
	
	public void keyReleased(KeyEvent e){ 
	}
	
	public void keyTyped(KeyEvent e){
		
	}
	
	public void resetLevelSelect(){
		selectedLevel = 0;
		selection = 0;
	}
	public int getNumPlayers(){
		return numPlayers;
	}
	public int getSelectedLevel(){
		return selectedLevel;
	}
}
