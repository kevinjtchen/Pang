import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


import javax.swing.*;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener, GameConstants{
	
	//set arraylists, arrays, files, file readers, and variables
	private ArrayList<PowerUp> powerUps;
	private ArrayList<Player> players;
	private ArrayList<Ball> balls;
	private ArrayList<Platform> platforms;
	private ArrayList<Monster> monsters;
	private ArrayList<Portal> portals;
	
	private File loadFile;
	private BufferedReader br;
	
	private boolean noTime; // if time has run out
	private boolean split; // if balls have been split
	private boolean frozen; // if time is frozen
	private boolean showBalls; // used to show when balls are about to resume movement 
	private int freezeLeft; //used to track time left during motion freezes
	private Timer freezeTimer; 
	private boolean [] deadPlayers; //used to keep track of who lost lives each round
	
	private int level;
	private int numBalls;
	private int numPlatforms;
	private int numPortals;
	private int numMonsters;
	private double percentTime; // from 0 to 100, used to draw the time bar
	private boolean twoPlayer;
	private FontMetrics fontMetrics; //used to right align text
	
	private int [] playerXspawn;
	private int [] playerYspawn;
	private int [] ballXspawn;
	private int [] ballYspawn;
	private int [] ballSize;
	private int [] ballDirection;
	private int [] platformXspawn;
	private int [] platformYspawn;
	private int [] platformWidth;
	private int [] platformHeight;
	private boolean [] platformBreakable;
	private int [] portalXspawn;
	private int [] portalYspawn;
	private int [] portalDestinations;
	private int [] monsterXspawn;
	private int [] monsterYspawn;

	//following are used to determine nextLevel, retryLevel and endGame conditions
	private boolean levelComplete;
	private boolean manDown;
	private boolean victory;
	private boolean gameStart;
	
	private Timer clock;
	
	//constructor
	public GamePanel(){
		
		//initialize ArrayLists and variables
		freezeTimer = new Timer(100, this);
		deadPlayers = new boolean [2];
		powerUps = new ArrayList<PowerUp>();
		players = new ArrayList<Player>();
		portals = new ArrayList<Portal>();
		balls = new ArrayList<Ball>();
		platforms = new ArrayList<Platform>();
		monsters = new ArrayList<Monster>();  //dont worry about monsters for now
		twoPlayer = false;
		
		this.setBackground(Color.BLACK);
		this.addKeyListener(this);

	    SoundEffect.init();
	    SoundEffect.volume = SoundEffect.Volume.MEDIUM;  
		
	}
	
	//paint component method
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		for (int i=0; i<monsters.size(); i++){
			monsters.get(i).draw(g);
		}
		for (int i=0; i<portals.size(); i++){
			portals.get(i).draw(g);
		}
		for (int i=0; i<platforms.size(); i++){
			platforms.get(i).draw(g);
		}
		
		//the following handles drawing for all of the games text and images
		g.drawImage(GAME_UI.getImage(), 0, 0, null); 
		g.setColor(Color.WHITE);
		g.fillRect(26+(int)((100-percentTime)*GAME_TIMEBAR_LENGTH/200), 555, (int)(GAME_TIMEBAR_LENGTH*percentTime/100), 22);

		Panel.font = Panel.font.deriveFont(30.0f);
		g.setFont(Panel.font);
		fontMetrics = g.getFontMetrics(Panel.font);
		for (int i=0; i<players.size(); i++){
			for (int j=0; j<players.get(i).getBullets().size(); j++){
				players.get(i).getBullets().get(j).draw(g);
			}
			if (players.get(i).getLives() > 0)
				players.get(i).draw(g);
			for (int j=0; j<players.get(i).getLives(); j++){
				if (i == 0)
					g.drawImage(GAME_LIFEIMG1.getImage(), 24 + 30*j, 515, GAME_LIFEIMG1.getIconWidth(), GAME_LIFEIMG1.getIconHeight(), null);
				else
					g.drawImage(GAME_LIFEIMG2.getImage(), 748 - 30*j, 515, GAME_LIFEIMG2.getIconWidth(), GAME_LIFEIMG2.getIconHeight(), null);
			}
			g.setColor(Color.white);
			if (i == 0){
				if (players.get(i).getLives() == 0 && twoPlayer){
					g.setFont(Panel.font.deriveFont(20.0f));
					g.drawString("Press 'F' to Play", 25, 528); 
				}
				g.setFont(Panel.font.deriveFont(30.0f));
				g.drawString(Integer.toString(players.get(i).getScore()), 25, 507); 
			}else{
				if (players.get(i).getLives() == 0 && twoPlayer){
					g.setFont(Panel.font.deriveFont(20.0f));
					g.drawString("Press 'Space' to Play", 650, 528); 
				}
				g.setFont(Panel.font.deriveFont(30.0f));
				g.drawString(Integer.toString(players.get(i).getScore()), 772 - fontMetrics.stringWidth(Integer.toString(players.get(i).getScore())), 507); 
			}
		}
		
		g.setFont(Panel.font.deriveFont(35.0f));
		g.drawString(Integer.toString(level+1), 430, 482);

		for (int i=0; i<powerUps.size(); i++){
			powerUps.get(i).draw(g);
		}
		if (showBalls){
			for (int i=0; i<balls.size(); i++){
				balls.get(i).draw(g);
			}
		}
		
		if (!gameStart){
			g.setFont(Panel.font.deriveFont(120.0f));
			g.drawString("GET READY!", 205, 270);
		}
		if (levelComplete){
			g.setFont(Panel.font.deriveFont(100.0f));
			g.drawString("LEVEL COMPLETE!", 165, 260);
		}
		if (noTime){
			g.setFont(Panel.font.deriveFont(100.0f));
			g.drawString("LEVEL FAILED!", 208, 265);
		}
	}
	
	//actionlistener method
	public void actionPerformed(ActionEvent e){
		
		//freezeTimer is used to flash the the powerup, player, or balls to show they are about to either run out, lose invincibility, or resume movement 
		//freezeTimer is also used to handle level complete, man down, and time ran out animation sequences
		if (e.getSource() == freezeTimer){
			if (!manDown && !levelComplete){ //flashes balls if freezeTime powerup is about to run out
				if (showBalls)
					showBalls = false;
				else
					showBalls = true;
			}
			freezeLeft+= 100;
			if (freezeLeft > 2000 && !levelComplete && !manDown){ 
				frozen = false;
				showBalls = true;
				freezeLeft = 0;
				freezeTimer.stop();
			}
			if (freezeLeft > 10000 && !levelComplete && manDown){ //time limit for manDown animation
				frozen = false;
				showBalls = true;
				freezeLeft = 0;
				freezeTimer.stop();
			}
			if (levelComplete){ //rapidly adds up remaining time score animation
				if (percentTime > 0){
					percentTime -= 1.5;
					for (int i = 0; i< players.size(); i++){
						players.get(i).addScore(50);
					}
				} else{
					freezeLeft += 10;
					if (freezeLeft > 10000){
						if (level == 9){
							victory = true;
							endGame(true); //endGame sequence if on last level
						}else
							nextLevel();
					} 
				}
			}
			if (manDown){ //death sequence animation for when players are either hit or if time runs out
				for (int i = 0; i < 2; i++){
					if (deadPlayers[i]){
						players.get(i).travel();
						players.get(i).move();
						System.out.println(players.get(i).getY());
						if (players.get(i).getY()>2000){
							if (deadPlayers[0]){
								players.get(0).setLives(players.get(0).getLives()-1);
								if (players.get(0).getLives() < 0)
									players.get(0).setLives(0);
							}if (deadPlayers[1]){
								players.get(1).setLives(players.get(1).getLives()-1);
								if (players.get(1).getLives() < 0)
									players.get(1).setLives(0);
							}if (gameOver())
								endGame(false);
							else
								retryLevel();
						}
					}
				}

			}

		}
		//regular gameplay timer
		if (e.getSource() == clock){
			gameStart = true;
			percentTime -= 0.1; //determines how fast time goes down in the level
			checkWin();
			//for every tick of the clock, perform movement methods for the players, balls, bullets, powerups, and mosnters, and check collision for bullets, players, and balls
			for (int i=0; i<players.size(); i++){
				players.get(i).travel();
				players.get(i).move();
				for (int j=0; j<players.get(i).getBullets().size(); j++){
					players.get(i).getBullets().get(j).travel();
					players.get(i).getBullets().get(j).move();
				}
			}
			for (int i=0; i<balls.size(); i++){
				if (!frozen){
					balls.get(i).travel();
					balls.get(i).move();
				}
			}
			for (int i=0; i<powerUps.size(); i++){
				powerUps.get(i).travel();
				powerUps.get(i).move();
				if (powerUps.get(i).isExpired())
					powerUps.remove(i);
			}
			for (int i=0; i<monsters.size(); i++){
				monsters.get(i).move();
			}
			checkBulletCollision();
			checkBallCollision();
			checkPowerUpCollision();
			checkPlayerCollision();
			monsterAI();
			
			//check if all of the balls have been destroyed - level has been won
			checkWin();
		}
		repaint();
	}
	
	private boolean gameOver(){ //checks if the total number of lives is 0, if so, gameover
		int livesLeft = 0;
		for (int i = 0; i<players.size(); i++){
			livesLeft += players.get(i).getLives();
		}
		if (livesLeft == 0)
			return true;
		return false;
	}
	
	public void keyPressed(KeyEvent e){
		//if player 1 is still alive, his controls will work and he can move left/right and shoot
		if (gameStart && !levelComplete && !manDown){
			if (players.get(0).getLives() > 0){
				if (e.getKeyCode() == KeyEvent.VK_A)
					players.get(0).setDirection(-1);
				else if (e.getKeyCode() == KeyEvent.VK_D)
					players.get(0).setDirection(1);
				else if (e.getKeyCode() == KeyEvent.VK_W){ //teleports the player to a portal's destination portal if they are within the boundaries of the portal
					if (players.get(0).inPortal(portals) != -1){
						System.out.println(players.get(0).inPortal(portals));
						players.get(0).teleport(portals.get(players.get(0).inPortal(portals)).x, portals.get(players.get(0).inPortal(portals)).y);
					}
				}else if (e.getKeyCode() == KeyEvent.VK_F)
					players.get(0).shoot();
			} else if (players.get(0).getLives() < 1){
				if (e.getKeyCode() == KeyEvent.VK_F && twoPlayer){
					players.get(0).setLives(3);
					players.get(0).setScore(0);
					players.get(0).spawn();
					players.get(0).giveInvincible();
				}	
			}
			//if a second player exists
			if (twoPlayer){
				//and the second player is alive, his controls will work (similar to first player)
				if (players.get(1).getLives() > 0){
					if (e.getKeyCode() == KeyEvent.VK_LEFT)
						players.get(1).setDirection(-1);
					else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
						players.get(1).setDirection(1);
					else if (e.getKeyCode() == KeyEvent.VK_UP){
						if (players.get(1).inPortal(portals) != -1){
							System.out.println(players.get(1).inPortal(portals));
							players.get(1).teleport(portals.get(players.get(1).inPortal(portals)).x, portals.get(players.get(1).inPortal(portals)).y);
						}
					}
					else if (e.getKeyCode() == KeyEvent.VK_SPACE)
						players.get(1).shoot();
				} else if (players.get(1).getLives() < 1){
					//if the second player is dead, he can press his shoot button to revive
					if (e.getKeyCode() == KeyEvent.VK_SPACE){
						players.get(1).setLives(3);
						players.get(1).setScore(0);
						players.get(1).spawn();
						players.get(1).giveInvincible();
					}	
				}
			}
		}
		repaint();
	}
	
	public void keyReleased(KeyEvent e){
		//if either player is alive and its detected that they have released their right/left button, they stop moving right/left
		if (players.get(0).getLives() > 0){
			if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D)
				players.get(0).setDirection(0);
			if (e.getKeyCode() == KeyEvent.VK_F)
				players.get(0).setShooting(false);
		}
		if (twoPlayer){
			if (players.get(1).getLives() > 0){
				if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)
					players.get(1).setDirection(0);
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
					players.get(1).setShooting(false);
			}
		}
		repaint();
	}
	
	public void keyTyped(KeyEvent e){
		
	}

	//method resets variables and objects whenever a new game/level is started
	public void setGame(int numPlayers, int level){
		noTime = false;
		deadPlayers[0] = false;
		deadPlayers[1] = false;
		if (numPlayers == 2)
			twoPlayer = true;
		levelComplete = false;
		this.level = level;
		loadLevel(level);
		percentTime = 100.0;
		for (int i=0; i<numPlatforms; i++)
			platforms.add(new Platform(platformBreakable[i], platformXspawn[i], platformYspawn[i], platformWidth[i], platformHeight[i]));
		for (int i=0; i<numPlayers; i++)
			players.add(new Player(i, playerXspawn[i], playerYspawn[i]));
		for (int i=0; i<numBalls; i++)
			balls.add(new Ball(ballSize[i], BALL_INITVELY[ballSize[i]], ballXspawn[i], ballYspawn[i], ballDirection[i]));
		for (int i=0; i<numPortals; i++)
			portals.add(new Portal(portalXspawn[i], portalYspawn[i], portalDestinations[i]));
		for (int i=0; i<numMonsters; i++)
			monsters.add(new Monster(monsterXspawn[i], monsterYspawn[i]));

		split = false;
		frozen = false;
		freezeLeft = 0;
		freezeTimer.stop();
		freezeTimer.setDelay(100);
		freezeTimer.setInitialDelay(5000);
		showBalls = true;
		gameStart = false;
		manDown = false;
		victory = false;
		clock = new Timer(20, this);
		clock.setInitialDelay(2000);
		clock.start();
		SoundEffect.READY.play();
	}

	public void removeAllObj(){ //removes all objects from the gamePanel, usually used for resetting the level
		platforms.removeAll(platforms);
		players.removeAll(players);
		balls.removeAll(balls);
		powerUps.removeAll(powerUps);
		portals.removeAll(portals);
		monsters.removeAll(monsters);
	}
	
	//method loads level info from a file
	private void loadLevel(int level){
		//initialize temp arrays
		String [] array1;
		String [] array2;
		String [] array3;
		String [] array4;
		String [] array5;

		loadFile = new File("level" + (level+1) + ".txt");
		try{
			//load the file and read strings split into arrays by spaces, and loop through arrays to assign values taken from the textfile
			//format of the textfiles can be found in levelformat.txt
			br = new BufferedReader(new FileReader(loadFile.getAbsolutePath()));
			array1 = br.readLine().split(" ");
			array2 = br.readLine().split(" ");

			playerXspawn = new int [array1.length];
			playerYspawn = new int [array2.length];
			for (int i = 0; i<array1.length; i++){
				playerXspawn[i] = Integer.parseInt(array1[i]);
				playerYspawn[i] = Integer.parseInt(array2[i]);
			}
			
			array1 = br.readLine().split(" ");
			array2 = br.readLine().split(" ");
			array3 = br.readLine().split(" ");
			array4 = br.readLine().split(" ");
			ballXspawn = new int [array1.length];
			ballYspawn = new int [array2.length];
			ballSize = new int [array3.length];
			ballDirection = new int [array4.length];
			for (int i = 0; i<array1.length; i++){
				ballXspawn[i] = Integer.parseInt(array1[i]);
				ballYspawn[i] = Integer.parseInt(array2[i]);
				ballSize[i] = Integer.parseInt(array3[i]);
				ballDirection[i] = Integer.parseInt(array4[i]);
			}
			numBalls = array1.length;

			array1 = br.readLine().split(" ");
			array2 = br.readLine().split(" ");
			array3 = br.readLine().split(" ");
			array4 = br.readLine().split(" ");
			array5 = br.readLine().split(" ");
			platformXspawn = new int [array1.length];
			platformYspawn = new int [array2.length];
			platformWidth = new int [array3.length];
			platformHeight = new int [array4.length];
			platformBreakable = new boolean [array5.length];
			for (int i = 0; i<array1.length; i++){
				platformXspawn[i] = Integer.parseInt(array1[i]);
				platformYspawn[i] = Integer.parseInt(array2[i]);
				platformWidth[i] = Integer.parseInt(array3[i]);
				platformHeight[i] = Integer.parseInt(array4[i]);
				platformBreakable[i] = Boolean.parseBoolean(array5[i]);
			}
			numPlatforms = array1.length;
			
			array1 = br.readLine().split(" ");
			array2 = br.readLine().split(" ");
			array3 = br.readLine().split(" ");
			portalXspawn = new int [array1.length];
			portalYspawn = new int [array2.length];
			portalDestinations = new int [array3.length];
			for (int i = 0; i<array1.length; i++){
				portalXspawn[i] = Integer.parseInt(array1[i]);
				portalYspawn[i] = Integer.parseInt(array2[i]);
				portalDestinations[i] = Integer.parseInt(array3[i]);
			}
			numPortals = array1.length;
			
			array1 = br.readLine().split(" ");
			array2 = br.readLine().split(" ");
			monsterXspawn = new int [array1.length];
			monsterYspawn = new int [array2.length];
			for (int i=0; i<array1.length; i++){
				monsterXspawn[i] = Integer.parseInt(array1[i]);
				monsterYspawn[i] = Integer.parseInt(array2[i]);
			}
			numMonsters = array1.length;
	
			br.close();
		} catch(Exception e){
			System.out.println(e);
		};
	}
	
	private void addPowerUp(int x, int y, int ballSize){ 
		double prob = ballSize/1.0;  //ballSize is used to determine likelihood of dropping powerup. The bigger the ball, the more likely (ball sizes from smallest to biggest are 0, 1, 2, 3), divide by 6.0 is Default
		if (Math.random() < prob){
			int randType = (int)(Math.random()*6); //generates random number from 0 to 5, used to randomize powerUp type
			powerUps.add(new PowerUp(randType, x, y));           
		}
	}
	
	//method checks for bullet collision with the ceiling, platforms, and balls
	private void checkBulletCollision(){
	
		//set objects and variables
		Rectangle bullet;
		Rectangle object;
		
		for (int i=0; i<players.size(); i++){
			for (int j=0; j<players.get(i).getBullets().size(); j++){

				//initialize bullet rectangle
				bullet = new Rectangle((int)players.get(i).getBullets().get(j).x, (int)players.get(i).getBullets().get(j).y, players.get(i).getBullets().get(j).width, players.get(i).getBullets().get(j).getCropHeight()*2);
				if (players.get(i).getBullets().get(j).y < 7){
					//if the bullet has reached the ceiling, remove the bullet
					players.get(i).removeBullet(j);
				}else{
					for (int k=0; k<balls.size(); k++){
						//initialize ball rectangle
						object = new Rectangle((int)balls.get(k).getX(), (int)balls.get(k).getY(), balls.get(k).getRadius()*2, balls.get(k).getRadius()*2);
						//check if the ball rectangle intersects the bullet, if it does remove the bullet and damage the ball
						if (bullet.intersects(object)){
							SoundEffect.POP1.play();
							players.get(i).removeBullet(j);
							if (balls.get(k).getSize() != 0){
								balls.add(new Ball(balls.get(k).getSize()-1, BALL_INITVELY[balls.get(k).getSize()],(int)balls.get(k).getX(), (int)(balls.get(k).getY()+balls.get(k).getRadius()/2.0), -1));
								balls.add(new Ball(balls.get(k).getSize()-1, BALL_INITVELY[balls.get(k).getSize()],(int)balls.get(k).getX()+balls.get(k).getRadius(), (int)(balls.get(k).getY()+balls.get(k).getRadius()/2.0), 1));
							}
							players.get(i).addScore(BALL_POINTS[balls.get(k).getSize()]);
							addPowerUp((int)balls.get(k).getX()+balls.get(k).getRadius(), (int)balls.get(k).getY()+balls.get(k).getRadius(), balls.get(k).getSize());
							balls.remove(k);
							return;
						}
					}

					for (int k=0; k<platforms.size(); k++){
						//check if the platform rectangle intersects the bullet, if it does then remove the bullet
						if (bullet.intersects(platforms.get(k))){
							if (platforms.get(k).isBreakable()){ //if the platform collided is breakable, remove it & add score
								players.get(i).addScore(500);
								platforms.remove(k);
								SoundEffect.SHATTER.play();
							}
							players.get(i).removeBullet(j);
							return;
						}
					}
					
					for (int k=0; k<monsters.size(); k++){
						object = new Rectangle(monsters.get(k).getX(), monsters.get(k).getY(), monsters.get(k).getWidth(), monsters.get(k).getHeight());
						
						if (bullet.intersects(object)){
							players.get(i).removeBullet(j);
							players.get(i).addScore(500);
							monsters.remove(k);
							return;
						}
					}
				}
			}
		}

	}
	
	private void checkBallCollision(){
		
		Rectangle ball;
		Rectangle player;
		int ballx, bally, balldia;
		int platx, platy, platwidth, platheight;
		
		//checks ball collision between players and platforms
		for (int i=0; i<balls.size(); i++){ 
			ballx = (int)balls.get(i).getX();
			bally = (int)balls.get(i).getY();
			balldia = balls.get(i).getRadius()*2;
			ball = new Rectangle(ballx, bally, balldia, balldia);
			
			for (int j=0; j<players.size(); j++){
				player = new Rectangle((int)players.get(j).getX()+4, (int)players.get(j).getY(), PLAYER_WIDTH-8, PLAYER_HEIGHT-5);
				
				if (!players.get(j).isInvincible()){ //only checks if player is not invulnerable
					if (ball.intersects(player) && players.get(j).getLives() != 0){
						if (players.get(j).hasShield()){ //applies different functions if player has a shield
							if (balls.get(i).getSize() > 0){
								balls.add(new Ball(balls.get(i).getSize()-1, BALL_INITVELY[balls.get(i).getSize()],(int)balls.get(i).getX(), (int)(balls.get(i).getY()+balls.get(i).getRadius()/2.0), -1));
								balls.add(new Ball(balls.get(i).getSize()-1, BALL_INITVELY[balls.get(i).getSize()],(int)balls.get(i).getX()+balls.get(i).getRadius(), (int)(balls.get(i).getY()+balls.get(i).getRadius()/2.0), 1));
							}
							balls.remove(i);
							SoundEffect.POP1.play();
							players.get(j).setShield(false);
							players.get(j).giveInvincible();
						}else{ //player dies, initiates death animation sequence by setting the following:
							deadPlayers[j] = true;
							players.get(j).setDead(true);
							SoundEffect.SCREAM2.play();
							manDown = true;
							players.get(j).setVelX(balls.get(i).getVelX()*2);
							players.get(j).setVelY(-5);
							clock.stop();
							freezeTimer.setInitialDelay(0);
							freezeTimer.stop();
							showBalls = true;
							freezeTimer.setDelay(20);
							freezeTimer.start();
							freezeLeft = 0;
						}
						return;
					}
				}
			}
			
			//checks ball collision between platforms
			for (int j=0; j<platforms.size(); j++){
				platx = (int)platforms.get(j).getX();
				platy = (int)platforms.get(j).getY();
				platwidth = (int)platforms.get(j).getWidth();
				platheight = (int)platforms.get(j).getHeight();
				
				if (balls.get(i).getVelY() >= 0 && ballx+balldia > platx && ballx < platx+platwidth && bally+balldia > platy && bally+balldia <= platy+balls.get(i).getVelY())
					balls.get(i).setVelY(-Math.abs(balls.get(i).getVelY())-balls.get(i).getAccY());
				else if (balls.get(i).getVelX() > 0 && ballx+balldia > platx && ballx+balldia < platx+platwidth/2.0 && bally+balldia > platy && bally+balldia < platy+platheight/2.0)
					balls.get(i).setVelX(-Math.abs(balls.get(i).getVelX()));
				else if (balls.get(i).getVelX() < 0 && ballx > platx+platwidth/2.0 && ballx < platx+platwidth && bally+balldia > platy && bally+balldia < platy+platheight/2.0)
					balls.get(i).setVelX(Math.abs(balls.get(i).getVelX()));
				else if (balls.get(i).getVelY() < 0 && ballx+balldia > platx && ballx < platx+platwidth && bally > platy+platheight+balls.get(i).getVelY() && bally < platy+platheight)
					balls.get(i).setVelY(Math.abs(balls.get(i).getVelY())-balls.get(i).getAccY());
			}
		}
	}
	
	//checks if the player walks into a certain powerup
	private void checkPowerUpCollision(){ 
		Rectangle powerUp;
		Rectangle player;
		for (int i=0; i<powerUps.size(); i++){
			
			powerUp = new Rectangle((int)powerUps.get(i).getX(), (int)powerUps.get(i).getY(), powerUps.get(i).getWidth(), powerUps.get(i).getHeight());
			
			for (int j=0; j<players.size(); j++){
				player = new Rectangle((int)players.get(j).getX(), (int)players.get(j).getY(), PLAYER_WIDTH, PLAYER_HEIGHT);
				if (powerUp.intersects(player)){
					if (powerUps.get(i).getType() == 0){            //laser power up
						players.get(j).setWeaponType(2);
					} else if (powerUps.get(i).getType() == 1){     //life up power up
						players.get(j).setLives(players.get(j).getLives()+1);
						SoundEffect.LIFE.play();
					} else if (powerUps.get(i).getType() == 2){     //shield power up
						players.get(j).setShield(true);
						SoundEffect.SHIELD.play();
					} else if (powerUps.get(i).getType() == 3){     //double shot power up
						players.get(j).setWeaponType(1);
					} else if (powerUps.get(i).getType() == 4){     //dynamite power up
						if (!split)
							SoundEffect.DYNAMITE.play();
						split = true;
						ArrayList<Ball> newArray = new ArrayList<Ball>();
						for (int k=0; k<balls.size(); k++){ //applies recursive method that splits every single ball to smallest
							if (balls.get(k).getSize() == 0)
								newArray.add(new Ball(0, balls.get(k).getVelY(), (int)balls.get(k).getX(), (int)balls.get(k).getY(), balls.get(k).getDirection()));
							else
								splitAll(newArray, (int)balls.get(k).getX(), (int)balls.get(k).getY()+balls.get(k).getRadius()*2, balls.get(k).getSize());
						}
						balls.clear();
						for (int k=0; k<newArray.size(); k++){
							balls.add(new Ball(newArray.get(k).getSize(), newArray.get(k).getVelY(), (int)newArray.get(k).getX(), (int)newArray.get(k).getY(), newArray.get(k).getDirection()));
						}
					} else{
						SoundEffect.FREEZETIME.play();
						freeze(); //freeze time method
					}
					powerUps.remove(i);
					return;
				}
			}
				
		}
	}

	//checks if player collides with platforms and sets proper x and y velocities
	private void checkPlayerCollision(){
		
		Rectangle player;
		boolean onPlatform;
		
		for (int i=0; i<players.size(); i++){
			player = new Rectangle((int)players.get(i).getX(), (int)players.get(i).getY(), PLAYER_WIDTH, PLAYER_HEIGHT);
			
			for (int j=0; j<platforms.size(); j++){
				if (players.get(i).getX()+PLAYER_WIDTH > platforms.get(j).getX() && players.get(i).getX() < platforms.get(j).getX()+platforms.get(j).getWidth() && players.get(i).getY()+PLAYER_HEIGHT > platforms.get(j).getY() && players.get(i).getY()+PLAYER_HEIGHT <= platforms.get(j).getY()+players.get(i).getVelY())
					onPlatform = true;
				else
					onPlatform = false;
				
				if (player.intersects(platforms.get(j)) && !manDown){
					if (onPlatform && players.get(i).getVelY() > 0){
						players.get(i).setVelY(0);
						players.get(i).setY(platforms.get(j).getY()-PLAYER_HEIGHT);
					}
					if (!onPlatform && players.get(i).getVelX() > 0){
						players.get(i).setVelX(0);
						players.get(i).setX(platforms.get(j).getX()-PLAYER_WIDTH);
					} else if (!onPlatform && players.get(i).getVelX() < 0){
						players.get(i).setVelX(0);
						players.get(i).setX(platforms.get(j).getX()+platforms.get(j).getWidth());
					}
				}
			}	
		}
	}
	
	//recursive method splits ball until smallest
	private void splitAll(ArrayList<Ball> newBalls, int x, int y, int size){
		if (size==1){ //base case
			newBalls.add(new Ball(size-1, BALL_INITVELY[size-1], x, y, -1));
			newBalls.add(new Ball(size-1, BALL_INITVELY[size-1], x+BALL_RADII[size], y, 1));
		} else if (size>1){
			splitAll(newBalls, x, y, size-1);
			splitAll(newBalls, x+BALL_RADII[size], y, size-1);
		}
	}
	
	//saves score and lives left information for players before removing all objects off panel (otherwise information would be lost)
	//sets score and lives from previous round
	private void retryLevel(){
		int numPlayers = players.size();
		int [] livesLeft = new int [2];
		int [] scores = new int [2];
		for (int i = 0; i<numPlayers; i++){
			livesLeft[i] = players.get(i).getLives();
			scores[i] = players.get(i).getScore();
		}
		removeAllObj();
		setGame(numPlayers, level);
		for (int j=0; j<numPlayers; j++){
			players.get(j).setLives(livesLeft[j]);
			players.get(j).setScore(scores[j]);
		}
	}
	
	//applies variable changes needed to initiate freeze sequence (stops motion of balls)
	private void freeze(){
		frozen = true;
		showBalls = true;
		freezeTimer.stop();
		freezeTimer.start();
		freezeLeft = 0;
	}
	
	//applies monster AI move function depending on where the player shoots his bullets
	private void monsterAI(){
		boolean[] bulletY = new boolean[800];
		for (int i=0; i<800; i++){
			bulletY[i] = true;
		}
		
		for (int i=0; i<players.size(); i++){
			for (int j=0; j<players.get(i).getBullets().size(); j++){
				for (int k=0; k<players.get(i).getBullets().get(j).width; k++){
					bulletY[(int)players.get(i).getBullets().get(j).x+k] = false;
				}
			}
		}
		
		for (int i=0; i<monsters.size(); i++){
			if (checkSafe(bulletY, monsters.get(i).getX(), monsters.get(i).getWidth(), 1) == 0)
				monsters.get(i).setDirection(0);
			else if (checkSafe(bulletY, monsters.get(i).getX(), monsters.get(i).getWidth(), 1) > checkSafe(bulletY, monsters.get(i).getX(), monsters.get(i).getWidth(), -1))
				monsters.get(i).setDirection(-1);
			else
				monsters.get(i).setDirection(1);
		}
	}

	//used to help the monster check if he is in a safe position away from any bullets
	private int checkSafe(boolean[] notSafe, int x, int width, int increment){
		boolean safe = true;
	
		if (x<0 || x+width>799)
			return 800;
		for (int i=0; i<width; i++){
			if (!notSafe[x+i])
				safe = false;
		}
		
		if (safe)
			return 0;
		else{
			return 1 + checkSafe(notSafe, x+increment, width, increment);
		}
	}
	
	//saves score and lives remaining before removing all objects for next level
	private void nextLevel(){
		level++;
		int numPlayers = players.size();
		int [] livesLeft = new int [2];
		int [] scores = new int [2];
		for (int i = 0; i<numPlayers; i++){
			livesLeft[i] = players.get(i).getLives();
			scores[i] = players.get(i).getScore();
		}
		removeAllObj();
		setGame(numPlayers, level);
		for (int j=0; j<numPlayers; j++){
			players.get(j).setLives(livesLeft[j]);
			players.get(j).setScore(scores[j]);
		}
		
	}
	
	//checks to see if there are no balls left or there is no more time left, and applies proper variable settings for the corresponding sequences
	private void checkWin(){ 
		if (balls.size() == 0){
			levelComplete = true;
			clock.stop();
			freezeTimer.setInitialDelay(0);
			freezeTimer.setDelay(20);
			freezeTimer.start();
			freezeLeft = 0;
			SoundEffect.LEVELCOMPLETE.play();
		}
		if (percentTime <= 0){
			noTime = true;
			SoundEffect.LEVELFAIL.play();
			for (int i = 0; i < players.size(); i++){
				deadPlayers[i] = true;
				players.get(i).setDead(true);
				players.get(i).setVelX(3);
				players.get(i).setVelY(-5);
			}
			SoundEffect.SCREAM2.play();
			manDown = true;
			clock.stop();
			freezeTimer.setInitialDelay(0);
			freezeTimer.stop();
			showBalls = true;
			freezeTimer.setDelay(20);
			freezeTimer.start();
			freezeLeft = 0;
		}
	}
	
	//applies endGame sequence if the game is over, be it through beating the game or running out of lives
	private void endGame(boolean win){
		if (win)
			SoundEffect.VICTORY.play();
		else
			SoundEffect.GAMEOVER.play();
		levelComplete = false;
		manDown = false;
		clock.stop();
		Panel.changeCard(5);
	}
	
	public boolean checkVictory(){
		return victory;
	}
	
	public boolean getNumPlayers(){
		return twoPlayer;
	}
	
	public int[] getScores(){
		int [] scores = new int [players.size()];
		for (int i = 0; i<players.size(); i++){
			scores[i] = players.get(i).getScore();
		}
		return scores;
	}
}
