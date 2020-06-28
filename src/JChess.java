import gui.Table;
import juego.tablero.Board;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author edamazzio
 */
public class JChess {

    public static void main (String[] args){
        
        Board tablero = Board.createStandardBoard();
        System.out.println(tablero);
        
        Table table = new Table();

    }
    
}
