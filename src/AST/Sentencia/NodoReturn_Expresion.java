package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AST.NodoBloque;
import AnalizadorLexico.Token;
import AnalizadorSemantico.EntradaMetodo;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import Traductor.Traductor;

import java.io.IOException;

public class NodoReturn_Expresion extends NodoReturn{

    private NodoExpresion expresion;

    public NodoReturn_Expresion(Token token_return, NodoExpresion expresion, NodoBloque bloque) {
        super(token_return,bloque);
        this.expresion = expresion;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        expresion.esta_bien_definido();
        if(nodo_bloque.get_unidad_bloque().get_tipo().getNombre().equals("void") || !expresion.get_tipo_expresion().es_de_tipo(nodo_bloque.get_unidad_bloque().get_tipo()))
            throw new ExcepcionTipo(token_return,"El tipo de la expresion de retorno no conforma el tipo que retorna el metodo, o el metodo es de tipo void");
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("Nodo return : ");
        expresion.mostrar_expresion();
    }

    public void generar_codigo() throws ExcepcionTipo, ExcepcionSemantica, IOException {
        expresion.generar_codigo();
        //Dejo el resultado para que el metodo al terminar lo tome y lo retorne
        //RETORNO
        int offset;
        int n = nodo_bloque.get_unidad_bloque().get_lista_argumentos().size();
        if (n == 0) {
            if (((EntradaMetodo) nodo_bloque.get_unidad_bloque()).es_estatico())
                offset = 3;
            else
                offset = 4;//4 por que es la pos despues del this
        } else {
            // Le sumo uno por que el espacio de retorno esta arriba del primer parametro
            offset = nodo_bloque.get_unidad_bloque().get_lista_argumentos().get(0).get_offset() + 1;
        }

        //Hago STORE de la expresion del return
        Traductor.getInstance().gen("STORE " + offset);

        //Aca debo hacer que todas las variables locales que se cargaron sean liberadas
        Traductor.getInstance().gen("FMEM "+nodo_bloque.get_cant_var_locales_cargadas_total());

        //Actualizo el fp para que apunte al RA del llamador
        Traductor.getInstance().gen("STOREFP");

        //Si es dinamico debo sumar 1 (como esta ahora)
        //Si es estatico es solo la cant de parametros
        if (((EntradaMetodo) nodo_bloque.get_unidad_bloque()).es_estatico())
            Traductor.getInstance().gen("RET " + n);
        else
            Traductor.getInstance().gen("RET " + (n + 1));
    }
}
