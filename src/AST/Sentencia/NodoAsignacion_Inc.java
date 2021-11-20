package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import Traductor.Traductor;

import java.io.IOException;

public class NodoAsignacion_Inc extends NodoAsignacion{

    public NodoAsignacion_Inc(NodoAcceso lado_izq, Token tipo_asignacion){
        super(lado_izq,tipo_asignacion);
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("NodoAsignacion_Inc : ");
        lado_izq.mostrar_acceso();
        System.out.println(tipo_asignacion.get_lexema());
        System.out.println("-----------------------------------------------------------");
    }

    @Override
    public void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica {
        //Primero trabajo el acceso como la parte derecha de la asignacion
        lado_izq.generar_codigo();
        Traductor.getInstance().gen("PUSH 1");
        Traductor.getInstance().gen("ADD");
        //Luego trabajo el acceso como la parte izquierda de la asignacion
        lado_izq.set_lado_izq();
        lado_izq.generar_codigo();
    }

    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        super.esta_bien_definido();
        if(!lado_izq.get_tipo_acceso().getNombre().equals("int"))
            throw new ExcepcionTipo(tipo_asignacion,"Error de tipo : La asignacion ++ solo puede aplicarse a tipos int");
    }
}
