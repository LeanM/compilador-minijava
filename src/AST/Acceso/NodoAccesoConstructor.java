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
        boolean iguales = false;
        LinkedList<EntradaConstructor> lista_constructores = TablaSimbolos.getInstance().get_entrada_clase(key_clase).get_lista_constructores();
        LinkedList<EntradaParametro> args_constructor;
        for(EntradaConstructor ec : lista_constructores){
            args_constructor = ec.get_lista_argumentos();
            if(args_constructor.size() == this.argumentos.size()) {
                iguales = true;
                for (int i = 0; i < this.argumentos.size() && iguales; i++) {
                    if(!this.argumentos.get(i).get_tipo_expresion().es_de_tipo(args_constructor.get(i).get_tipo()))
                        iguales = false;
                }
                if(iguales)
                    break;
            }
        }

        if(!iguales)
            throw new ExcepcionTipo("La llamda a constructor no es correctamente tipada para ninguno de los constructores de la clase.");
    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        return TablaSimbolos.getInstance().get_entrada_clase(key_clase).get_lista_constructores().getFirst().get_tipo();
    }
}
