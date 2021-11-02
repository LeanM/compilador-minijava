package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

import java.util.LinkedList;

public class NodoMetodoEncadenado extends NodoEncadenado{

    private LinkedList<NodoExpresion> argumentos;

    public NodoMetodoEncadenado(Token nombre,LinkedList<NodoExpresion> args){
        super(nombre);
        argumentos = args;
    }

    public void setArgumentos(LinkedList<NodoExpresion> args){
        this.argumentos = args;
    }

    @Override
    public void esta_bien_definido() {

    }
}
