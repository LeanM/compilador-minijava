package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

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

            for (int i = 0; i < argumentos_formales.size(); i++) {
                //Esto dejaria el resultado de la expresion en la pila
                argumentos.get(i).generar_codigo();
            }
            unidad_conformada.generar_codigo();
        }
    }
}
