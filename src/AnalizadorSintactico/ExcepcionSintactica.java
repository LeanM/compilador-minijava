package AnalizadorSintactico;

import AnalizadorLexico.Token;

public class ExcepcionSintactica extends Exception {

    private Token tokenActual_enError;
    private String token_esperado;

    public ExcepcionSintactica(Token tokenActual, String token_esperado) {
        this.tokenActual_enError = tokenActual;
        this.token_esperado = token_esperado;
    }

    public void printStackTrace(){
        System.out.println("Error Sintactico en linea "+tokenActual_enError.get_nro_linea()+" : Se esperaba "+token_esperado+"  y se encontro "+tokenActual_enError.get_lexema());
        System.out.println("[Error:"+tokenActual_enError.get_lexema()+"|"+tokenActual_enError.get_nro_linea()+"]");
    }
}
