package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

public class NodoReturn_Expresion extends NodoReturn{

    private NodoExpresion expresion;

    public NodoReturn_Expresion(Token token_return, NodoExpresion expresion) {
        super(token_return);
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        expresion.esta_bien_definido();
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("Nodo return : ");
        expresion.mostrar_expresion();
    }
}
