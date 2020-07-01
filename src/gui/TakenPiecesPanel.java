package gui;

import com.google.common.primitives.Ints;
import gui.Table.MoveLog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import juego.pieza.Piece;
import juego.tablero.Move;

/**
 *
 * @author Yelson
 */
public class TakenPiecesPanel extends JPanel{
    
    private final JPanel northPanel;
    private final JPanel southPanel;
    
    private static final Color PANEL_COLOR = Color.BLACK;
    private static final Dimension TAKEN_PIECES_DIM = new Dimension(40, 80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    
    public TakenPiecesPanel(){
        super(new BorderLayout());
        setBackground(PANEL_COLOR);
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8,2));
        this.southPanel = new JPanel(new GridLayout(8,2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        setPreferredSize(TAKEN_PIECES_DIM);
    }
    
    public void redo(final MoveLog moveLog){
        this.southPanel.removeAll();
        this.northPanel.removeAll();
        
        final List<Piece> whiteTakenPieces = new ArrayList();
        final List<Piece> blackTakenPieces = new ArrayList();
        
        for(final Move move : moveLog.getMoves()){
            if(move.isAtack()){
                final Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getPieceColor().isWhite()){
                    whiteTakenPieces.add(takenPiece);
                } else if(takenPiece.getPieceColor().isBlack()){
                    blackTakenPieces.add(takenPiece);
                } else{
                    throw new RuntimeException("No debería entrar aquí");
                }
            }
        }
        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });
        
        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });
        
        for(final Piece takenPiece : whiteTakenPieces){
            try{
                //final BufferedImage image = ImageIO.read(new File("src/art/pieces/plain"));
                final BufferedImage image = ImageIO.read(new File("src/art/pieces" + takenPiece.getPieceColor().toString().substring(0, 1) + "" + takenPiece.toString()));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.southPanel.add(imageLabel);
            }catch(IOException e){
            }
        }
        
        for(final Piece takenPiece : blackTakenPieces){
            try{
                //final BufferedImage image = ImageIO.read(new File("src/art/pieces/plain"));
                final BufferedImage image = ImageIO.read(new File("src/art/pieces" + takenPiece.getPieceColor().toString().substring(0, 1) + "" + takenPiece.toString()));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.southPanel.add(imageLabel);
            }catch(IOException e){
            }
        }
        
        validate();
    }
}

