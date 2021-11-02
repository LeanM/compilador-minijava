package AST;

import AST.Sentencia.NodoSentencia;

import java.util.LinkedList;

public class NodoBloque {

    private LinkedList<NodoSentencia> lista_sentencias;

    public NodoBloque(){
        lista_sentencias = new LinkedList<NodoSentencia>();
    }

    public void setSentencia(NodoSentencia sentencia_nueva) {
        lista_sentencias.addLast(sentencia_nueva);
    }

    public void esta_bien_definido(){
        //Verificar toda la lista de sentencias
    }
}
