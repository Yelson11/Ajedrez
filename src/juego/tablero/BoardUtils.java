package juego.tablero;

/**
 *
 * @author emers
 */
public class BoardUtils 
{
    
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static boolean[] SECOND_COLUMN = initColumn(1);
    public static boolean[] SEVENTH_COLUMN = initColumn(6);
    public static boolean[] EIGHT_COLUMN = initColumn(7);
    
    public static final boolean[] EIGHT_RANK = initRow(0);
    public static final boolean[] SEVENTH_RANK = initRow(8);
    public static final boolean[] SIXTH_RANK = initRow(16);
    public static final boolean[] FIFTH_RANK = initRow(24);
    public static final boolean[] FOURTH_RANK = initRow(32);
    public static final boolean[] THIRD_RANK = initRow(40);
    public static final boolean[] SECOND_RANK = initRow(48);
    public static final boolean[] FIRST_RANK = initRow(56);
    
    public static final int TILE_NUMBER = 64;
    public static final int TILE_NUMBER_PER_ROW = 8;
    
    private BoardUtils(){
        throw new RuntimeException("No me puedes instanciar");
    }
    
    private static boolean[] initColumn(int columnNumber){
        final boolean[] column = new boolean[TILE_NUMBER];
        do{
            column[columnNumber] = true;
            columnNumber += TILE_NUMBER_PER_ROW;
        } while(columnNumber < TILE_NUMBER);
        return column;
    }
    
    private static boolean[] initRow (int rowNumber){
        final boolean[] fila = new boolean[TILE_NUMBER];
        do {
            fila[rowNumber] = true;
            rowNumber++;
        }
        while (rowNumber % TILE_NUMBER_PER_ROW != 0);
        
        return fila;
    }
    
    public static boolean tileIsValid(final int coordenada){
        return coordenada >=0 && coordenada < TILE_NUMBER;
    }
    
}
