package AST.Sentencia;

import AST.Expresion.NodoExpresion;

public class NodoIf extends NodoSentencia {

    private NodoExpresion condicion;
    private NodoSentencia cuerpo_then;

    public NodoIf(NodoExpresion condicion, NodoSentencia then){
        super();
        this.condicion = condicion;
        this.cuerpo_then = then;
    }

    @Override
    public void esta_bien_definido() {

    }
}
