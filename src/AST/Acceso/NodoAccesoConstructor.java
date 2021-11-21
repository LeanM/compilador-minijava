package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import Traductor.Traductor;

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

            //Crear el CIR del objeto a retornar por el constructor
            //Se llama a la rutina malloc, por lo tanto reservo un lugar para el resultado de la rutina que me devuelve el CIR
            Traductor.getInstance().gen("RMEM 1");
            //Parametro de malloc (cantidad de variables de instancia de la clase a crear + 1 (por la VT))
            int cant_variables_instancia = TablaSimbolos.getInstance().get_tabla_clases().get(token_acceso.get_lexema()).get_tabla_atributos().size();
            //Parametro
            Traductor.getInstance().gen("PUSH "+ (cant_variables_instancia + 1));
            //La direccion de memoria de la rutina malloc
            Traductor.getInstance().gen("PUSH lmalloc");
            Traductor.getInstance().gen("CALL");
            //Para no perder la referencia al nuevo CIR cuando haga STOREREF para asociarle la VT
            Traductor.getInstance().gen("DUP");
            //Hago push de la etiqueta de la VT de la clase a retornar
            Traductor.getInstance().gen("PUSH "+token_acceso.get_lexema());
            Traductor.getInstance().gen("STOREREF 0");

            //Ahora tenemos que hacer la llamada
            //al constructor pasándole este CIR
            //como el this de su RA

            //Primero duplico la referencia al objeto ya que al finalizar el constructor tiene
            //que quedar como resultado en el tope de la pila
            Traductor.getInstance().gen("DUP");

            //Ahora cargo los parametros bajando el this al agregar uno
            for (int i = 0; i < argumentos_formales.size(); i++) {
                //Esto dejaria el resultado de la expresion en la pila
                argumentos.get(i).generar_codigo();
                //Pongo el comentario del nombre del parametro (No se si no tengo q hacer un .STACK para q aparezca en la pila)
                Traductor.getInstance().gen_comment(argumentos_formales.get(i).getNombre());
                //Hago un swap para ir bajando el this, asi este queda por debajo de los parametros
                Traductor.getInstance().gen("SWAP");
            }
            //Ahora que tenemos los parametros y el this, podemos hacer la llamada al metodo (hay que buscarlo en la VT del this)
            //Por como funciona el LOADREF hay que hacer DUP para no perder la referencia al CIR (this)

            //Como es un constructor, determinamos la direccion del constructor en tiempo de compilacion (no uso la VT)
            Traductor.getInstance().gen("PUSH "+unidad_conformada.get_etiqueta());
            //Hago la llamada
            Traductor.getInstance().gen("CALL");
        }
    }
}
