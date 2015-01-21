import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class PowerUp extends MovingObject implements GameConstants, ActionListener{
	private int type;
	private Timer spawnTimer;
	private boolean show;
	private boolean expired;
	private int timeLeft;

	//constructor
	public PowerUp(int type, int x, int y){
		super(x, y);
		show = true;
		expired = false;
		this.type = type;
		spawnTimer = new Timer(100, this);
		spawnTimer.setInitialDelay(3000);
		velx = 0;
		vely = 3;
		width = POWERUPS_IMGS[type].getIconWidth();
		height = POWERUPS_IMGS[type].getIconHeight();
	}
	
	public void draw(Graphics g) {
		if (show)
			g.drawImage(POWERUPS_IMGS[type].getImage(), (int)x, (int)y, width, height, null);
	}
	
	//moves the powerup down
	public void travel(){
		if (y+POWERUPS_IMGS[type].getIconHeight() > 433){
			vely = 0;
			spawnTimer.start();
		}	
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	public int getType(){
		return type;
	}

	public boolean isExpired(){
		return expired;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == spawnTimer){
			if (show)
				show = false;
			else
				show = true;
			timeLeft += 100;
			if (timeLeft > 3000)
				expired = true;
		}
	}
}