package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import Traductor.Traductor;

import java.io.IOException;
import java.util.LinkedList;

public class NodoAccesoMetodo extends NodoAccesoUnidad{

    public NodoAccesoMetodo(Token nombre, LinkedList<NodoExpresion> args, String key_clase){
        super(nombre,args,key_clase);
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        if(!TablaSimbolos.getInstance().get_tabla_clases().get(key_clase).get_tabla_metodos().containsKey(token_acceso.get_lexema()))
            throw new ExcepcionSemantica(token_acceso,"El metodo llamado no es visible en el contexto de la clase "+key_clase);

        unidad_conformada = TablaSimbolos.getInstance().conforma_metodo(token_acceso,argumentos,key_clase);
        if (unidad_conformada == null)
            throw new ExcepcionTipo(token_acceso,"La llamada a metodo no conforma con ningun metodos de la clase.");

    }

    @Override
    public Tipo get_tipo_acceso() throws ExcepcionTipo, ExcepcionSemantica {
        if (unidad_conformada == null)
            throw new ExcepcionTipo(token_acceso,"La llamada a metodo no conforma con ningun metodos de la clase.");
        else return unidad_conformada.get_tipo();
    }

    @Override
    public void chequeo_acceso_estatico() throws ExcepcionSemantica, ExcepcionTipo {
        EntradaMetodo metodo_en_clase = TablaSimbolos.getInstance().conforma_metodo(token_acceso,argumentos,key_clase);
        if (metodo_en_clase == null)
            throw new ExcepcionTipo(token_acceso,"La llamada a metodo no conforma con ningun metodos de la clase.");
        else
            if(!metodo_en_clase.es_estatico())
                throw new ExcepcionSemantica(token_acceso,"No se puede acceder al metodo dinamico "+metodo_en_clase.getNombre()+" desde un contexto estatico");
    }

    @Override
    public void generar_codigo() throws ExcepcionTipo, ExcepcionSemantica, IOException {
        //Generar codigo parametros
        if(unidad_conformada != null) {
            LinkedList<EntradaParametro> argumentos_formales = unidad_conformada.get_lista_argumentos();

            //Cargo el CIR de el RA actual, por que va a ser el mismo del nuevo RA para el metodo
            //Si el metodo es estatico no debo cargarlo
            if(!((EntradaMetodo) unidad_conformada).es_estatico())
                Traductor.getInstance().gen("LOAD 3");
            if(!unidad_conformada.no_retorna()) {
                //Si la unidad retorna un valor
                Traductor.getInstance().gen("RMEM 1");
                if(!((EntradaMetodo) unidad_conformada).es_estatico())
                    //Hago un swap para ir bajando el this
                    Traductor.getInstance().gen("SWAP");
            }

            for (int i = 0; i < argumentos.size(); i++) {
                //Esto dejaria el resultado de la expresion en la pila
                argumentos.get(i).generar_codigo();
                //Pongo el comentario del nombre del parametro (No se si no tengo q hacer un .STACK para q aparezca en la pila)
                Traductor.getInstance().gen_comment(argumentos_formales.get(i).getNombre());
                if(!((EntradaMetodo) unidad_conformada).es_estatico())
                    //Hago un swap para ir bajando el this, asi este queda por debajo de los parametros
                    Traductor.getInstance().gen("SWAP");
            }

            if(!((EntradaMetodo) unidad_conformada).es_estatico()) {
                //Ahora que tenemos los parametros y el this, podemos hacer la llamada al metodo (hay que buscarlo en la VT del this)
                //Por como funciona el LOADREF hay que hacer DUP para no perder la referencia al CIR (this)
                Traductor.getInstance().gen("DUP");
                //Hago el LOADREF para obtener la VT en el CIR (el offset de la VT es siempre 0)
                Traductor.getInstance().gen("LOADREF 0");
                //Obtengo de la VT el label del metodo conformado con el offset de ese metodo
                Traductor.getInstance().gen("LOADREF " + unidad_conformada.get_offset());
            }
            else
                //Si es estatico, hago push de la etiqueta del metodo
                Traductor.getInstance().gen("PUSH "+unidad_conformada.get_etiqueta());

            //Hago la llamada
            Traductor.getInstance().gen("CALL");
        }
    }
}
