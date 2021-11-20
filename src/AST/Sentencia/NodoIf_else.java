package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import Traductor.*;

import java.io.IOException;

public class NodoIf_else extends NodoIf {

    private NodoSentencia cuerpo_else;

    public NodoIf_else(NodoExpresion condicion, NodoSentencia then, NodoSentencia cuerpo_else) {
        super(condicion, then);
        this.cuerpo_else = cuerpo_else;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        super.esta_bien_definido();
        cuerpo_else.esta_bien_definido();
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Nodo If else");
        condicion.mostrar_expresion();
        cuerpo_then.mostar_sentencia();
        cuerpo_else.mostar_sentencia();
        System.out.println("-----------------------------------------------------------");
    }

    public void generar_codigo() throws ExcepcionTipo, ExcepcionSemantica, IOException {
        int index_etiquetas_if = Index_etiquetas.getInstance().get_index();
        String etiqueta_else = "if_else_"+nombre_unidad_declarada+"_"+ index_etiquetas_if;
        String etiqueta_nop = "if_nop_"+nombre_unidad_declarada+"_"+ index_etiquetas_if;

        condicion.generar_codigo();
        //Si la condicion no se cumple salto al else
        Traductor.getInstance().gen("BF "+etiqueta_else);

        //Le seteo el mismo nombre de unidad declarada
        cuerpo_then.set_nombre_unidad_declarada(nombre_unidad_declarada);
        cuerpo_then.generar_codigo();
        Traductor.getInstance().gen("JUMP "+etiqueta_nop);
        Traductor.getInstance().gen_etiqueta(etiqueta_else);

        //Le seteo el mismo nombre de unidad declarada
        cuerpo_else.set_nombre_unidad_declarada(nombre_unidad_declarada);
        cuerpo_else.generar_codigo();
        Traductor.getInstance().gen_etiqueta(etiqueta_nop);
    }
}
