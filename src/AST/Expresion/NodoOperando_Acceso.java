package AST.Expresion;

import AST.Acceso.NodoAcceso;
import AST.Acceso.NodoAccesoVar;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

import java.io.IOException;

public class NodoOperando_Acceso extends NodoOperando {

    private NodoAcceso nodo_acceso;

    public NodoOperando_Acceso(NodoAcceso acceso){
        super(acceso.getToken());
        this.nodo_acceso = acceso;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        nodo_acceso.esta_bien_definido();
    }

    @Override
    public Tipo get_tipo_expresion() throws ExcepcionTipo, ExcepcionSemantica {
        return nodo_acceso.get_tipo_acceso();
    }

    @Override
    public void chequeo_acceso_estatico() throws ExcepcionTipo, ExcepcionSemantica {
        nodo_acceso.chequeo_acceso_estatico();
    }

    @Override
    public void mostrar_expresion() {
        nodo_acceso.mostrar_acceso();
    }

    @Override
    public void generar_codigo() throws ExcepcionTipo, ExcepcionSemantica, IOException {
        nodo_acceso.generar_codigo();
    }

    public void set_lado_der(){
        nodo_acceso.set_lado_der();
    }
}
