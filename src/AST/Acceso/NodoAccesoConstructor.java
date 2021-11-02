package AST.Acceso;

import AnalizadorLexico.Token;

public class NodoAccesoConstructor extends NodoAccesoUnidad{

    public NodoAccesoConstructor(Token nombre){
        super(nombre);
    }

    @Override
    public void esta_bien_definido() {

    }
}
