import java.awt.*;
import javax.swing.*;
import java.util.*;

public class FieldApplet extends JApplet
{
    public void init()
    {    
    	FieldPanel b = new FieldPanel(800, 200, 100, 300, 75);
		setSize(1000, 450);
		setBackground(Color.white);
		setContentPane(b);   
    }
  
}