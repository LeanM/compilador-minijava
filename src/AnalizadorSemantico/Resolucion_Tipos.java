package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.Arrays;

public class Resolucion_Tipos {

    private static Resolucion_Tipos instance = null;

    public static Resolucion_Tipos getInstance() {
        if(instance == null)
            instance = new Resolucion_Tipos();

        return instance;
    }

    private Resolucion_Tipos(){

    }

    public Tipo resolver_tipo_binario(Tipo tipoA, Token operador_binario, Tipo tipoB) throws ExcepcionTipo, ExcepcionSemantica {
        Tipo toReturn = tipoA;
        if(Arrays.asList("+","-","/","%","*").contains(operador_binario.get_lexema())){
            if(tipoA.getNombre().equals("int") && tipoB.getNombre().equals("int"))
                toReturn = new TipoPrimitivo(new Token("Entero","int",0));
            else
                throw new ExcepcionTipo("Los operadores +,-,*,/,% solo trabajan con tipos int");
        }
        else
            if(Arrays.asList("&&","||").contains(operador_binario.get_lexema())){
                if(tipoA.getNombre().equals("boolean") && tipoB.getNombre().equals("boolean"))
                    toReturn = new TipoPrimitivo(new Token("pr_boolean","boolean",0));
                else
                    throw new ExcepcionTipo("Los operadores && y || solo trabajan con tipos boolean");
            }
            else
                if(Arrays.asList("<",">","<=",">=").contains(operador_binario.get_lexema())){
                    if((tipoA.getNombre().equals("int")) && (tipoB.getNombre().equals("int")))
                        toReturn = new TipoPrimitivo(new Token("pr_boolean","boolean",0));
                    else
                        throw new ExcepcionTipo("Los operadores <,>,<=,>= solo trabajan con tipos int");
                }
                else
                    if(Arrays.asList("==","!=").contains(operador_binario.get_lexema())){
                        if(tipoA.es_de_tipo(tipoB))
                            toReturn = tipoA;
                        else
                            if(tipoB.es_de_tipo(tipoB))
                                toReturn = tipoB;
                            else
                                throw new ExcepcionTipo("Los operadores == y != solo trabajan con tipos conformantes");
                    }
        return toReturn;
    }

    public Tipo resolver_tipo_unario(Token operador_unario,Tipo tipo) throws ExcepcionTipo {
        if(operador_unario.get_lexema().equals("!")){
            if(!tipo.getNombre().equals("boolean"))
                throw new ExcepcionTipo("El tipo deberia ser boolean");
        }
        else {
            //Caso + o -
            if(!tipo.getNombre().equals("int"))
                throw new ExcepcionTipo("El tipo deberia ser int");
        }

        return tipo;
    }
}
