package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

public class NodoReturn_Expresion extends NodoReturn{

    private NodoExpresion expresion;

    public NodoReturn_Expresion(Token token_return, NodoExpresion expresion, Tipo tipo_metodo) {
        super(token_return,tipo_metodo);
        this.expresion = expresion;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        expresion.esta_bien_definido();
        if(!expresion.get_tipo_expresion().es_de_tipo(tipo_metodo))
            throw new ExcepcionTipo(token_return,"El tipo de la expresion de retorno no conforma el tipo que retorna el metodo");
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("Nodo return : ");
        expresion.mostrar_expresion();
    }
}
