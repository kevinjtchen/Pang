import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Monster extends MovingObject implements GameConstants, ActionListener{
	private int monsterState;
	private Timer spriteTimer;
	
	public Monster(int x, int y){
		super(x,y);
		spriteTimer = new Timer(100,this);
		spriteTimer.start();
		velx = 0;
		vely = 0;
		width = MONSTER_IMGS[0].getIconWidth();
		height = MONSTER_IMGS[0].getIconHeight();
		monsterState = 0;
	}
	
	public void draw(Graphics g){
		g.drawImage(MONSTER_IMGS[monsterState].getImage(), (int)x, (int)y, width, height, null);
	}
	
	//cycles through the sprites for every tick
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == spriteTimer){
			monsterState += 1;
			if (monsterState == 4)
				monsterState = 0;
		}
	}
	
	public void setDirection(double velx){
		this.velx = 5*velx;
	}
	
	public int getX(){
		return (int)x;
	}
	public int getY(){
		return (int)y;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
}
