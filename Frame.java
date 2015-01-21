import java.awt.*;
import javax.swing.*;

public class Frame extends JFrame{
	//create container and main panel
	private Container container;
	private Panel mainPanel;
	
	//frame constructor
	public Frame(){
		//set title, remove border
		setTitle("Pang!");
		setUndecorated(true);
		
		container = getContentPane();
		
		//initialize the panel, add the panel to the container
		mainPanel = new Panel();
		container.add(mainPanel);
	}
	
	//main method
	public static void main(String[]args){
		Frame frame = new Frame();
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}