package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

import java.util.LinkedList;

public class NodoAccesoConstructor extends NodoAccesoUnidad{

    public NodoAccesoConstructor(Token nombre, LinkedList<NodoExpresion> args){
        super(nombre,args);
    }

    @Override
    public void esta_bien_definido() {

    }
}
