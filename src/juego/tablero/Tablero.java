package juego.tablero;

import juego.jugador.JugadorNegras;
import juego.jugador.Jugador;
import juego.jugador.JugadorBlancas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.HashMap;

import juego.pieza.Alfil;
import juego.pieza.ColorPieza;
import juego.pieza.Rey;
import juego.pieza.Caballo;
import juego.pieza.Peon;
import juego.pieza.Pieza;
import juego.pieza.Reina;
import juego.pieza.Torre;

/**
 *
 * @author emers
 */
public class Tablero {
    private final List<Casilla> tableroJuego;
    protected final Collection<Pieza> piezasBlancas;
    protected final Collection<Pieza> piezasNegras;

    private final JugadorBlancas jugadorBlancas;
    private final JugadorNegras jugadorNegras;
    private final Jugador jugadorActual;

    private Tablero(ConstructorTablero builder) {

        this.tableroJuego = crearTableroJuego(builder);
        this.piezasBlancas = calcularPiezasActivas(this.tableroJuego, ColorPieza.BLANCO);
        this.piezasNegras = calcularPiezasActivas(this.tableroJuego, ColorPieza.NEGRO);

        final Collection<Movimiento> movimientosPermitidosBlancas = calcularMovimientosPermitidos(this.piezasBlancas);
        final Collection<Movimiento> movimientosPermitidosNegras = calcularMovimientosPermitidos(this.piezasNegras);

        this.jugadorBlancas = new JugadorBlancas(this, movimientosPermitidosBlancas, movimientosPermitidosNegras);
        this.jugadorNegras = new JugadorNegras(this, movimientosPermitidosBlancas, movimientosPermitidosNegras);

        this.jugadorActual = builder.nextMoveMaker.escogerJugador (this.jugadorBlancas, this.jugadorNegras);

    }

    public Jugador jugadorActual(){
        return this.jugadorActual;
    }

    public Collection<Pieza> getPiezasNegras() {
        return this.piezasNegras;
    }

    public Collection<Pieza> getPiezasBlancas() {
        return this.piezasBlancas;
    }

    public Jugador jugadorBlancas() {
        return this.jugadorBlancas;
    }

    public Jugador jugadorNegras() {
        return this.jugadorNegras;
    }

    @Override
    public String toString() {
        final StringBuilder constructorString = new StringBuilder();
        for (int i = 0; i < tableroUtilitarios.NUM_CASILLAS; i++) {
            final String textoCasilla = this.tableroJuego.get(i).toString();
            constructorString.append(String.format("%3s", textoCasilla));
            if ((i + 1) % tableroUtilitarios.NUM_CASILLAS_POR_FILA == 0) {
                constructorString.append("\n");
            }
        }
        return constructorString.toString();
    }

    private Collection<Movimiento> calcularMovimientosPermitidos(final Collection<Pieza> piezas) {

        final List<Movimiento> movimientosPermitidos = new ArrayList<>();

        for (final Pieza pieza : piezas) {
            movimientosPermitidos.addAll(pieza.calculaMovimientosLegales(this));
        }

        return ImmutableList.copyOf(movimientosPermitidos);
    }

    private static Collection<Pieza> calcularPiezasActivas(final List<Casilla> tableroJuego, final ColorPieza color) {
        final List<Pieza> piezasActivas = new ArrayList<>();

        for(final Casilla casilla : tableroJuego) {
            if (casilla.casillaEstaOcupada()) {
                final Pieza pieza = casilla.getPieza();
                if (pieza.getPiezaColor() == color) {
                    piezasActivas.add(pieza);
                }
            }
        }
        return ImmutableList.copyOf(piezasActivas);
    }

    public Casilla getCasilla(final int coordenadaCasilla) {
        return tableroJuego.get(coordenadaCasilla);
    }

    private static List<Casilla> crearTableroJuego(final ConstructorTablero builder) {
        final Casilla[] casillas = new Casilla[tableroUtilitarios.NUM_CASILLAS];
        for (int i = 0; i < tableroUtilitarios.NUM_CASILLAS; i++) {
            casillas[i] = Casilla.crearCasilla(i, builder.configuracionTablero.get(i));
        }
        return ImmutableList.copyOf(casillas);
    }

    public static Tablero crearTableroEstandar() {
        final ConstructorTablero builder = new ConstructorTablero();
        // Black Layout
        builder.setPieza(new Torre(ColorPieza.NEGRO, 0));
        builder.setPieza(new Caballo(ColorPieza.NEGRO, 1));
        builder.setPieza(new Alfil(ColorPieza.NEGRO, 2));
        builder.setPieza(new Reina(ColorPieza.NEGRO, 3));
        builder.setPieza(new Rey(ColorPieza.NEGRO, 4));
        builder.setPieza(new Alfil(ColorPieza.NEGRO, 5));
        builder.setPieza(new Caballo(ColorPieza.NEGRO, 6));
        builder.setPieza(new Torre(ColorPieza.NEGRO, 7));
        builder.setPieza(new Peon(ColorPieza.NEGRO, 8));
        builder.setPieza(new Peon(ColorPieza.NEGRO, 9));
        builder.setPieza(new Peon(ColorPieza.NEGRO, 10));
        builder.setPieza(new Peon(ColorPieza.NEGRO, 11));
        builder.setPieza(new Peon(ColorPieza.NEGRO, 12));
        builder.setPieza(new Peon(ColorPieza.NEGRO, 13));
        builder.setPieza(new Peon(ColorPieza.NEGRO, 14));
        builder.setPieza(new Peon(ColorPieza.NEGRO, 15));
        // White Layout
        builder.setPieza(new Peon(ColorPieza.BLANCO, 48));
        builder.setPieza(new Peon(ColorPieza.BLANCO, 49));
        builder.setPieza(new Peon(ColorPieza.BLANCO, 50));
        builder.setPieza(new Peon(ColorPieza.BLANCO, 51));
        builder.setPieza(new Peon(ColorPieza.BLANCO, 52));
        builder.setPieza(new Peon(ColorPieza.BLANCO, 53));
        builder.setPieza(new Peon(ColorPieza.BLANCO, 54));
        builder.setPieza(new Peon(ColorPieza.BLANCO, 55));
        builder.setPieza(new Torre(ColorPieza.BLANCO, 56));
        builder.setPieza(new Caballo(ColorPieza.BLANCO, 57));
        builder.setPieza(new Alfil(ColorPieza.BLANCO, 58));
        builder.setPieza(new Reina(ColorPieza.BLANCO, 59));
        builder.setPieza(new Rey(ColorPieza.BLANCO, 60));
        builder.setPieza(new Alfil(ColorPieza.BLANCO, 61));
        builder.setPieza(new Caballo(ColorPieza.BLANCO, 62));
        builder.setPieza(new Torre(ColorPieza.BLANCO, 63));
        // white to move
        builder.setMoveMaker(ColorPieza.BLANCO);
        // build the board
        return builder.construirTablero();
    }

    Iterable<Movimiento> getMovimientosLegales() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.jugadorBlancas.getMovimientosPermitidos(), this.jugadorNegras.getMovimientosPermitidos()));
    }

    public static class ConstructorTablero {

        Map<Integer, Pieza> configuracionTablero;
        ColorPieza nextMoveMaker;
        Peon peonPasajero;

        public ConstructorTablero() {
            this.configuracionTablero = new HashMap<Integer, Pieza>();
        }

        public ConstructorTablero setPieza(final Pieza pieza) {
            this.configuracionTablero.put(pieza.getPosicionPieza(), pieza);
            return this;
        }

        public ConstructorTablero setMoveMaker(final ColorPieza nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Tablero construirTablero() {
            return new Tablero(this);
        }

        void setPeonPasajero(Peon peonMovido) {
            this.peonPasajero = peonMovido;
        }

    }

}
