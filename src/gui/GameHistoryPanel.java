package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import juego.tablero.Board;
import juego.tablero.Move;

/**
 *
 * @author Yelson
 */
public class GameHistoryPanel extends JPanel{
    
    private final DataModel model;
    private final JScrollPane scrollPane;
    private static final Dimension HIST_PANEL_DIM = new Dimension(100,400);

    public GameHistoryPanel() {
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(HIST_PANEL_DIM);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }
    
    void redo(final Board board, final Table.MoveLog moveHistory){
        int currentRow = 0;
        this.model.clear();
        for(final Move move : moveHistory.getMoves()){
            final String moveText = move.toString();
            if(move.getMovedPiece().getPieceColor().isWhite()){
                this.model.setValueAt(moveText, currentRow, 0);
            } else if(move.getMovedPiece().getPieceColor().isBlack()){
                this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }
        if(moveHistory.getMoves().size() > 0){
            final Move lastMove = moveHistory.getMoves().get(moveHistory.size()-1);
            final String moveText = lastMove.toString();
            if(lastMove.getMovedPiece().getPieceColor().isWhite()){
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow, 0);
            }
            else if (lastMove.getMovedPiece().getPieceColor().isBlack()){
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow -1, 1);
            }
        }
        
        final JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private String calculateCheckAndCheckMateHash(Board board) {
        if(board.currentPlayer().isInCheckMate()){
            return "#";
        }else if(board.currentPlayer().isInCheck()){
            return "+";
        }
        return "";
    }
    
    private static class DataModel extends DefaultTableModel{
        private final List<Row> values;
        private static final String[] NAMES = {"Blancas", "Negras"};
        
        DataModel(){
            this.values = new ArrayList<>();
        }
        
        public void clear(){
            this.values.clear();
            setRowCount(0);
        }
        
        @Override
        public int getRowCount(){
            if(this.values == null){
                return 0;
            }
            return this.values.size();
        }
        
        @Override
        public int getColumnCount(){
            return NAMES.length;
        }
        
        @Override
        public Object getValueAt(final int row, final int column){
            final Row currentRow = this.values.get(row);
            if(column == 0){
                return currentRow.getWhiteMove();
            } else if(column == 1){
                return currentRow.getBlackMove();
            }
            return null;
        }
        
        @Override
        public void setValueAt(final Object aValue, final int row, final int column){
            final Row currentRow;
            if(this.values.size() <= row){
                currentRow = new Row();
                this.values.add(currentRow);
            }else{
                currentRow = this.values.get(row);
            }
            if(column == 0){
                currentRow.setWhiteMove((String)aValue);
                fireTableRowsInserted(row, row);
            } else if(column == 1){
                currentRow.setBlackMove((String)aValue);
                fireTableCellUpdated(row, column);
            }
        }
        
        @Override
        public Class<?> getColumnClass(final int column){
            return Move.class;
        }
        
        @Override
        public String getColumnName(final int column){
            return NAMES[column];
        }
    }
    
    private static class Row{
        private String whiteMove;
        private String blackMove;
        Row(){
            
        }

        public String getBlackMove() {
            return blackMove;
        }

        public void setBlackMove(String blackMove) {
            this.blackMove = blackMove;
        }

        public String getWhiteMove() {
            return whiteMove;
        }

        public void setWhiteMove(String whiteMove) {
            this.whiteMove = whiteMove;
        }
        
        
    }
    
}
