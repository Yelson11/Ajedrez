package gui;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class Table {
    
    private final JFrame gameFrame;
    
    private static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    
    public Table(){
        this.gameFrame = new JFrame("Chess");
        final JMenuBar tableMenuBar = new JMenuBar();
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setVisible(true);
    }
    
    private void populateMenuBar(JMenuBar tableMenuBar){
        
    }
    
}
