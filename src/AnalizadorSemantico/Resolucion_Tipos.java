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
                throw new ExcepcionTipo(operador_binario,"Los operadores +,-,*,/,% solo trabajan con tipos int");
        }
        else
            if(Arrays.asList("&&","||").contains(operador_binario.get_lexema())){
                if(tipoA.getNombre().equals("boolean") && tipoB.getNombre().equals("boolean"))
                    toReturn = new TipoPrimitivo(new Token("pr_boolean","boolean",0));
                else
                    throw new ExcepcionTipo(operador_binario,"Los operadores && y || solo trabajan con tipos boolean");
            }
            else
                if(Arrays.asList("<",">","<=",">=").contains(operador_binario.get_lexema())){
                    if((tipoA.getNombre().equals("int")) && (tipoB.getNombre().equals("int")))
                        toReturn = new TipoPrimitivo(new Token("pr_boolean","boolean",0));
                    else
                        throw new ExcepcionTipo(operador_binario,"Los operadores <,>,<=,>= solo trabajan con tipos int");
                }
                else
                    if(Arrays.asList("==","!=").contains(operador_binario.get_lexema())){
                        if(tipoA.es_de_tipo(tipoB) || tipoB.es_de_tipo(tipoA))
                            toReturn = new TipoPrimitivo(new Token("pr_boolean","boolean",0));
                        else
                            throw new ExcepcionTipo(operador_binario,"Los operadores == y != solo trabajan con tipos conformantes");
                    }
        return toReturn;
    }

    public Tipo resolver_tipo_unario(Token operador_unario,Tipo tipo) throws ExcepcionTipo {
        if(operador_unario.get_lexema().equals("!")){
            if(!tipo.getNombre().equals("boolean"))
                throw new ExcepcionTipo(operador_unario,"El operador unario "+operador_unario.get_lexema()+" solo puede recibir tipo boolean");
        }
        else {
            //Caso + o -
            if(!tipo.getNombre().equals("int"))
                throw new ExcepcionTipo(operador_unario,"El operador unario "+operador_unario.get_lexema()+" solo puede recibir tipo int");
        }

        return tipo;
    }
}
