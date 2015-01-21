import java.awt.*;

public class Platform extends Rectangle implements GameConstants{
	
	private boolean breakable;
	
	public Platform(boolean breakable, int x, int y, int width, int height){
		super(x, y, width, height);
		this.breakable = breakable;
	}
	
	//draws the platform horizontally or vertically depending on whether the width or height is greater
	public void draw(Graphics g){
		if (breakable){
			if (width>height)
				g.drawImage(PLATFORM_IMG1.getImage(), x, y, width, height, null);
			else
				g.drawImage(PLATFORM_IMG1B.getImage(), x, y, width, height, null);				
		}else
			if (width>height)
				g.drawImage(PLATFORM_IMG2.getImage(), x, y, width, height, null);
			else
				g.drawImage(PLATFORM_IMG2B.getImage(), x, y, width, height, null);
	}

	public boolean isBreakable(){
		return breakable;
	}

}
