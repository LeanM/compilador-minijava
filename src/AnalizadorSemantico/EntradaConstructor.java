package AnalizadorSemantico;

import AnalizadorLexico.Token;
import Traductor.*;

import java.io.IOException;

public class EntradaConstructor extends EntradaUnidad {

    public EntradaConstructor(Token token_constructor,Tipo tipo_constructor) {
        super(tipo_constructor,token_constructor);
    }

    public void esta_bien_declarado() throws ExcepcionSemantica {
        for (EntradaParametro ea : lista_argumentos)
            ea.esta_bien_declarado();
    }

    public String get_etiqueta() {
        String toReturn = "lCtor"+this.getNombre();
        for(EntradaParametro ep : lista_argumentos){
            toReturn = toReturn + "_" + ep.get_tipo().getNombre();
        }
        return toReturn;
    }

    public boolean no_retorna() {
        return false;
    }

    @Override
    public int get_offset() {
        return 0;
    }

    public void generar_codigo() throws IOException, ExcepcionTipo, ExcepcionSemantica {
        //Guardo el enlace dinamico al RA llamador
        Traductor.getInstance().gen("LOADFP");
        //Apilo el lugar donde comienza el RA de la unidad llamada (esta)
        Traductor.getInstance().gen("LOADSP");
        //Actualiza el Fp con el valor tope de la pila
        Traductor.getInstance().gen("STOREFP");
        //Una vez el RA esta terminado, puedo generar el codigo de los bloques
        bloque_principal.generar_codigo();
        Index_etiquetas.getInstance().reset_index();

        //RETORNO - Como es constructor no tengo que hacer store del resultado, el resultado es el objeto creado al llamarlo
        int n = lista_argumentos.size();
        Traductor.getInstance().gen("STOREFP");
        Traductor.getInstance().gen("RET "+ (n + 1));
    }
}
