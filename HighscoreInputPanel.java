import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class HighscoreInputPanel extends JPanel implements KeyListener, GameConstants{
	
	private BufferedReader br;
	private BufferedWriter bw;
	private File highscoreFile;
	private String[] names;
	private int[] scores;
	private int selection;
	private int yourScore;
	private FontMetrics fontMetrics;
	
	private String nameInput;
	private boolean victory;
	private boolean topScore;
	
	public HighscoreInputPanel(){
		nameInput = "";
		this.addKeyListener(this);
		yourScore = 0;
		selection = 0;
		highscoreFile = new File("highscores.txt");
		names = new String[10];
		scores = new int[10];		
		
	}
	
	//draws everything there is in the panel
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.white);
		if (victory){
			g.drawImage(WIN_IMGS[selection].getImage(), 0, 0, null);
		}else{
			g.drawImage(LOSE_IMGS[selection].getImage(), 0, 0, null);
		}
		g.setFont(Panel.font.deriveFont(85.0f));
		fontMetrics = g.getFontMetrics(Panel.font.deriveFont(85.0f));
		g.drawString(""+yourScore, 400 - (int)(fontMetrics.stringWidth(""+yourScore)/2.0), 283);
		
		g.setFont(Panel.font.deriveFont(60.0f));
		fontMetrics = g.getFontMetrics(Panel.font.deriveFont(62.0f));
		if (selection == 0)
			g.drawString(nameInput+"_", 400 - (int)(fontMetrics.stringWidth(nameInput+"_")/2.0), 426);
		else
			g.drawString(""+nameInput, 400 - (int)(fontMetrics.stringWidth(""+nameInput)/2.0), 426);
	}
	
	//applies key controls, takes in name input using keyCode and adding to input string
	public void keyPressed(KeyEvent e){
		if (e.getKeyCode() > 64 && e.getKeyCode() < 91){
			nameInput += e.getKeyChar();
			System.out.println(nameInput);
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER && nameInput.length() > 0){
			selection++;
			System.out.println(selection);
			if (selection == 2){
				addScores();
				resetHighScoreInput();
				Panel.changeCard(0);
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && nameInput.length() > 0){
			if (selection == 0)
				nameInput = nameInput.substring(0, nameInput.length()-1);
			else
				selection--;
		}
	}
	
	public void keyReleased(KeyEvent e){
	}
	
	public void keyTyped(KeyEvent e){
	}
	
	//gets score to use in inputting highscores
	public void getScoreInfo(boolean win, int [] scores){
		victory = win;
		for (int i = 0; i < scores.length; i++){
			if (scores[i] > yourScore)
				yourScore = scores[i];
		}
	}
	
	//adds highscore if it is in top 10
	private void addScores(){
		loadScores();
		topScore = true;
		if (yourScore > scores[9]){
			for (int i=8; i>-1; i--){
				if (yourScore <= scores[i]){
					insertScore(i, yourScore);
					topScore = false;
					break;
				}
			}
			
			if (topScore)
				insertScore(-1, yourScore);
		}
		
		try{
			bw = new BufferedWriter(new FileWriter(highscoreFile.getAbsolutePath()));
			for (int i=0; i<10; i++){
				bw.write(names[i] + " " + scores[i]);
				bw.newLine();
			}
			bw.close();
		} catch(Exception e){
			System.out.print(e);
			System.out.println("ERROR: Highscores file not saved properly");
		};
	}
	
	//loads the scores from file
	private void loadScores(){
		String[] tempArray;
		
		try{
			br = new BufferedReader(new FileReader(highscoreFile.getAbsolutePath()));
			
			for (int i=0; i<10; i++){
				tempArray = br.readLine().split(" ");
				
				names[i] = tempArray[0];
				scores[i] = Integer.parseInt(tempArray[1]);
			}
			
			br.close();
		} catch(Exception e){
			System.out.println("ERROR: Highscores file not loaded properly");
		};
	}
	
	//inserts the score in the proper top 10 position
	private void insertScore(int index, int score){
		int[] tempArray = new int[9-index];
		String[] tempArray2 = new String[9-index];
		for (int i=0; i<tempArray.length; i++){
			tempArray[i] = scores[index+1+i];
			tempArray2[i] = names[index+1+i];
		}
		
		scores[index+1] = score;
		names[index+1] = nameInput;
		
		for (int i=0; i<tempArray.length-1; i++){
			scores[index+2+i] = tempArray[i];
			names[index+2+i] = tempArray2[i];
		}
	}
	
	//resets variables for next use
	public void resetHighScoreInput(){
		selection = 0;
		yourScore = 0;
		nameInput = "";
	}
}
