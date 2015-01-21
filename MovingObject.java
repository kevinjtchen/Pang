import java.awt.*;
import javax.swing.*;

//abstract class for all moving objects: monster, ball, bullet, player
public abstract class MovingObject{
	protected double x;
	protected double y;
	protected double velx;
	protected double vely;
	protected double accy;
	protected int width;
	protected int height;
	
	public MovingObject(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void move() {
		x = x + velx;
		y = y + vely;
	}
	
	public abstract void draw(Graphics g);
	
}
