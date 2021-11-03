package AST.Acceso;

import AST.Expresion.NodoExpresion;

public class NodoExpParentizada extends NodoPrimario {

    private NodoExpresion expresion_parentizada;

    public NodoExpParentizada(NodoExpresion exp){
        super(exp.getToken());
        this.expresion_parentizada = exp;
    }

    @Override
    public void esta_bien_definido() {
        expresion_parentizada.esta_bien_definido();
    }

    @Override
    public void mostrar_acceso() {
        System.out.println("Nodo ExpParentizada :");
        expresion_parentizada.mostrar_expresion();
    }
}
