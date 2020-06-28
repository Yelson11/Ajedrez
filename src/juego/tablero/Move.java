package juego.tablero;

import juego.pieza.PieceColor;
import juego.pieza.Pawn;
import juego.pieza.Piece;
import juego.pieza.Rook;
import juego.tablero.Board.BoardBuilder;
import sun.security.provider.certpath.BuildStep;

/**
 *
 * @author emers
 */
public abstract class Move 
{
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;
    public static final Move NULL_MOVEMENT = new MovimientoNulo();
    
    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate)
    {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }
    
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }
    
    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
               getMovedPiece().equals(otherMove.getMovedPiece());
    }
    
    public int getCurrentCoordinate(){
        return this.getMovedPiece().getPiecePosition();
    }
    
    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }
    
    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public boolean isAtack(){
        return false;
    }
    
    public boolean isCastledMove(){
        return false;
    }
    
    public Piece getAttackedPiece(){
        return null;
    }
    
    public Board execute() {
            
        final Board.BoardBuilder boardBuilder = new BoardBuilder();

        for (final Piece pieza : this.board.currentPlayer().getActivePieces()){
            //TODO hashcode and equals for pieces
            if (!this.movedPiece.equals(pieza)){
                boardBuilder.setPiece(pieza);
            }
        }

        for (final Piece pieza : this.board.currentPlayer().getOponent().getActivePieces()){
            boardBuilder.setPiece(pieza);
        }

        //mueve la pieza movida
        boardBuilder.setPiece(this.movedPiece.movePiece(this));
        boardBuilder.setMoveMaker(this.board.currentPlayer().getOponent().getPieceColor());
        return boardBuilder.buildBoard();
    }
    
    public static final class MajorMove extends Move
    {
        public MajorMove(final Board tablero, final Piece movedPiece, final int destinationCoordinate) {
            super(tablero, movedPiece, destinationCoordinate);
        }   
    }
    
    public static class AtackMove extends Move
    {
        final Piece attackedPiece;
        
        public AtackMove(final Board tablero, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(tablero, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
       
        @Override
        public int hashCode() {            
            return this.attackedPiece.hashCode() + super.hashCode();
        } 
        
        @Override
        public boolean equals(final Object otro){            
            if(this == otro){
                return true;
            }
            if (!(otro instanceof AtackMove)){
                return false;
            }
            final AtackMove otherMove = (AtackMove) otro;
            return super.equals(otherMove) && getAttackedPiece().equals(otherMove.getAttackedPiece());
        } 
        
        @Override
        public Board execute() {            
            return null;
        } 
        
        @Override
        public boolean isAtack() {            
            return true;
        }
        
        @Override
        public Piece getAttackedPiece() {            
            return this.attackedPiece;
        }
    }
    
    public static final class MovimientoPeon extends Move
    {
        public MovimientoPeon(final Board tablero, final Piece movedPiece, final int destinationCoordinate) {
            super(tablero, movedPiece, destinationCoordinate);
        }   
    }
    
    public static class MovimientoAtaquePeon extends AtackMove
    {
        public MovimientoAtaquePeon(final Board tablero, final Piece movedPiece, final int destinationCoordinate, final Piece piezaAtacada) {
            super(tablero, movedPiece, destinationCoordinate, piezaAtacada);
        }   
    }
    
    public static final class MovimientoAtaquePeonPasajero extends AtackMove
    {
        public MovimientoAtaquePeonPasajero(final Board tablero, final Piece movedPiece, final int destinationCoordinate, final Piece piezaAtacada) {
            super(tablero, movedPiece, destinationCoordinate, piezaAtacada);
        }   
    }
    
    public static class SaltoPeon extends Move
    {
        public SaltoPeon(final Board tablero, final Piece movedPiece, final int destinationCoordinate) {
            super(tablero, movedPiece, destinationCoordinate);
        }
        
        @Override
        public Board execute(){
            final BoardBuilder builder = new BoardBuilder();
            for(final Piece pieza : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(pieza)){
                    builder.setPiece(pieza);
                }
            }
            for(final Piece pieza : this.board.currentPlayer().getOponent().getActivePieces()){
                builder.setPiece(pieza);
            }
            final Pawn peonMovido = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(peonMovido);
            builder.setPeonPasajero(peonMovido);
            builder.setMoveMaker(this.board.currentPlayer().getOponent().getPieceColor());
            return builder.buildBoard();
            
        }
    }
    
    static abstract class MovimientoCastillo extends Move{
        protected final Rook castle;
        protected final int castleInicio;
        protected final int castleDestino;
        public MovimientoCastillo(final Board tablero, final Piece movedPiece, final int destinationCoordinate, Rook castle, int castleIncio, int castleDestino) {
            super(tablero, movedPiece, destinationCoordinate);
            this.castle = castle;
            this.castleInicio = castleIncio;
            this.castleDestino = castleDestino;
        }
        
        public Rook getTorreCastle(){
            return this.castle;
        }
        
        @Override
        public boolean isCastledMove(){
            return true;
        }
        
        @Override
        public Board execute(){
            final BoardBuilder builder = new BoardBuilder();
            for(final Piece pieza : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(pieza) && !this.castle.equals(pieza)){
                    builder.setPiece(pieza);
                }
            }
            for(final Piece pieza : this.board.currentPlayer().getOponent().getActivePieces()){
                builder.setPiece(pieza);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castle.getPieceColor(), this.destinationCoordinate, false));
            builder.setMoveMaker(this.board.currentPlayer().getOponent().getPieceColor());
            return builder.buildBoard();
        }
    }
    
    public static class MovimientoCastilloLadoRey extends MovimientoCastillo{
        public MovimientoCastilloLadoRey(final Board tablero, final Piece movedPiece, final int destinationCoordinate, Rook castle, int castleIncio, int castleDestino) {
            super(tablero, movedPiece, destinationCoordinate, castle, castleIncio, castleDestino);
        }  
        
        @Override 
        public String toString(){
            return "0-0";
        }
    }
    
    public static class MovimientoCastilloLadoReina extends MovimientoCastillo{
        public MovimientoCastilloLadoReina(final Board tablero, final Piece movedPiece, final int destinationCoordinate, Rook castle, int castleIncio, int castleDestino) {
            super(tablero, movedPiece, destinationCoordinate, castle, castleIncio, castleDestino);
        }   
        @Override 
        public String toString(){
            return "0-0-0";
        }
    }
    
    public static class MovimientoNulo extends Move{
        public MovimientoNulo(){
            super(null, null, -1);
        }   
        
        @Override
        public Board execute(){
            throw new RuntimeException("No se puede ejecutar un movimiento nulo");
        }
    }
    
    public static class FabricaMovimientos{
        
        private FabricaMovimientos(){
            throw new RuntimeException("No inicializable");
        }
        public static Move crearMovimiento(final Board tablero, final int actualCoordenada, final int destinationCoordinate){
            for(final Move movimiento : tablero.getLegalMovements()){
                if(movimiento.getCurrentCoordinate() == actualCoordenada && movimiento.getDestinationCoordinate() == destinationCoordinate){
                    return movimiento;
                }
            }
            return NULL_MOVEMENT;
        }
    }
    
}
