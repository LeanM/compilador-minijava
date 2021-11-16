package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AST.Acceso.NodoAccesoVar;
import AST.Acceso.NodoVarEncadenada_Decorator;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.io.IOException;

public class NodoAsignacion_Dec extends NodoAsignacion{

    public NodoAsignacion_Dec(NodoAcceso lado_izq, Token tipo_asignacion){
        super(lado_izq,tipo_asignacion);
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("NodoAsignacion_Dec : ");
        lado_izq.mostrar_acceso();
        System.out.println(tipo_asignacion.get_lexema());
        System.out.println("-----------------------------------------------------------");
    }

    @Override
    public void generar_codigo() throws IOException, ExcepcionSemantica, ExcepcionTipo {
        if (lado_izq instanceof NodoAccesoVar){
            //Para el momento en el que se necesite el valor
            lado_izq.generar_codigo();

            Traductor.getInstance().gen("PUSH 1");
            Traductor.getInstance().gen("SUB");

            //Para el momento en el que se asigna el nuevo valor decrementado
            ((NodoAccesoVar) lado_izq).set_es_lado_izq();
            lado_izq.generar_codigo();
        }
        else {
            //Si no es un NodoAccesoVar si o si es una var encadenada, sino no hubiese pasado el chequeo
            ((NodoVarEncadenada_Decorator) lado_izq).set_es_lado_izq();
            lado_izq.generar_codigo();
            Traductor.getInstance().gen("PUSH 1");
            Traductor.getInstance().gen("SUB");
            ((NodoAccesoVar) lado_izq).get_acceso_tipo_var();
        }



    }

    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        lado_izq.esta_bien_definido();
        if(!lado_izq.get_tipo_acceso().getNombre().equals("int"))
            throw new ExcepcionTipo(tipo_asignacion,"Error de tipo : La asignacion -- solo puede aplicarse a tipos int");
    }
}
