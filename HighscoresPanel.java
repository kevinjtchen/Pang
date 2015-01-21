import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class HighscoresPanel extends JPanel implements KeyListener, GameConstants{
	private File highscoreFile;
	private BufferedReader br;
	
	private String[] names;
	private int selection;
	private int[] scores;
	private FontMetrics fontMetrics;
	
	public HighscoresPanel(){
		highscoreFile = new File("highscores.txt");
		names = new String[10];
		scores = new int[10];
		selection = 0;
		loadFile();
		this.addKeyListener(this);
	}
	
	//draws the names and scores, using fontMetrics for right aligning
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		fontMetrics = g.getFontMetrics(Panel.font.deriveFont(32.0f));
		g.drawImage(HIGHSCORES_IMGS[selection].getImage(), 0, 0, null);
		g.setFont(Panel.font.deriveFont(32.0f));
		g.setColor(Color.WHITE);
		for (int i=0; i<names.length; i++){
			g.drawString((i+1)+".", 238 - fontMetrics.stringWidth((i+1)+"."), 175 + i*32);
			g.drawString(names[i], 248, 175 + i*32);
			
			g.drawString(""+scores[i], 558 - fontMetrics.stringWidth(""+scores[i]), 180 + i*32);

		}
	}
	
	//tracks selection for going back and play
	public void keyPressed(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT){
			selection++;
			if (selection > 1)
				selection = 0;
			SoundEffect.SELECT.play();
		}else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE){
			System.out.println(selection);
			SoundEffect.SELECT.play();
			Panel.changeCard(selection);
		}
	}
	
	public void keyReleased(KeyEvent e){
	}
	
	public void keyTyped(KeyEvent e){
		
	}
	
	//loads the names and scores into String array for drawing onto panel
	public void loadFile(){
		String[] tempArray = new String[2];
		
		try{
			br = new BufferedReader(new FileReader(highscoreFile.getAbsolutePath()));
			for (int i=0; i<10; i++){
				tempArray = br.readLine().split(" ");
				System.out.println(tempArray);
				names[i] = tempArray[0];
				scores[i] = Integer.parseInt(tempArray[1]);
			}
			br.close();
		} catch(Exception e){
			System.out.println("ERROR: Highscores file not loaded properly");
		};
	}
}
