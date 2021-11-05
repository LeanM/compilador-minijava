package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;

public class NodoEntero extends NodoOperando_Literal {
    public NodoEntero(Token literal) {
        super(literal);
    }

    public void esta_bien_definido(){
        //Verificar para el literal especifico
    }

    @Override
    public Tipo get_tipo_expresion() throws ExcepcionTipo, ExcepcionSemantica {
        return new TipoPrimitivo(new Token("pr_int","int",0));
    }
}
