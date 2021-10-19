package AnalizadorLexico;

public class ExcepcionLexica extends Exception{

    private String lexema_error, linea_actual;
    private int nro_linea_error, nro_char_actual;

    public ExcepcionLexica(String tipo_error, String lexema, int nro_linea, String linea_actual, int nro_char_actual){
        super("Error Lexico en linea "+nro_linea+" , columna "+ nro_char_actual +" : "+ lexema +" -> "+ tipo_error);
        lexema_error = lexema;
        nro_linea_error = nro_linea;
        this.linea_actual = linea_actual;
        this.nro_char_actual = nro_char_actual;
    }

    public void printStackTrace(){
        System.out.println(this.getMessage());
        this.linea_actual = this.linea_actual.replace((char)10,' ');
        this.linea_actual = this.linea_actual.replace((char)9,' ');
        System.out.println("Detalle : " + this.linea_actual);
        System.out.print("          ");
        for (int i = 0; i < nro_char_actual - 1;i++){ //nro_char_actual va a ser >= 1 siempre por que ya se leyo un char de la linea, por lo tanto se incremento el nro_char_actual
            System.out.print(" ");
        }
        System.out.println("^");
        System.out.println("[Error:"+lexema_error+"|"+nro_linea_error+"]");
    }

}
