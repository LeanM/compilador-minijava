package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

public class NodoIf extends NodoSentencia {

    protected NodoExpresion condicion;
    protected NodoSentencia cuerpo_then;

    public NodoIf(NodoExpresion condicion, NodoSentencia then){
        super();
        this.condicion = condicion;
        this.cuerpo_then = then;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        condicion.esta_bien_definido();
        cuerpo_then.esta_bien_definido();
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Nodo If ");
        condicion.mostrar_expresion();
        cuerpo_then.mostar_sentencia();
        System.out.println("-----------------------------------------------------------");
    }
}
