package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;

import java.io.IOException;

public class NodoExpParentizada extends NodoPrimario_Concreto {

    private NodoExpresion expresion_parentizada;

    public NodoExpParentizada(NodoExpresion exp, String key_clase){
        super(exp.getToken(),key_clase);
        this.expresion_parentizada = exp;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        expresion_parentizada.esta_bien_definido();
    }

    @Override
    public void chequeo_acceso_estatico() throws ExcepcionTipo, ExcepcionSemantica {
        expresion_parentizada.chequeo_acceso_estatico();
    }


    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        return expresion_parentizada.get_tipo_expresion();
    }

    @Override
    public void mostrar_acceso() {
        System.out.println("Nodo ExpParentizada :");
        expresion_parentizada.mostrar_expresion();
    }

    @Override
    public void generar_codigo() throws ExcepcionTipo, ExcepcionSemantica, IOException {
        expresion_parentizada.generar_codigo();
    }
}
