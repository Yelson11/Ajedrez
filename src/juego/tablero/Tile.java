package juego.tablero;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import juego.pieza.Piece;


/**
 *
 * @author emers
 */
public abstract class Tile 
{
    
    protected final int tileCoordinate;
    private static final Map<Integer, EmptyTile> emptyTiles=  createPossibleEmptyTiles();
    
    private static Map<Integer, EmptyTile> createPossibleEmptyTiles() 
    {
        final Map<Integer, EmptyTile> emptyMap = new HashMap<>();
        
        for(int i =0; i < BoardUtils.TILE_NUMBER; i++)
        {
            emptyMap.put(i, new EmptyTile(i));
        }
        
        //Collections.unmodifiableMap(mapaVacio);
        return ImmutableMap.copyOf(emptyMap);
    }
    
    private Tile(int tileCoordinate)
    {
        this.tileCoordinate = tileCoordinate;
    }
    
    public abstract boolean tileIsOccupied();
    
    public abstract Piece getPiece();
    
    public static Tile createTile(final int tileCoordinate, final Piece piece)
    {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : emptyTiles.get(tileCoordinate);
    }
    
    public static final class EmptyTile extends Tile
    {
        
        EmptyTile(final int coordinate)
        {
            super(coordinate);
        }

        @Override
        public String toString (){
            return "-";
        }
        
        @Override
        public boolean tileIsOccupied()
        {
            return false;
        }
        public Piece getPiece()
        {
            return null;
        }
        
    }
    
     public static final class OccupiedTile extends Tile
    {
         
        private final Piece pieceInTile;
        
        OccupiedTile(int tileCoordinate, Piece pieceInTile)
        {
            super(tileCoordinate);
            this.pieceInTile = pieceInTile;
        }

        @Override
        public String toString (){
            return getPiece().getPieceColor().isBlack() ? getPiece().toString().toLowerCase() :
                    getPiece().toString(); 
        }
        
        
        @Override
        public boolean tileIsOccupied()
        {
            return true;
        }
        public Piece getPiece()
        {
            return this.pieceInTile;
        }
        
    }
    public int getTileCoordinate() {
        return tileCoordinate;
    }
}
