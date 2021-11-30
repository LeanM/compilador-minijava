package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;
import Traductor.Traductor;

import java.io.IOException;

public class NodoBoolean extends NodoOperando_Literal {
    public NodoBoolean(Token literal) {
        super(literal);
    }

    public void esta_bien_definido(){
        //Verificar para el literal especifico
    }

    @Override
    public Tipo get_tipo_expresion() throws ExcepcionTipo, ExcepcionSemantica {
        return new TipoPrimitivo(new Token("pr_boolean","boolean",0));
    }

    public void generar_codigo() throws IOException {
        if(token_expresion.get_lexema().equals("true"))
            Traductor.getInstance().gen("PUSH 1");
        else
            Traductor.getInstance().gen("PUSH 0");
    }
}