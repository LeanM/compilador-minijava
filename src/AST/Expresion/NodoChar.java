package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;

public class NodoChar extends NodoOperando_Literal {
    public NodoChar(Token literal) {
        super(literal);
    }

    public void esta_bien_definido(){
        //Verificar para el literal especifico
    }

    @Override
    public Tipo get_tipo_expresion() throws ExcepcionTipo, ExcepcionSemantica {
        return new TipoPrimitivo(new Token("pr_char","char",0));
    }
}
