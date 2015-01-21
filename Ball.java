import java.awt.Graphics;
import java.util.ArrayList;

public class Ball extends MovingObject implements GameConstants{
	
	private int radius;
	private int size;
	private int direction;
	
	public Ball(int size, double vely, int x, int y, int direction){		
		super(x, y);
		this.size = size;
		this.vely = vely;

		radius = BALL_RADII[size];
		accy = GAME_GRAVITY*.1;
		this.direction = direction; 
		velx = direction*2.4;
		
	}

	public void travel(){
		//applies gravity 
		vely = vely + accy;
		
		//checks for floor & wall bounces
		if (y+radius*2>432)
			vely= -1*vely + accy;
		if (x<15 || x>785-radius*2)
			velx = -1*velx;

	}

	public int getSize(){
		return size;
	}
	public void draw(Graphics g) {
		g.drawImage(BALL_IMG.getImage(), (int)x, (int)y, radius*2, radius*2, null);		
	}

	public double getY() {
		return y;
	}
	public double getX(){
		return x;
	}
	public int getRadius(){
		return radius;
	}
	public double getVelX(){
		return velx;
	}
	public double getAccY(){
		return accy;
	}
	public double getVelY(){
		return vely;
	}
	public int getDirection(){
		return direction;
	}
	public void setVelX(double velx){
		this.velx = velx;
	}
	public void setVelY(double vely){
		this.vely = vely;
	}
}
