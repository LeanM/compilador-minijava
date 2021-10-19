package AnalizadorLexico;

import java.io.*;
import java.util.Scanner;

public class GestorArchivo {

    private int nro_linea, nro_char_actual;
    private String linea_actual;
    private boolean esEOF;
    private Scanner scanner;

    private static GestorArchivo instance = null;

    public GestorArchivo(File programa) throws FileNotFoundException {
        scanner = new Scanner(programa);
        esEOF = false;
        nro_linea = 1;
        nro_char_actual = 0;
        if (scanner.hasNextLine()) {
            this.linea_actual = scanner.nextLine();
            this.linea_actual = linea_actual + (char) 10; //Agrego enter al final de la linea (scanner no los toma)
        }
        else esEOF = true;
    }

    public char getNextChar() throws IOException{
        char toReturn;

        if ((this.linea_actual != null) && (nro_char_actual < this.linea_actual.length())) {
            toReturn = this.linea_actual.charAt(nro_char_actual++);
        }
        else
            if (scanner.hasNextLine()) {
                this.linea_actual = scanner.nextLine();
                this.linea_actual = linea_actual + (char) 10; //Agrego enter al final de la linea (scanner no lo toma)
                this.nro_linea++;
                this.nro_char_actual = 0;
                toReturn = linea_actual.charAt(nro_char_actual++);
            }
            else {
                this.nro_char_actual++;
                toReturn = (char) 0;
                esEOF = true;
            }
        return toReturn;
    }

    public int getNro_linea(){
        return nro_linea;
    }
    public String get_Linea(){
        return linea_actual;
    }
    public int get_nro_char_actual(){
        return nro_char_actual;
    }

    public boolean esEOF(){
        return esEOF;
    }

}
