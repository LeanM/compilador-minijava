package AST.Expresion;

import AST.Acceso.NodoAcceso;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

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
    public void mostrar_expresion() {
        nodo_acceso.mostrar_acceso();
    }
}
