import javax.swing.ImageIcon;

public interface GameConstants {	
	
	public static final int PLAYER_WIDTH = 50;
	public static final int PLAYER_HEIGHT = 70;
	public static final int PLAYER_LIVES = 1;   // still, 12345, shoot
	public static final ImageIcon [] PLAYER_IMGS = {new ImageIcon("playerStill.png"), new ImageIcon("playerMove1.png"), new ImageIcon("playerMove2.png"), new ImageIcon("playerMove3.png"), new ImageIcon("playerMove4.png"), new ImageIcon("playerMove5.png"), new ImageIcon("playerShoot.png"), new ImageIcon("player2still.png"), new ImageIcon("player2move1.png"), new ImageIcon("player2move2.png"), new ImageIcon("player2move3.png"), new ImageIcon("player2move4.png"), new ImageIcon("player2move5.png"), new ImageIcon("player2Shoot.png")};
	public static final ImageIcon [] PLAYER_DEAD = {new ImageIcon("player1dead.png"), new ImageIcon("player2dead.png")};
	public static final ImageIcon PLAYER_SHIELD = new ImageIcon("actualShield.png");
	
	public static final ImageIcon PORTAL_IMG = new ImageIcon("portal.png"); 
	
	public static final ImageIcon BACKGROUND_IMG = new ImageIcon("bg.png");
	
	public static final ImageIcon PLATFORM_IMG1 = new ImageIcon("breakable.png");
	public static final ImageIcon PLATFORM_IMG1B = new ImageIcon("breakable2.png");
	public static final ImageIcon PLATFORM_IMG2 = new ImageIcon("unbreakable.png");
	public static final ImageIcon PLATFORM_IMG2B = new ImageIcon("unbreakable2.png");

	public static final ImageIcon HARPOON_IMG = new ImageIcon("harpoon.png");
	public static final ImageIcon LASER_IMG = new ImageIcon("laserShot.png");
	public static final double [] BULLET_SPEEDS = {-3.0, -8};
	
	public static final int [] BALL_POINTS = {200, 150, 100, 50}; 	
	public static final int [] BALL_RADII = {8,16,32,64};
	public static final double [] BALL_INITVELY = {-1.5,-1.8,-2.1,-2.4};
	public static final ImageIcon BALL_IMG = new ImageIcon("ball.png");

	public static final ImageIcon [] POWERUPS_IMGS = {new ImageIcon("laser.png"), new ImageIcon("life.png"), new ImageIcon("shield.png"), new ImageIcon("doubleshot.png"), new ImageIcon("dynamite.png"), new ImageIcon("clock.png")};
	
	public static final ImageIcon [] MONSTER_IMGS = {new ImageIcon("monster1.png"), new ImageIcon("monster2.png"), new ImageIcon("monster3.png"), new ImageIcon("monster4.png")};

	public static final double GAME_GRAVITY = 2;
	public static final ImageIcon GAME_UI = new ImageIcon("gameui.png");
	public static final ImageIcon GAME_LIFEIMG1 = new ImageIcon("life1.png");
	public static final ImageIcon GAME_LIFEIMG2 = new ImageIcon("life2.png");
	public static final int GAME_TIMEBAR_LENGTH = 750;
	public static final double GAME_CLOCKSPEED = 0.1;  
	
	public static final ImageIcon [] MENU_IMGS = {new ImageIcon("menu1.png"), new ImageIcon("menu2.png"), new ImageIcon("menu3.png")};
	public static final ImageIcon [] MENU_INSTRUCTIONS = {new ImageIcon("instructions1.png"),  new ImageIcon("instructions2.png")};
	 
	public static final ImageIcon [] LEVELSELECT_IMGS = {new ImageIcon("LevelSelect.png"), new ImageIcon("LevelSelect1.png"), new ImageIcon("LevelSelect2.png"), new ImageIcon("LevelSelect1PLAY.png"), new ImageIcon("LevelSelect2PLAY.png")};
	
	public static final ImageIcon [] HIGHSCORES_IMGS = {new ImageIcon("highscores1.png"), new ImageIcon("highscores2.png")};
	public static final ImageIcon [] WIN_IMGS = {new ImageIcon("win1.png"), new ImageIcon("win2.png")};
	public static final ImageIcon [] LOSE_IMGS = {new ImageIcon("gameover1.png"), new ImageIcon("gameover2.png")};
	
}
