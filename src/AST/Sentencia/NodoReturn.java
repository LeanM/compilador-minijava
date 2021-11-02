package AST.Sentencia;

import AST.Expresion.NodoExpresion;

public class NodoReturn extends NodoSentencia {

    private NodoExpresion expresion;

    public NodoReturn(){
        super();
    }

    public NodoReturn(NodoExpresion expresion){
        super();
        this.expresion = expresion;
    }

    @Override
    public void esta_bien_definido() {
        //expresion puede ser null
    }
}
