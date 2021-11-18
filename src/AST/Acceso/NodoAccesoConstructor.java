package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.io.IOException;
import java.util.LinkedList;

public class NodoAccesoConstructor extends NodoAccesoUnidad{

    public NodoAccesoConstructor(Token nombre, LinkedList<NodoExpresion> args, String key_clase){
        super(nombre,args,key_clase);
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        //Verificar semanticas de acceso constructor
        unidad_conformada = TablaSimbolos.getInstance().conforma_constructor(token_acceso,argumentos,token_acceso.get_lexema());
        if(unidad_conformada == null)
            throw new ExcepcionTipo(token_acceso,"No hay conformidad de tipos con ninguno de los constructores definidos en la clase.");
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        if(unidad_conformada == null)
            throw new ExcepcionTipo(token_acceso,"No hay conformidad de tipos con ninguno de los constructores definidos en la clase.");
        else
            return unidad_conformada.get_tipo();
    }

    @Override
    public void chequeo_acceso_estatico() throws ExcepcionSemantica, ExcepcionTipo {
        //nada
    }

    @Override
    public void generar_codigo() throws ExcepcionTipo, ExcepcionSemantica, IOException {
        //Generar codigo parametros
        if(unidad_conformada != null) {
            LinkedList<EntradaParametro> argumentos_formales = unidad_conformada.get_lista_argumentos();
            for (int i = 0; i < argumentos_formales.size(); i++) {
                //Esto dejaria el resultado de la expresion en la pila
                argumentos.get(i).generar_codigo();
            }
            unidad_conformada.generar_codigo();
        }
    }
}
