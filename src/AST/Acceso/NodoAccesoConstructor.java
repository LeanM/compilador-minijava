package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.LinkedList;

public class NodoAccesoConstructor extends NodoAccesoUnidad{

    public NodoAccesoConstructor(Token nombre, LinkedList<NodoExpresion> args, String key_clase){
        super(nombre,args,key_clase);
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        //Verificar semanticas de acceso constructor
        EntradaConstructor entradaConstructor = TablaSimbolos.getInstance().conforma_constructor(token_acceso,argumentos,key_clase);
        if(entradaConstructor == null)
            throw new ExcepcionTipo(token_acceso,"No hay conformidad de tipos con ninguno de los constructores definidos en la clase.");
        
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        EntradaConstructor ec = TablaSimbolos.getInstance().conforma_constructor(token_acceso,argumentos,key_clase);
        if(ec == null)
            throw new ExcepcionTipo(token_acceso,"No hay conformidad de tipos con ninguno de los constructores definidos en la clase.");
        else
            return ec.get_tipo();
    }

    @Override
    public void chequeo_acceso_estatico() throws ExcepcionSemantica, ExcepcionTipo {
        //nada
    }
}
