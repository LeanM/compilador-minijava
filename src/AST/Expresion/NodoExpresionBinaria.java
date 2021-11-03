package AST.Expresion;

import AnalizadorLexico.Token;

public class NodoExpresionBinaria extends NodoExpresion {

    private NodoExpresion expresion_izq;
    //private Token operador_binario;
    private NodoExpresion expresion_der;

    public NodoExpresionBinaria(NodoExpresion exp_izq, Token op_binario, NodoExpresion exp_der){
        super(op_binario);
        this.expresion_izq = exp_izq;
        this.expresion_der = exp_der;
        //this.operador_binario = op_binario;
    }

    @Override
    public void esta_bien_definido() {

    }

    @Override
    public void mostrar_expresion() {
        System.out.println("Expresion Binaria : ");
        expresion_izq.mostrar_expresion();
        System.out.println(token_expresion.get_lexema());
        expresion_der.mostrar_expresion();
    }
}
