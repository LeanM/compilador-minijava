package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;

import java.io.IOException;

public class NodoAsignacion_Standar extends NodoAsignacion{

    private NodoExpresion lado_der;

    public NodoAsignacion_Standar(NodoAcceso lado_izq, Token tipo_asignacion, NodoExpresion lado_der) {
        super(lado_izq, tipo_asignacion);
        this.lado_der = lado_der;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        super.esta_bien_definido();
        lado_der.esta_bien_definido();

        if(en_metodo_static)
            lado_der.chequeo_acceso_estatico();

        if(!lado_izq.puede_ser_asignado())
            throw new ExcepcionTipo(tipo_asignacion,"El lado izquierdo de la asignacion debe ser una variable local, un parametro del metodo o un atributo visible de la clase.");

        if(!lado_der.get_tipo_expresion().es_de_tipo(lado_izq.get_tipo_acceso()))
            throw new ExcepcionTipo(tipo_asignacion,"El lado derecho de la asignacion no conforma al tipo del lado izquierdo");
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Nodo asignacion normal : ");
        lado_izq.mostrar_acceso();
        System.out.println(tipo_asignacion.get_lexema());
        lado_der.mostrar_expresion();
        System.out.println("-----------------------------------------------------------");
    }

    @Override
    public void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica {
        lado_der.set_lado_der();
        lado_der.generar_codigo();
        //Seteo que el lado izq es parte izquierda de asignacion
        lado_izq.set_lado_izq();
        lado_izq.generar_codigo();
    }
}
