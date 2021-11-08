package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;

import java.util.LinkedList;

public abstract class NodoAccesoUnidad extends NodoPrimario_Concreto {

    protected LinkedList<NodoExpresion> argumentos;

    public NodoAccesoUnidad(Token nombre, LinkedList<NodoExpresion> args, String key_clase){
        super(nombre,key_clase);
        argumentos = args;
    }
    public void setArgumentos(LinkedList<NodoExpresion> argumentos){
        this.argumentos = argumentos;
    }
}
