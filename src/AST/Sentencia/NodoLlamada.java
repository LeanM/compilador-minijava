package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AST.Acceso.NodoAccesoThis;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import Traductor.Traductor;

import java.io.IOException;

public class NodoLlamada extends NodoSentencia{

    private NodoAcceso nodo_acceso;

    public NodoLlamada(NodoAcceso nodo) {
        super();
        this.nodo_acceso = nodo;
    }

    public NodoAcceso get_nodo_acceso(){
        return nodo_acceso;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        nodo_acceso.esta_bien_definido();
        if(nodo_acceso instanceof NodoAccesoThis){
            if (((NodoAccesoThis) nodo_acceso).es_ultimo_encadenado())
                throw new ExcepcionSemantica(nodo_acceso.getToken(),"No es valido llamar al this sin concatenar un metodo o un atributo");
        }

        if(nodo_acceso.puede_ser_asignado())
            throw new ExcepcionTipo(nodo_acceso.getToken(),"No es una sentencia correcta, un acceso sin asignacion no puede ser una variable.");

        if(en_metodo_static)
            nodo_acceso.chequeo_acceso_estatico();
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("-----------------------------------------------------------");
        System.out.println("Nodo llamada : ");
        nodo_acceso.mostrar_acceso();
        System.out.println("-----------------------------------------------------------");
    }

    @Override
    public void generar_codigo() throws ExcepcionTipo, ExcepcionSemantica, IOException {
        nodo_acceso.generar_codigo();
        if(!nodo_acceso.get_tipo_acceso().getNombre().equals("void"))
            Traductor.getInstance().gen("POP");
    }
}
