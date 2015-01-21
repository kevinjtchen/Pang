import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

//contains all the panels and changes them correspondingly depending on what page you are supposed to be on in the game
public class Panel extends JPanel implements ActionListener{
	private static int card;
	private int oldCard;
	private CardLayout cl;
	private MenuPanel menu;
	private LevelSelectPanel levelSelect;
	private GamePanel game;
	private InstructionsPanel instructions;
	private HighscoresPanel highscores;
	private HighscoreInputPanel highscoresInput;
	public static Font font;
	public Timer clock;
	
	//initiatest all panels in this game
	public Panel(){
		loadFont();
		cl = new CardLayout();
		this.setLayout(cl);
		
		menu = new MenuPanel();
		levelSelect = new LevelSelectPanel();
		game = new GamePanel();
		instructions = new InstructionsPanel();
		highscores = new HighscoresPanel();
		highscoresInput = new HighscoreInputPanel();
		
		this.add(menu, "0");
		this.add(levelSelect, "1");
		this.add(instructions, "2");
		this.add(highscores, "3");
		this.add(game, "4");
		this.add(highscoresInput, "5");
		
		menu.setFocusable(true);
		levelSelect.setFocusable(true);
		game.setFocusable(true);
		instructions.setFocusable(true);
		highscores.setFocusable(true);
		highscoresInput.setFocusable(true);

		card = 0;
		clock = new Timer(20, this);
		clock.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == clock){   // have something constantly check the active panel if it wants to change into another panel
			if (card != oldCard){     
				changeCard();
				oldCard = card;
			}
		}
		
		repaint();
	}
	
	//changes the visibility and focus for different panels and applies functions when applicable
	private void changeCard(){
		visibleOff();
		if (card == 0){
			menu.setVisible(true);
			menu.requestFocus();
			menu.resetMenu();
		}else if (card == 1){
			levelSelect.setVisible(true);  					
			levelSelect.requestFocus();	
			levelSelect.resetLevelSelect();
		}else if (card == 2){
			instructions.setVisible(true);
			instructions.requestFocus();
		}else if (card == 3){
			highscores.setVisible(true);
			highscores.requestFocus();
			highscores.loadFile();
		}else if (card == 4){
			game.setVisible(true);					
			game.requestFocus();
			game.removeAllObj();
			game.setGame(levelSelect.getNumPlayers(), levelSelect.getSelectedLevel());
		}else{
			highscoresInput.setVisible(true);
			highscoresInput.requestFocus();
			highscoresInput.getScoreInfo(game.checkVictory(), game.getScores());
		}
	}
	public static void changeCard (int newCard){
		card = newCard;
	}
	
	//turns off all visibility
	private void visibleOff(){
		menu.setVisible(false);
		levelSelect.setVisible(false);
		instructions.setVisible(false);
		game.setVisible(false);
		highscores.setVisible(false);
		highscoresInput.setVisible(false);
	}

	//loads font for all panels to use
	public void loadFont(){
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("pang.ttf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(font);
		font = font.deriveFont(Font.PLAIN, 20);
	}    
	
}
