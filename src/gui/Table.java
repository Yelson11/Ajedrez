package gui;

import com.google.common.collect.Lists;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import juego.jugador.TransitionMove;
import juego.pieza.Piece;
import juego.tablero.Tile;
import juego.tablero.Move;

import juego.tablero.Board;
import juego.tablero.BoardUtils;

public class Table {

    private final JFrame gameFrame;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final BoardPanel boardPanel;
    private Board board;
    
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    
    private final Color lightTileColor = Color.decode("#e4e7ec");
    private final Color darkTileColor  = Color.decode("#7d8697");

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private static final String defaultPieceImagesPath = "src/art/pieces/";
    private static final String defaultArtImagePath = "src/art/";
    
    private BoardDirection boardDirection;    
    
    public Table() {
        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.board = Board.createStandardBoard();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.boardPanel = new BoardPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("open up that pgn file");
            }

        });
        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){

                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }
    
    private JMenu createPreferencesMenu(){
        
        final JMenu preferecesMenu = new JMenu("Preferencias");
        final JMenuItem flipboardMenuItem = new JMenuItem("Invertir tablero");
        flipboardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(board);
            }
        });
        preferecesMenu.add(flipboardMenuItem);
        return preferecesMenu;
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.TILE_NUMBER; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);

            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();

        }

        
        //Repaint
        private void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }
    
    public static class MoveLog{
        private final List<Move> moves;
        
        MoveLog(){
            this.moves = new ArrayList<>();
        }
        
        public List<Move> getMoves(){
            return this.moves;
        }
        
        public void addMove(final Move move){
            this.moves.add(move);
        }
        
        public int size(){
            return this.moves.size();
        }
        
        public void clear(){
            this.moves.clear();
        }
        
        public Move removeMove(int index){
            return this.moves.remove(index);
        }
        
        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }
    }
    

    private class TilePanel extends JPanel {
        private final int tileId;

        TilePanel(final BoardPanel boardPanel, final int tileId) {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(board);
            
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {                   
                    if (SwingUtilities.isRightMouseButton(e)){
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }
                    else if (SwingUtilities.isLeftMouseButton(e)){
                        if (sourceTile == null ){
                             sourceTile = board.getTile(tileId);
                             humanMovedPiece = sourceTile.getPiece();
                             if (humanMovedPiece == null){
                                 sourceTile = null;
                             }
                        } else {
                            destinationTile = board.getTile(tileId);
                            final Move move = Move.FabricaMovimientos.crearMovimiento(board, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final TransitionMove transicion = board.currentPlayer().makeMove(move);
                            if (transicion.getMoveStatus() == Move.MoveStatus.DONE){
                                board = transicion.getTransitionBooard();
                                //TODO add the move
                            }
                            destinationTile = null;
                            sourceTile = null;
                            humanMovedPiece = null;
                       }
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                boardPanel.drawBoard(board);
                            }
                        });
                    }
                }
                 

                @Override
                public void mousePressed(MouseEvent e) {
                 
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                 
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                 
                }

                @Override
                public void mouseExited(MouseEvent e) {
                 
                }
            });
            
            validate();
        }
        
        public void drawTile (final Board tablero){
            assignTileColor();
            assignTilePieceIcon(tablero);
            highLightLegals(board);
            validate();
            repaint();
        }

        private void  assignTilePieceIcon (final Board tablero){
            this.removeAll();
            if (tablero.getTile(this.tileId).tileIsOccupied()){
                try {
                    final String imagePath = defaultPieceImagesPath+tablero.getTile(this.tileId).getPiece().getPieceColor().toString().substring(0, 1) + tablero.getTile(this.tileId).getPiece().toString()+".PNG";
                    final BufferedImage image = ImageIO.read(new File(imagePath));
                    add(new JLabel (new ImageIcon(image)));
                } catch (IOException ex) {
                    Logger.getLogger(Table.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        private void highLightLegals (final Board board){
            for (final Move move : pieceLegalMoves(board)){
                    if (move.getDestinationCoordinate() == this.tileId){
                        try {
                            add (new JLabel (new ImageIcon(ImageIO.read(new File(defaultArtImagePath+"other/legals.png")))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            }
        }
        
        private Collection<Move> pieceLegalMoves (final Board board){
            if (humanMovedPiece != null && humanMovedPiece.getPieceColor() == board.currentPlayer().getPiecesColor()){
                return humanMovedPiece.calculateLegalMovements(board);
            }
            return Collections.emptyList();
        }

        private void assignTileColor() {

            if (BoardUtils.EIGHT_RANK[this.tileId] || 
                BoardUtils.SIXTH_RANK[this.tileId] || 
                BoardUtils.FOURTH_RANK[this.tileId] || 
                BoardUtils.SECOND_RANK[this.tileId]) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if ( BoardUtils.SEVENTH_RANK[this.tileId] || 
                        BoardUtils.FIFTH_RANK[this.tileId] || 
                        BoardUtils.THIRD_RANK[this.tileId] || 
                        BoardUtils.FIRST_RANK[this.tileId]) {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }

        }
    }
    
    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles){
                return boardTiles;
            }
            @Override
            BoardDirection opposite(){
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles){
                return Lists.reverse(boardTiles);
            }
            @Override
            BoardDirection opposite(){
                return NORMAL;
            }
        };
        
        abstract List<TilePanel> traverse (final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }

}
