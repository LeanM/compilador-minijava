package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import Traductor.Traductor;

import java.io.IOException;

public class NodoNull extends NodoOperando_Literal {
    public NodoNull(Token literal) {
        super(literal);
    }

    public void esta_bien_definido(){
        //Verificar para el literal especifico
    }

    @Override
    public Tipo get_tipo_expresion() throws ExcepcionTipo, ExcepcionSemantica {
        return new TipoReferencia(token_expresion);
    }

    public void generar_codigo() throws IOException {
        Traductor.getInstance().gen("PUSH 0");
    }
}
