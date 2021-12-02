package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import Traductor.*;

import java.io.IOException;

public class NodoIf extends NodoSentencia {

    protected NodoExpresion condicion;
    protected NodoSentencia cuerpo_then;

    public NodoIf(NodoExpresion condicion, NodoSentencia then){
        super();
        this.condicion = condicion;
        this.cuerpo_then = then;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        condicion.esta_bien_definido();
        if(en_metodo_static)
            condicion.chequeo_acceso_estatico();

        if(!condicion.get_tipo_expresion().getNombre().equals("boolean"))
            throw new ExcepcionTipo(condicion.getToken(),"Error de tipo : La expresion condicion en un if debe ser de tipo boolean");

        cuerpo_then.esta_bien_definido();
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Nodo If ");
        condicion.mostrar_expresion();
        cuerpo_then.mostar_sentencia();
        System.out.println("-----------------------------------------------------------");
    }

    @Override
    public void generar_codigo() throws ExcepcionTipo, ExcepcionSemantica, IOException {
        int index_etiquetas_if = Index_etiquetas.getInstance().get_index();
        String etiqueta_nop = "if_nop_"+nombre_unidad_declarada+"_"+ index_etiquetas_if;
        condicion.generar_codigo();
        //Si la condicion no se cumple salto afuera del then
        Traductor.getInstance().gen("BF "+etiqueta_nop);

        //Le seteo el mismo nombre de unidad declarada
        cuerpo_then.set_nombre_unidad_declarada(nombre_unidad_declarada);
        cuerpo_then.generar_codigo();
        Traductor.getInstance().gen_etiqueta(etiqueta_nop);
    }
}
