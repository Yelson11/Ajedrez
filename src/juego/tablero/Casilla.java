package juego.tablero;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import juego.pieza.Pieza;


/**
 *
 * @author emers
 */
public abstract class Casilla 
{
    
    protected final int coordenadaCasilla;
    private static final Map<Integer, CasillaVacia> casillasVacias = crearCasillasVaciosPosibles();
    
    private static Map<Integer, CasillaVacia> crearCasillasVaciosPosibles() 
    {
        final Map<Integer, CasillaVacia> mapaVacio = new HashMap<>();
        
        for(int i =0; i < tableroUtilitarios.NUM_CASILLAS; i++)
        {
            mapaVacio.put(i, new CasillaVacia(i));
        }
        
        //Collections.unmodifiableMap(mapaVacio);
        return ImmutableMap.copyOf(mapaVacio);
    }
    
    private Casilla(int coordenadaCasilla)
    {
        this.coordenadaCasilla = coordenadaCasilla;
    }
    
    public abstract boolean casillaEstaOcupada();
    
    public abstract Pieza getPieza();
    
    public static Casilla crearCasilla(final int coordenadaCasilla, final Pieza pieza)
    {
        return pieza != null ? new CasillaOcupada(coordenadaCasilla, pieza) : casillasVacias.get(coordenadaCasilla);
    }
    
    public static final class CasillaVacia extends Casilla
    {
        
        CasillaVacia(final int coordenada)
        {
            super(coordenada);
        }

        @Override
        public String toString (){
            return "-";
        }
        
        @Override
        public boolean casillaEstaOcupada()
        {
            return false;
        }
        public Pieza getPieza()
        {
            return null;
        }
        
    }
    
     public static final class CasillaOcupada extends Casilla
    {
         
        private final Pieza piezaEnCasilla;
        
        CasillaOcupada(int coordenadaCasilla, Pieza piezaEnCasilla)
        {
            super(coordenadaCasilla);
            this.piezaEnCasilla = piezaEnCasilla;
        }

        @Override
        public String toString (){
            return getPieza().getPiezaColor().esNegra() ? getPieza().toString().toLowerCase() :
                    getPieza().toString(); 
        }
        
        
        @Override
        public boolean casillaEstaOcupada()
        {
            return true;
        }
        public Pieza getPieza()
        {
            return this.piezaEnCasilla;
        }
        
    }
    public int getCoordenadaCasilla() {
        return coordenadaCasilla;
    }
}
