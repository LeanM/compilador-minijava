package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AST.NodoBloque;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import Traductor.Traductor;

import java.io.IOException;

public class NodoVarLocal_Asignacion extends NodoVarLocal{

    private NodoExpresion expresion_de_asignacion;

    public NodoVarLocal_Asignacion(Tipo tipo, Token variable, EntradaUnidad unidad, NodoBloque bloque, NodoExpresion exp) {
        super(tipo, variable, unidad, bloque);
        this.expresion_de_asignacion = exp;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        super.esta_bien_definido();
        expresion_de_asignacion.esta_bien_definido();

        if(en_metodo_static)
            expresion_de_asignacion.chequeo_acceso_estatico();

        if(!expresion_de_asignacion.get_tipo_expresion().es_de_tipo(tipo))
            throw new ExcepcionTipo(expresion_de_asignacion.getToken(),"El lado derecho de la asignacion no conforma al tipo del lado izquierdo");

    }

    @Override
    public void mostar_sentencia() {
        System.out.println("varLocal asignacion : "+var_local.get_lexema());
        expresion_de_asignacion.mostrar_expresion();
    }

    @Override
    public void generar_codigo() throws ExcepcionTipo, ExcepcionSemantica, IOException {
        expresion_de_asignacion.generar_codigo();
        Traductor.getInstance().gen("STORE "+this.get_offset());
    }
}
