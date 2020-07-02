package juego.tablero;

import juego.pieza.Pawn;
import juego.pieza.Piece;
import juego.pieza.PieceColor;
import juego.pieza.Rook;
import juego.tablero.Board.BoardBuilder;

/**
 *
 * @author emers
 */
public abstract class Move 
{


    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;
    public static final Move NULL_MOVEMENT = new NullMove();
    
    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate)
    {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }
    
    private Move(final Board board, final int destinationCoordinate){
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }
    
    public Board getBoard() {
        return board;
    }
    
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
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
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
               getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
               getMovedPiece().equals(otherMove.getMovedPiece());
    }
    
    public Board execute() {
        final BoardBuilder boardBuilder = new BoardBuilder();
        for (final Piece piece : this.board.currentPlayer().getActivePieces()){
            if (!this.movedPiece.equals(piece)){
                boardBuilder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.currentPlayer().getOponent().getActivePieces()){
            boardBuilder.setPiece(piece);
        }

        //mueve la pieza movida
        boardBuilder.setPiece(this.movedPiece.movePiece(this));
        boardBuilder.setMoveMaker(this.board.currentPlayer().getOponent().getPiecesColor());
        return boardBuilder.buildBoard();
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
    
    public static class MajorAttackMove extends AtackMove{
        
        public MajorAttackMove(Board board, Piece pieceMoved, int destinationCoordinate, Piece attackedPiece) {
            super(board, pieceMoved, destinationCoordinate, attackedPiece);
        }
        
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }
        
        @Override
        public String toString(){
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
        
    }    
    public static final class MajorMove extends Move
    {
        public MajorMove(final Board tablero, final Piece movedPiece, final int destinationCoordinate) {
            super(tablero, movedPiece, destinationCoordinate);
        }   
        
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }
        
        @Override
        public String toString(){
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    
    public static class AtackMove extends Move
    {
        final Piece attackedPiece;
        
        public AtackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
       
        @Override
        public int hashCode() {            
            return this.attackedPiece.hashCode() + super.hashCode();
        } 
        
        @Override
        public boolean equals(final Object other){            
            if(this == other){
                return true;
            }
            if (!(other instanceof AtackMove)){
                return false;
            }
            final AtackMove otherMove = (AtackMove) other;
            return super.equals(otherMove) && getAttackedPiece().equals(otherMove.getAttackedPiece());
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
    
    public static final class PawnMove extends Move
    {
        public PawnMove(final Board tablero, final Piece movedPiece, final int destinationCoordinate) {
            super(tablero, movedPiece, destinationCoordinate);
        }   
        
        @Override 
        public boolean equals (final Object other){
            return this == other || other instanceof PawnMove && super.equals(other);
        }
        
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    
    public static class PawnAttackMove extends AtackMove
    {
        public PawnAttackMove(final Board tablero, final Piece movedPiece, final int destinationCoordinate, final Piece piezaAtacada) {
            super(tablero, movedPiece, destinationCoordinate, piezaAtacada);
        }
        
        @Override 
        public boolean equals (final Object other){
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }
        
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0,1) + "-x-" +  BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    
    public static final class PawnAttackPassenger extends AtackMove
    {
        public PawnAttackPassenger(final Board tablero, final Piece movedPiece, final int destinationCoordinate, final Piece piezaAtacada) {
            super(tablero, movedPiece, destinationCoordinate, piezaAtacada);
        }    
    }
    
    public static final class PawnEnPassantAttackMove extends PawnAttackMove{
        
        public PawnEnPassantAttackMove(Board tablero, Piece movedPiece, int destinationCoordinate, Piece piezaAtacada) {
            super(tablero, movedPiece, destinationCoordinate, piezaAtacada);
        }
        
        @Override 
        public boolean equals (final Object other){
            return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
        }
        
        @Override
        public Board execute() {
            
            final BoardBuilder builder = new BoardBuilder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()){
                if (!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOponent().getActivePieces()){
                if (!piece.equals(this.getAttackedPiece())){
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOponent().getPiecesColor());
            return builder.buildBoard();
        }
        
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0,1) + "-x-" +  BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        } 
        
        
        
    }
    
    public static class PawnPromotion extends Move{
        final Move decoratedMove;
        final Pawn promotedPawn;
        
        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }
        
        @Override
        public int hashCode(){
            return this.decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }
        
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnPromotion && (super.equals(other));
        }
        
        @Override
        public Board execute(){
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Board.BoardBuilder builder = new BoardBuilder();
            for(final Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()){
                if(!this.promotedPawn.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : pawnMovedBoard.currentPlayer().getOponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getPiecesColor());
            return builder.buildBoard();
        }
         
        @Override
        public boolean isAtack(){
            return this.decoratedMove.isAtack();
        }
        
        @Override
        public Piece getAttackedPiece(){
            return this.decoratedMove.getAttackedPiece();
        }
        
        @Override
        public String toString(){
            return "";
        }
    }
    
    public static class PawnJump extends Move
    {
        public PawnJump(final Board tablero, final Piece movedPiece, final int destinationCoordinate) {
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
            builder.setMoveMaker(this.board.currentPlayer().getOponent().getPiecesColor());
            return builder.buildBoard();
        }
        
        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(destinationCoordinate);
        }
    }
    
    static abstract class CastleMove extends Move{
        protected final Rook castle;
        protected final int castleInicio;
        protected final int castleDestino;
        public CastleMove(final Board tablero, final Piece movedPiece, final int destinationCoordinate, Rook castle, int castleIncio, int castleDestino) {
            super(tablero, movedPiece, destinationCoordinate);
            this.castle = castle;
            this.castleInicio = castleIncio;
            this.castleDestino = castleDestino;
        }
        
        public Rook getCastleRook(){
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
            //TODO look into the first move on normal pieces 
            builder.setPiece(new Rook(this.castle.getPieceColor(), this.destinationCoordinate, false));
            builder.setMoveMaker(this.board.currentPlayer().getOponent().getPiecesColor());
            return builder.buildBoard();
        }
        
        @Override
        public int hashCode(){
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castle.hashCode();
            result = prime * result + this.castleDestino;
            return result;
        }
        
        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CastleMove)) {
                return false;
            }
            final CastleMove otherCastleMove = (CastleMove)other;
            return super.equals(otherCastleMove) && this.castle.equals(otherCastleMove.getCastleRook());
        }
    }
    
    public static class KingSideCastleMove extends CastleMove{
        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate, Rook castle,  int castleIncio, int castleDestino) {
            super(board, movedPiece, destinationCoordinate, castle, castleIncio, castleDestino);
        }  
        
        @Override 
        public String toString(){
            return "0-0";
        }
        
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof KingSideCastleMove && super.equals(other);
        }
    }
    
    public static class QueenSideCastleMove extends CastleMove{
        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate, Rook castle, int castleRookStart, int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castle, castleRookStart, castleRookDestination);
        }   
        @Override 
        public String toString(){
            return "0-0-0";
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof QueenSideCastleMove && super.equals(other);
        }
    }
    
    public static class NullMove extends Move{
        public NullMove(){
            super(null, 65);
        }   
        
        @Override
        public Board execute(){
            throw new RuntimeException("No se puede ejecutar un movimiento nulo");
        }
        
        @Override
        public int getCurrentCoordinate(){
            return -1;
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
    
    public enum MoveStatus {
    
        DONE {
            @Override
            boolean isDone() {
                return true;
            }
        },
        ILLEGAL {

            @Override
            boolean isDone() {
                return false;
            }

        },
        LEAVES_PLAYER_IN_CHECK {
            @Override
            boolean isDone() {
                return false;
            }
        };

        public abstract boolean isDone();
    }
    
}




