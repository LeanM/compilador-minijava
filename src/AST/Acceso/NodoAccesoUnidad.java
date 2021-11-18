package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.EntradaUnidad;

import java.util.LinkedList;

public abstract class NodoAccesoUnidad extends NodoPrimario_Concreto {

    protected LinkedList<NodoExpresion> argumentos;
    protected EntradaUnidad unidad_conformada;

    public NodoAccesoUnidad(Token nombre, LinkedList<NodoExpresion> args, String key_clase){
        super(nombre,key_clase);
        argumentos = args;
        unidad_conformada = null;
    }
    public void setArgumentos(LinkedList<NodoExpresion> argumentos){
        this.argumentos = argumentos;
    }
}
