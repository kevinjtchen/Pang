import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Bullet extends MovingObject implements GameConstants{
	
	private int type;
	private int cropHeight; //cropHeight allows the full length harpoon image to be drawn correctly at different heights
	
	public Bullet(int type, int x, int y){ //type 0 is regular harpoon, type 1 is laser 
		super(x, y);
		velx = 0;
		if (type == 0){
			width = HARPOON_IMG.getIconWidth();
			height = HARPOON_IMG.getIconHeight();
		}else{
			width = LASER_IMG.getIconWidth();
			height = LASER_IMG.getIconHeight();
		}
		vely = BULLET_SPEEDS[type];
		this.type = type;
	}

	public void draw(Graphics g){
		if (type == 0){
			g.drawImage(HARPOON_IMG.getImage(), (int)x, (int)y, (int)x+width, (int)y+2*cropHeight, 0, 0, width, 2*cropHeight, null);
		} else{
			g.drawImage(LASER_IMG.getImage(), (int)x, (int)y, null);
		}
	}

	public void travel(){
		y = y + vely;
		cropHeight -= vely; //vely is negative, cropHeight increases, increasing the amount of harpoon.png drawn
	}
	public double getY(){
		return y;
	}
	public double getX(){
		return x;
	}
	
	public int getCropHeight(){
		return cropHeight;
	}
}
