package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;
import Traductor.Traductor;

import java.io.IOException;

public class NodoString extends NodoOperando_Literal {
    public NodoString(Token literal) {
        super(literal);
    }

    public void esta_bien_definido(){
        //Verificar para el literal especifico
    }

    @Override
    public Tipo get_tipo_expresion() throws ExcepcionTipo, ExcepcionSemantica {
        return new TipoPrimitivo(new Token("pr_String","String",0));
    }

    public void generar_codigo() throws IOException {
        Traductor.getInstance().set_modo_data();
        String nombre_etiqueta = token_expresion.get_lexema().replace("\"","");
        Traductor.getInstance().gen_basico("l_"+nombre_etiqueta+": DW "+token_expresion.get_lexema()+",0");
        Traductor.getInstance().set_modo_code();
        Traductor.getInstance().gen("PUSH "+"l_"+nombre_etiqueta);
    }
}