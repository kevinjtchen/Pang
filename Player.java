import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class Player extends MovingObject implements GameConstants, ActionListener{
	private boolean shooting;
	private int spawnx;
	private int spawny;
	private int lives;
	private int score;
	private int playerNum;
	private int weaponType; //normal 0 , double 1, machinegun 2, sticky if time 3
	private int playerState; //used to draw different sprites
    private ArrayList<Bullet> bullets;
    
	private boolean shield; //done later
	private boolean invincible;
	private boolean showShield;
	private boolean showPlayer;
	private int shieldTimeLeft;
	private int invincibleTimeLeft;
    private Timer spriteTimer;
    private Timer shieldTimer;
    private boolean dead;
	
	public Player(int playerNum, int x, int y){
		super(x, y);
		shooting = false;
		spawnx = x;
		spawny = y;
		this.playerNum = playerNum;
		score = 0;
		lives = PLAYER_LIVES;
		width = PLAYER_WIDTH;
		height = PLAYER_HEIGHT;
		velx = 0;
		vely = 0;
		accy = GAME_GRAVITY*0.5;
		weaponType = 0;
		bullets = new ArrayList<Bullet>();
		
		showPlayer = true;
		shield = false;
		invincible = false;
		spriteTimer = new Timer (80, this);
		shieldTimer = new Timer (80, this);
		spriteTimer.start();
		shieldTimer.setInitialDelay(13000);
	}
	
	public void draw(Graphics g){
		if(!dead){
			if (showShield) //((shieldTimeLeft%200 == 0) || shieldTimeLeft < 5000)
				g.drawImage(PLAYER_SHIELD.getImage(), (int)x-5, (int)y-5, width+10, height+10, null);
			if (showPlayer){
				if (velx > 0)
					g.drawImage(PLAYER_IMGS[playerState+7*playerNum].getImage(), (int)x, (int)y, width, height, null); //playerNum * 5 assumes the player has 5 states - refine how the sprite cycle system works
				else if (velx < 0)
					g.drawImage(PLAYER_IMGS[playerState+7*playerNum].getImage(), (int)x+width, (int)y, (int)x, (int)y+height, 0, 0, PLAYER_IMGS[playerState].getIconWidth(), PLAYER_IMGS[playerState].getIconHeight(), null);
				else if (shooting)
					g.drawImage(PLAYER_IMGS[6+7*playerNum].getImage(), (int)x, (int)y, width, height, null); //playerNum * 5 assumes the player has 5 states - refine how the sprite cycle system works
				else
					g.drawImage(PLAYER_IMGS[0+7*playerNum].getImage(), (int)x, (int)y, width, height, null); //playerNum * 5 assumes the player has 5 states - refine how the sprite cycle system works
			}
		}else{
			g.drawImage(PLAYER_DEAD[playerNum].getImage(), (int)x, (int)y, height+10, width+10, null);
		}
			
	}	
	
	public void setDirection(int direction){
		velx = 5*direction;
	}
	
	public void travel(){ //*** INCOMPLETE *** 
		// IF BOTTOM OF PLAYER IS NOT COLLIDING WITH PLATFORM OR GROUND, do the following:
		vely = vely + accy;
		if (y+PLAYER_HEIGHT+vely > 437 && !dead){
			vely = 0;
		}
		if (x+PLAYER_WIDTH > 790){
			velx = 0;
			x = 790-PLAYER_WIDTH;
		}
		if (x < 12){
			velx = 0;
			x = 12;
		}
	}
	
	public void shoot(){
		if (vely == 0){
			if (weaponType == 0 && bullets.size() < 1){  //weapon type: single harpoon 
				bullets.add(new Bullet(0, (int)x+17, (int)y+PLAYER_HEIGHT));
				shootAnimation();
				SoundEffect.HARPOON.play();
			}else if (weaponType == 1 && bullets.size() < 2){ //weapon type: double harpoon
				bullets.add(new Bullet(0, (int)x+17, (int)y+PLAYER_HEIGHT));
				shootAnimation();
				SoundEffect.HARPOON.play();
			}else if (weaponType == 2){ //weapon type: machinegun, shoots two bullets
				bullets.add(new Bullet(1, (int)x+6, (int)y));
				bullets.add(new Bullet(1, (int)x+32, (int)y));
				shootAnimation();
				SoundEffect.LASER.play();
			}
		}
	}
	
	public boolean hasShield(){
		return shield;
	}
	
	public void shootAnimation(){
		shooting = true;
		velx = 0;
	}
	public void setShooting(boolean shoot){
		shooting = shoot;
	}

	public void hit(){ //to be programmed later here: do not remove life if shield is true
		playerState = 3;
		if (lives != 0)
			lives--;
	}
	
	public void spawn(){
		x = spawnx;
		y = spawny;
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public double getVelX(){
		return velx;
	}
	public double getVelY(){
		return vely;
	}
	public int getPlayerNum(){
		return playerNum;
	}
	public int getLives(){
		return lives;
	}
	public int getScore(){
		return score;
	}
	public void loseLife(){
		lives--;
	}
	
	public int inPortal(ArrayList<Portal> portals){ //returns destination portal index, -1 if not in portal
		for (int i = 0; i<portals.size(); i++){
			if (x > portals.get(i).x && x+width < portals.get(i).x+portals.get(i).width){
				if (y > portals.get(i).y && y+height < portals.get(i).y + portals.get(i).height)
					return portals.get(i).getDestination();
			}
		}	
		return -1;
	}
	
	public void teleport(int x, int y){
		this.x = x+10;
		this.y = y+10;
	}
	
	public ArrayList<Bullet> getBullets(){
		return bullets;
	}
	public void removeBullet(int index){
		bullets.remove(index);
	}
	public void setLives(int lives){
		this.lives = lives;
	}
	public void addScore(int score){
		this.score += score;
	}
	public void setScore(int score){
		this.score = score;
	}
	public void setSpawn(int spawnx, int spawny){
		this.spawnx = spawnx;
		this.spawny = spawny;
	}
	public void setWeaponType(int type){ //consider removing all bullets whenever weapon is changed
		this.weaponType = type;
	}
	public boolean isInvincible(){
		return invincible;
	}
	
	public void setX(double inp){
		x = inp;
	}
	public void setY(double inp){
		y = inp;
	}
	public void setVelY(double inp){
		vely = inp;
	}
	public void setVelX(double inp){
		velx = inp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == spriteTimer){
			if (showPlayer && invincible)
				showPlayer = false;
			else
				showPlayer = true;
			if (invincible){
				invincibleTimeLeft += 100;
				if (invincibleTimeLeft > 2000){
					invincible = false;
					invincibleTimeLeft = 0;
				}
			}
			
			playerState += 1;		
			if (playerState == 6)
				playerState = 1;
		}
		if (e.getSource() == shieldTimer){
			if (showShield)
				showShield = false;
			else
				showShield = true;

			shieldTimeLeft += 100;
			if (shieldTimeLeft > 2000){
				shield = false;
				SoundEffect.SHIELDDOWN.play();
				showShield = false;
				shieldTimer.stop();
			}
		}
	}
	
	public void setShield(boolean input) {
		if (input){
			shield = true;
			showShield = true;
			shieldTimeLeft = 0;
			if (shield)
				shieldTimer.stop();
			shieldTimer.start();
		}else{
			shield = false;
			showShield = false;
			shieldTimer.stop();
		}
	}
	public void giveInvincible(){
		invincible = true;
		invincibleTimeLeft = 0;
	}
	public void setDead(boolean input){
		dead = input;
	}
	
}
