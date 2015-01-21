import java.awt.*;

public class Portal extends Rectangle implements GameConstants{
	
	private int destination; //the destination (index) of portal
	
	public Portal(int x, int y, int destination){
		super(x, y, PORTAL_IMG.getIconWidth(), PORTAL_IMG.getIconHeight());
		this.destination = destination;
	}
	
	public void draw(Graphics g){
		g.drawImage(PORTAL_IMG.getImage(), x, y, null);
	}
	
	//returns which portal the portal leads to
	public int getDestination(){
		return destination;
	}
	
}
