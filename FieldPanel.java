import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class FieldPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener
{

	private Rock r;
	private Target ta = new Target();
	private boolean clicked = false;
	private int xOff;
	private int yOff;
	public int pointX;
	public int pointY;
	public int dragDist;
	public Timer t;
	public int xCounter;
	ArrayList<Rock> rocks;
	double slope;
	private Image bomb;
	private Image target;
	private Image youLose;
	private boolean shooting = false;
	public int score = 0;
	public int lives = 3;
	public boolean lose = false;
	Font f = new Font("default", Font.BOLD, 32);
	public int winStreak = 0;
	ArrayList <Integer> highScore = new ArrayList <Integer>();

	public FieldPanel(int tx, int ty, int sX, int sY, int drag) 
	{
		ta.setX(tx);
		ta.setY(ty);
		pointX = sX;
		pointY = sY;
		dragDist = drag;
		rocks = new ArrayList<Rock>();
		ImageIcon img = new ImageIcon("Bomb.png");
		ImageIcon img2 = new ImageIcon("Target.png");
		ImageIcon img3 = new ImageIcon("youLose.png");
		bomb = img.getImage();
		target  = img2.getImage();
		youLose = img3.getImage();
		setSize(505, 505);
		setBackground(Color.white);
		addMouseListener(this);
		addMouseMotionListener(this);
		r = new Rock(pointX, pointY);
		highScore.add(0);
		highScore.add(0);
		highScore.add(0);
	}

	public void paintComponent(Graphics g) 
	{
		g.clearRect(0, 0, getWidth(), getHeight());
		if (shooting == false)
		{
			if (r.getX() > pointX)
			{
				g.setColor(Color.pink);
				g.fillArc(pointX - dragDist, pointY - dragDist, dragDist * 2, dragDist * 2, -90, 180);
			}
		}
		g.setColor(Color.black);
		g.setFont(f);
		g.drawString("Your score: "+score, 20, 30);
		g.drawString("Your lives: " + lives, 20, 60);
		g.drawString("Win streak: " + winStreak, 20, 90);
		g.drawString("(" + (5 - winStreak % 5) + " more for extra life)", 20, 120);
		g.drawOval(pointX - dragDist, pointY - dragDist, dragDist * 2, dragDist * 2);
		g.drawImage(target, ta.getX() - 16, ta.getY(), null);
	    g.drawImage(bomb, r.getX() - 25, r.getY() - 25, null);
	    if(lose == true)
	    {
	    	
	    	g.clearRect(0, 0, getWidth(), getHeight());
	    	g.drawImage(youLose, 350, 100, null);
	    	g.drawString("Your score: "+score, 20, 30);
			g.drawString("Your lives: " + lives, 20, 60);
			g.drawString("Click to reset", 20, 90);
	    	g.drawString("High Score 1: " + highScore.get(0), 400, 330);
	    	g.drawString("High Score 2: " + highScore.get(1), 400, 360);
	    	g.drawString("High Score 3: " + highScore.get(2), 400, 390);
	    }
	}

	public void mousePressed(MouseEvent m)
	{
		clicked = false;
		if(lose == false)
		{
		if (Math.pow(m.getX() - r.getX(), 2) + Math.pow(m.getY() - r.getY(), 2) <= 625) 
		{
			System.out.println("Ball Pressed!");
			xOff = m.getX() - r.getX();
			yOff = m.getY() - r.getY();
			clicked = true;
		}
		}
	}
	
	public void mouseReleased(MouseEvent m) 
	{
		if (clicked == true)
		{
			if (r.getX() < pointX)
			{
				launched();
				t.start();
			}
		}
		else if(lose == true)
		{
			score = 0;
			lives = 3;
			winStreak = 0;
			lose = false;
			repaint();
		}
	}
		
	public static void sleep (int wait)
		{
		    long timeToQuit = System.currentTimeMillis() + wait;
		    while (System.currentTimeMillis() < timeToQuit);
		}

	public void mouseDragged(MouseEvent m) 
	{
		if (clicked) 
		{
			int rx = m.getX() - xOff;
			int ry = m.getY() - yOff;
			if (Math.sqrt(Math.pow(m.getX() - xOff - pointX, 2) + Math.pow(m.getY() - yOff - pointY, 2)) <= dragDist) //is in drag circle
			{
				r.setX(rx);
				r.setY(ry);
				repaint();
			}
			else
			{
				double reduce = Math.pow( Math.sqrt(dragDist)/(Math.pow(rx - pointX,2) + Math.pow(ry - pointY,2)),-.5 )/dragDist * 3;
				r.setX((int)((rx - pointX) / reduce + pointX));
				r.setY((int)((ry - pointY) / reduce + pointY));
				repaint();
			}
		}
	}
	
	public void launched()
	{
			clicked = false;
			System.out.println("Ball Launched!");
			int startX = r.getX();
			int startY = r.getY();
			rocks.clear();
			xCounter = 0;
			t = new Timer((100/Math.abs(r.getX()-pointX)), this);
			findLine(startX - pointX, startY - pointY, 0, 0);
			for (int k = startX -pointX; k < 0; k++)
			{
				rocks.add(new Rock(k + pointX, (int)(slope*(k)) + pointY));
			}
			for(int i = 0; i <= getWidth() - pointX; i++)
			{
				rocks.add(new Rock( (i + pointX), (int)(Math.pow(i, 2)/Math.pow(0.6 * (startX-pointX), 2) + (startY - pointY) * i/(startX-pointX))+pointY));
			}
			shooting = true;
			repaint();
	}
	

	public void mouseMoved(MouseEvent m) 
	{
	}

	public void mouseClicked(MouseEvent m) 
	{

	}

	public void mouseEntered(MouseEvent m) 
	{

	}

	public void mouseExited(MouseEvent m) 
	{

	}

	public void actionPerformed(ActionEvent arg0)
	{
		r.setX(rocks.get(xCounter).getX());
		r.setY(rocks.get(xCounter).getY());
		repaint();
		xCounter++;
		if(r.getX() == ta.getX())
		{
			if(r.getY() > ta.getY() && r.getY() < ta.getY() + 100)
			{
				t.stop();
				sleep (300);
				pointX = (int)(Math.random() * 300 + 100);
				pointY = (int)(Math.random() * 250 + 100);
				ta.setX((int)(Math.random() * 300 + 600));
				ta.setY((int)(Math.random() * 250 + 100));
				dragDist = (int)(Math.sqrt(ta.getX() - pointX + (pointY - ta.getY())) + 40);
				score = score + 10;
				winStreak++;
				if(winStreak % 5 == 0)
				{
					lives++;
				}
				r.setX(pointX);
				r.setY(pointY);
				shooting = false;
				
			}
		}
		else if(xCounter >= rocks.size() || r.getY() >= getHeight())
		{
			t.stop();
			sleep (300);
			lives = lives - 1;
			winStreak = 0;
			if(lives == 0)
			{
				lose = true;
				if(score > highScore.get(0))
		    	{
		    		highScore.set(2, highScore.get(1));
		    		highScore.set(1,  highScore.get(0));
		    		highScore.set(0,score);
		    	}
		    	else if(score > highScore.get(1))
		    	{
		    		highScore.set(2, highScore.get(1));
		    		highScore.set(1, score);
		    	}
		    	else if (score > highScore.get(2))
		    		highScore.set(2, score);
			}
			r.setX(pointX);
			r.setY(pointY);
			shooting = false;
		}
	}
	
	public void findLine(double x1,double y1,double x2,double y2)
	{
		slope = (y2-y1)/(x2-x1);
	}
}
