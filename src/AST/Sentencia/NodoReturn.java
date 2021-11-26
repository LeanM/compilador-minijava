package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AST.NodoBloque;
import AnalizadorLexico.Token;
import AnalizadorSemantico.EntradaMetodo;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.ExcepcionTipo;
import AnalizadorSemantico.Tipo;
import Traductor.Traductor;

import java.io.IOException;

public class NodoReturn extends NodoSentencia {

    protected Token token_return;
    protected NodoBloque nodo_bloque;

    public NodoReturn(Token token_return, NodoBloque bloque){
        super();
        this.token_return = token_return;
        this.nodo_bloque = bloque;
    }

    @Override
    public void esta_bien_definido() throws ExcepcionTipo, ExcepcionSemantica {
        if(!nodo_bloque.get_unidad_bloque().get_tipo().getNombre().equals("void"))
            throw new ExcepcionTipo(token_return,"El metodo no es de tipo void");
    }

    @Override
    public void mostar_sentencia() {
        System.out.println("Nodo return : ");
    }

    @Override
    public void generar_codigo() throws ExcepcionTipo, ExcepcionSemantica, IOException {
        //Cant argumentos
        int n = nodo_bloque.get_unidad_bloque().get_lista_argumentos().size();
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
