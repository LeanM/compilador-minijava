package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

import java.util.LinkedList;

public abstract class NodoAccesoUnidad extends NodoPrimario {

    protected LinkedList<NodoExpresion> argumentos;

    public NodoAccesoUnidad(Token nombre, LinkedList<NodoExpresion> args){
        super(nombre);
        argumentos = args;
    }
    public void setArgumentos(LinkedList<NodoExpresion> argumentos){
        this.argumentos = argumentos;
    }
}
