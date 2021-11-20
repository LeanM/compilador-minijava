package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import Traductor.*;

import java.io.IOException;

public class NodoFor extends NodoSentencia {

    private NodoVarLocal varLocal;
    private NodoExpresion condicion;
    private NodoAsignacion asignacion;
    private NodoSentencia cuerpo_for;

    public NodoFor(NodoVarLocal var, NodoExpresion condicion, NodoAsignacion asignacion, NodoSentencia cuerpo_for){
        super();
        this.varLocal = var;
        this.condicion = condicion;
        this.asignacion = asignacion;
        this.cuerpo_for = cuerpo_for;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        varLocal.esta_bien_definido();
        condicion.esta_bien_definido();

        if(!condicion.get_tipo_expresion().getNombre().equals("boolean"))
            throw new ExcepcionTipo(condicion.getToken(),"Error de tipo : La expresion condicion en un for debe ser de tipo boolean");

        asignacion.esta_bien_definido();
        cuerpo_for.esta_bien_definido();
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Nodo for");
        varLocal.mostar_sentencia();
        condicion.mostrar_expresion();
        asignacion.mostar_sentencia();
        cuerpo_for.mostar_sentencia();
        System.out.println("-----------------------------------------------------------");
    }

    @Override
    public void generar_codigo() throws ExcepcionTipo, ExcepcionSemantica, IOException {
        int index_etiquetas_for = Index_etiquetas.getInstance().get_index();
        String etiqueta_loop = "for_loop_"+nombre_unidad_declarada+"_"+ index_etiquetas_for;
        String etiqueta_nop = "for_nop_"+nombre_unidad_declarada+"_"+ index_etiquetas_for;
        varLocal.generar_codigo();
        //Genero etiqueta del loop
        Traductor.getInstance().gen_etiqueta(etiqueta_loop);
        condicion.generar_codigo();
        //Si la condicion no se cumple salto afuera del bloque del for
        Traductor.getInstance().gen("BF "+etiqueta_nop);
        asignacion.generar_codigo();

        //Le seteo el mismo nombre de metodo
        cuerpo_for.set_nombre_unidad_declarada(nombre_unidad_declarada);
        cuerpo_for.generar_codigo();
        //Vuelvo a loopear
        Traductor.getInstance().gen("JUMP "+etiqueta_loop);
        //Genero etiqueta de salida del loop
        Traductor.getInstance().gen_etiqueta(etiqueta_nop);
    }
}
