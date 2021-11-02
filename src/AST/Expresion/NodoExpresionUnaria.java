package AST.Expresion;

import AnalizadorLexico.Token;

public class NodoExpresionUnaria extends NodoExpresion {

    private Token operador_unario;
    private NodoOperando operando;

    public NodoExpresionUnaria(Token op_unario, NodoOperando operando){
        super();
        this.operador_unario = op_unario;
        this.operando = operando;
    }

    @Override
    public void esta_bien_definido() {

    }
}
