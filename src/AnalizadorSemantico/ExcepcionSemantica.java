package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class ExcepcionSemantica extends Exception {

    private Token token_en_error;

    public ExcepcionSemantica(Token token_en_error, String e){
        super(e);
        this.token_en_error = token_en_error;
    }

    @Override
    public void printStackTrace() {
        System.out.println(this.getMessage());
        System.out.println("[Error:"+token_en_error.get_lexema()+"|"+token_en_error.get_nro_linea()+"]");
    }
}
