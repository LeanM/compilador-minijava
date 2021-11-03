package AST;

import AST.Sentencia.NodoSentencia;

import java.util.LinkedList;

public class NodoBloque extends NodoSentencia {

    private LinkedList<NodoSentencia> lista_sentencias;

    public NodoBloque(LinkedList<NodoSentencia> lista){
        lista_sentencias = lista;
    }

    public void setSentencia(NodoSentencia sentencia_nueva) {
        lista_sentencias.addLast(sentencia_nueva);
    }
    public LinkedList<NodoSentencia> get_lista_sentencias(){ return lista_sentencias; }

    public void esta_bien_definido(){
        //Verificar toda la lista de sentencias
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("NodoBloque : ");
        for(NodoSentencia ns : lista_sentencias)
            ns.mostar_sentencia();
    }
}
