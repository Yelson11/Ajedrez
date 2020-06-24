package juego.tablero;

import juego.pieza.ColorPieza;
import juego.pieza.Peon;
import juego.pieza.Pieza;
import juego.pieza.Torre;
import juego.tablero.Tablero.ConstructorTablero;
import sun.security.provider.certpath.BuildStep;

/**
 *
 * @author emers
 */
public abstract class Movimiento 
{
    final Tablero tablero;
    final Pieza piezaMovida;
    final int coordenadaDestino;
    public static final Movimiento MOVIMIENTO_NULO = new MovimientoNulo();
    
    private Movimiento(final Tablero tablero, final Pieza piezaMovida, final int coordenadaDestino)
    {
        this.tablero = tablero;
        this.piezaMovida = piezaMovida;
        this.coordenadaDestino = coordenadaDestino;
    }
    
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + this.coordenadaDestino;
        result = prime * result + this.piezaMovida.hashCode();
        return result;
    }
    
    @Override
    public boolean equals(final Object otro){
        if(this == otro){
            return true;
        }
        if(!(otro instanceof Movimiento)){
            return false;
        }
        final Movimiento otroMovimiento = (Movimiento) otro;
        return getCoordenadaDestino() == otroMovimiento.getCoordenadaDestino() &&
               getPiezaMovida().equals(otroMovimiento.getPiezaMovida());
    }
    
    public int getCoordenadaActual(){
        return this.getPiezaMovida().getPosicionPieza();
    }
    
    public int getCoordenadaDestino() {
        return this.coordenadaDestino;
    }
    
    public Pieza getPiezaMovida(){
        return this.piezaMovida;
    }

    public boolean esAtaque(){
        return false;
    }
    
    public boolean esMovimientoCastillo(){
        return false;
    }
    
    public Pieza getPiezaAtacada(){
        return null;
    }
    
    public Tablero ejecutar() {
            
        final Tablero.ConstructorTablero constructorTablero = new ConstructorTablero();

        for (final Pieza pieza : this.tablero.jugadorActual().getPiezasActivas()){
            //TODO hashcode and equals for pieces
            if (!this.piezaMovida.equals(pieza)){
                constructorTablero.setPieza(pieza);
            }
        }

        for (final Pieza pieza : this.tablero.jugadorActual().getOponente().getPiezasActivas()){
            constructorTablero.setPieza(pieza);
        }

        //mueve la pieza movida
        constructorTablero.setPieza(this.piezaMovida.moverPieza(this));
        constructorTablero.setMoveMaker(this.tablero.jugadorActual().getOponente().getColor());
        return constructorTablero.construirTablero();
    }
    
    public static final class MovimientoMayor extends Movimiento
    {
        public MovimientoMayor(final Tablero tablero, final Pieza piezaMover, final int destinoCoordenada) {
            super(tablero, piezaMover, destinoCoordenada);
        }   
    }
    
    public static class MovimientoAtaque extends Movimiento
    {
        final Pieza piezaAtaque;
        
        public MovimientoAtaque(final Tablero tablero, final Pieza piezaMover, final int destinoCoordenada, final Pieza piezaAtaque) {
            super(tablero, piezaMover, destinoCoordenada);
            this.piezaAtaque = piezaAtaque;
        }
       
        @Override
        public int hashCode() {            
            return this.piezaAtaque.hashCode() + super.hashCode();
        } 
        
        @Override
        public boolean equals(final Object otro){            
            if(this == otro){
                return true;
            }
            if (!(otro instanceof MovimientoAtaque)){
                return false;
            }
            final MovimientoAtaque otroMovimiento = (MovimientoAtaque) otro;
            return super.equals(otroMovimiento) && getPiezaAtacada().equals(otroMovimiento.getPiezaAtacada());
        } 
        
        @Override
        public Tablero ejecutar() {            
            return null;
        } 
        
        @Override
        public boolean esAtaque() {            
            return true;
        }
        
        @Override
        public Pieza getPiezaAtacada() {            
            return this.piezaAtaque;
        }
    }
    
    public static final class MovimientoPeon extends Movimiento
    {
        public MovimientoPeon(final Tablero tablero, final Pieza piezaMover, final int destinoCoordenada) {
            super(tablero, piezaMover, destinoCoordenada);
        }   
    }
    
    public static class MovimientoAtaquePeon extends MovimientoAtaque
    {
        public MovimientoAtaquePeon(final Tablero tablero, final Pieza piezaMover, final int destinoCoordenada, final Pieza piezaAtacada) {
            super(tablero, piezaMover, destinoCoordenada, piezaAtacada);
        }   
    }
    
    public static final class MovimientoAtaquePeonPasajero extends MovimientoAtaque
    {
        public MovimientoAtaquePeonPasajero(final Tablero tablero, final Pieza piezaMover, final int destinoCoordenada, final Pieza piezaAtacada) {
            super(tablero, piezaMover, destinoCoordenada, piezaAtacada);
        }   
    }
    
    public static class SaltoPeon extends Movimiento
    {
        public SaltoPeon(final Tablero tablero, final Pieza piezaMover, final int destinoCoordenada) {
            super(tablero, piezaMover, destinoCoordenada);
        }
        
        @Override
        public Tablero ejecutar(){
            final ConstructorTablero builder = new ConstructorTablero();
            for(final Pieza pieza : this.tablero.jugadorActual().getPiezasActivas()){
                if(!this.piezaMovida.equals(pieza)){
                    builder.setPieza(pieza);
                }
            }
            for(final Pieza pieza : this.tablero.jugadorActual().getOponente().getPiezasActivas()){
                builder.setPieza(pieza);
            }
            final Peon peonMovido = (Peon)this.piezaMovida.moverPieza(this);
            builder.setPieza(peonMovido);
            builder.setPeonPasajero(peonMovido);
            builder.setMoveMaker(this.tablero.jugadorActual().getOponente().getColor());
            return builder.construirTablero();
            
        }
    }
    
    static abstract class MovimientoCastillo extends Movimiento{
        protected final Torre castle;
        protected final int castleInicio;
        protected final int castleDestino;
        public MovimientoCastillo(final Tablero tablero, final Pieza piezaMover, final int destinoCoordenada, Torre castle, int castleIncio, int castleDestino) {
            super(tablero, piezaMover, destinoCoordenada);
            this.castle = castle;
            this.castleInicio = castleIncio;
            this.castleDestino = castleDestino;
        }
        
        public Torre getTorreCastle(){
            return this.castle;
        }
        
        @Override
        public boolean esMovimientoCastillo(){
            return true;
        }
        
        @Override
        public Tablero ejecutar(){
            final ConstructorTablero builder = new ConstructorTablero();
            for(final Pieza pieza : this.tablero.jugadorActual().getPiezasActivas()){
                if(!this.piezaMovida.equals(pieza) && !this.castle.equals(pieza)){
                    builder.setPieza(pieza);
                }
            }
            for(final Pieza pieza : this.tablero.jugadorActual().getOponente().getPiezasActivas()){
                builder.setPieza(pieza);
            }
            builder.setPieza(this.piezaMovida.moverPieza(this));
            builder.setPieza(new Torre(this.castle.getPiezaColor(), this.coordenadaDestino, false));
            builder.setMoveMaker(this.tablero.jugadorActual().getOponente().getColor());
            return builder.construirTablero();
        }
    }
    
    public static class MovimientoCastilloLadoRey extends MovimientoCastillo{
        public MovimientoCastilloLadoRey(final Tablero tablero, final Pieza piezaMover, final int destinoCoordenada, Torre castle, int castleIncio, int castleDestino) {
            super(tablero, piezaMover, destinoCoordenada, castle, castleIncio, castleDestino);
        }  
        
        @Override 
        public String toString(){
            return "0-0";
        }
    }
    
    public static class MovimientoCastilloLadoReina extends MovimientoCastillo{
        public MovimientoCastilloLadoReina(final Tablero tablero, final Pieza piezaMover, final int destinoCoordenada, Torre castle, int castleIncio, int castleDestino) {
            super(tablero, piezaMover, destinoCoordenada, castle, castleIncio, castleDestino);
        }   
        @Override 
        public String toString(){
            return "0-0-0";
        }
    }
    
    public static class MovimientoNulo extends Movimiento{
        public MovimientoNulo(){
            super(null, null, -1);
        }   
        
        @Override
        public Tablero ejecutar(){
            throw new RuntimeException("No se puede ejecutar un movimiento nulo");
        }
    }
    
    public static class FabricaMovimientos{
        
        private FabricaMovimientos(){
            throw new RuntimeException("No inicializable");
        }
        public static Movimiento crearMovimiento(final Tablero tablero, final int actualCoordenada, final int destinoCoordenada){
            for(final Movimiento movimiento : tablero.getMovimientosLegales()){
                if(movimiento.getCoordenadaActual() == actualCoordenada && movimiento.getCoordenadaDestino() == destinoCoordenada){
                    return movimiento;
                }
            }
            return MOVIMIENTO_NULO;
        }
    }
    
}
