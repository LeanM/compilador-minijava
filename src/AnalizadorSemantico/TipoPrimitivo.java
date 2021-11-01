package AnalizadorSemantico;
import AnalizadorLexico.Token;

import java.util.LinkedList;

public class TipoPrimitivo extends Tipo {

    public TipoPrimitivo(Token token_tipo) {
        super(token_tipo);
    }

    public boolean esPrimitivo(){
        return true;
    }

    @Override
    public boolean es_de_tipo(Tipo tipo) {
        return this.mismo_tipo_exacto(tipo);
    }
}
