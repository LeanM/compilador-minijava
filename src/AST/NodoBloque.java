package AST;

import AST.Sentencia.NodoSentencia;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

import java.util.LinkedList;

public class NodoBloque extends NodoSentencia {

    private LinkedList<NodoSentencia> lista_sentencias;

    public NodoBloque(LinkedList<NodoSentencia> lista){
        super();
        lista_sentencias = lista;
    }

    public void setSentencia(NodoSentencia sentencia_nueva) {
        lista_sentencias.addLast(sentencia_nueva);
    }
    public LinkedList<NodoSentencia> get_lista_sentencias(){ return lista_sentencias; }

    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        //Verificar toda la lista de sentencias
        for(NodoSentencia ns : lista_sentencias)
            ns.esta_bien_definido();
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("NodoBloque : ");
        for(NodoSentencia ns : lista_sentencias)
            ns.mostar_sentencia();
    }
}
