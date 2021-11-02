package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

import java.util.LinkedList;

public class NodoMetodoEncadenado extends NodoEncadenado{

    private LinkedList<NodoExpresion> argumentos;

    public NodoMetodoEncadenado(Token nombre){
        super(nombre);
        argumentos = new LinkedList<NodoExpresion>();
    }

    public void setArgumentos(LinkedList<NodoExpresion> args){
        this.argumentos = args;
    }

    @Override
    public void esta_bien_definido() {

    }
}
