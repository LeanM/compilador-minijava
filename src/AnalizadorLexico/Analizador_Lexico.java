package AnalizadorLexico;

import java.io.IOException;
import java.util.HashMap;

public class Analizador_Lexico {

    private String lexema;
    private char caracter_actual;
    private GestorArchivo GA;
    private HashMap<String,Token> tabla_reservadas;
    private boolean huboErrores;

    public Analizador_Lexico(GestorArchivo GA) throws IOException {
        huboErrores = false;
        lexema = "";
        this.GA = GA;
        caracter_actual = GA.getNextChar();
        tabla_reservadas = new HashMap<String,Token>();
        llenar_tabla();
    }

    public Token proximo_token() {
        try{
            lexema= "";
            return e0();
        }
        catch (ExcepcionLexica | IOException e) {
            e.printStackTrace();
            huboErrores = true;
            return proximo_token();
        }
    }

    /*
     *  Estado Inicial
     */
    private Token e0() throws ExcepcionLexica, IOException {

        if (GA.esEOF()){
            return e41();
        }

        if (es_Mayuscula(caracter_actual)){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e1();
        }

        if (es_Minuscula(caracter_actual)){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e2();
        }

        if (es_Digito(caracter_actual)){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e3();
        }

        if (caracter_actual == '\''){
            //Preguntar si debo incluir las comillas en el lexema o no
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e4();
        }

        if (caracter_actual == '"'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e9();
        }

        if (caracter_actual == '/'){
            lexema = lexema + caracter_actual; //En el proximo estado si no termina ahi, es un comentario, y debo borrar el lexema.
            caracter_actual = GA.getNextChar();
            return e12();
        }

        if (caracter_actual == '('){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e16();
        }

        if (caracter_actual == ')'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e17();
        }

        if (caracter_actual == '{'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e18();
        }

        if (caracter_actual == '}'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e19();
        }

        if (caracter_actual == ';'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e20();
        }

        if (caracter_actual == '.'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e21();
        }

        if (caracter_actual == ','){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e22();
        }

        if (caracter_actual == '+'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e23();
        }

        if (caracter_actual == '-'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e25();
        }

        if (caracter_actual == '*'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e27();
        }

        if (caracter_actual == '%'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e28();
        }

        if (caracter_actual == '>'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e29();
        }

        if (caracter_actual == '<'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e31();
        }

        if (caracter_actual == '!'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e33();
        }

        if (caracter_actual == '|'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e35();
        }

        if (caracter_actual == '&'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e37();
        }

        if (caracter_actual == '='){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e39();
        }


        if (es_espacioBlanco(caracter_actual)){
            caracter_actual = GA.getNextChar();
            return e0();
        }
        else {
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            throw new ExcepcionLexica("No es un simbolo valido",lexema,GA.getNro_linea(), GA.get_Linea(), GA.get_nro_char_actual()-1);
        }
    }

    /*
     *  Estado para Token identificador de clase (idClase)
     *  Estado Terminador
     */
    private Token e1() throws ExcepcionLexica, IOException {
        if (es_Digito(caracter_actual) || es_Letra(caracter_actual) || caracter_actual == '_'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e1();
        }
        else {
            if(tabla_reservadas.containsKey(lexema)){
                Token token, aCopiar;
                aCopiar = tabla_reservadas.get(lexema);
                token = new Token(aCopiar.get_id_token(),aCopiar.get_lexema(),GA.getNro_linea());
                return token;
            }
            else
                return new Token("idClase", lexema, GA.getNro_linea());
        }
    }

    /*
     *   Estado para Token identificador de Metodo o variable (idMetVar)
     *   Estado Terminador
     */
    private Token e2() throws ExcepcionLexica, IOException {
        if (es_Digito(caracter_actual) || es_Letra(caracter_actual) || caracter_actual == '_'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e2();
        }
        else {
            if(tabla_reservadas.containsKey(lexema)){
                Token token, aCopiar;
                aCopiar = tabla_reservadas.get(lexema);
                token = new Token(aCopiar.get_id_token(),aCopiar.get_lexema(),GA.getNro_linea());
                return token;
            }
            else
                return new Token("idMetVar", lexema, GA.getNro_linea());
        }
    }

    /*
     * Estado para Token Entero.
     * Estado Terminador
     */
    private Token e3() throws ExcepcionLexica, IOException {
        if (es_Digito(caracter_actual)){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e3();
        }
        else {
            if (lexema.length() <= 9)
                return new Token("Entero", lexema, GA.getNro_linea());
            else
                throw new ExcepcionLexica("El entero tiene mas de 9 digitos." , lexema, GA.getNro_linea(), GA.get_Linea(), GA.get_nro_char_actual()-1);
        }
    }

    /*
     *  Estados para Token Char
     */
    private Token e4() throws ExcepcionLexica,IOException {
        if ((caracter_actual != '\\') && (caracter_actual != '\'') && (!GA.esEOF())){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e7();
        }
        else
            if(caracter_actual == '\\'){
                lexema = lexema + caracter_actual;
                caracter_actual = GA.getNextChar();
                return e5();
            }
            else {
                String tipo_error;
                if (caracter_actual=='\'') {
                    tipo_error = "El char esta vacio.";
                    lexema = lexema + caracter_actual;
                    caracter_actual = GA.getNextChar();
                }
                else
                    tipo_error = "Char sin cerrar, se llego a fin de archivo.";
                throw new ExcepcionLexica(tipo_error , lexema, GA.getNro_linea(), GA.get_Linea(), GA.get_nro_char_actual()-1);
            }
    }

    private Token e5() throws ExcepcionLexica, IOException {
        if(!GA.esEOF()) {
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e6();
        }
        else throw new ExcepcionLexica("Char sin cerrar, se llego a fin de archivo." , lexema, GA.getNro_linea(), GA.get_Linea(), GA.get_nro_char_actual());
    }

    private Token e6() throws ExcepcionLexica, IOException {
        if (caracter_actual == '\'') {
            lexema = lexema + caracter_actual; //No se si el lexema de los char tienen que incluir las comillas simples
            caracter_actual = GA.getNextChar();
            return e8();
        }
        else {
            throw new ExcepcionLexica("Char no cerrado, deberia haber una comilla simple." , lexema, GA.getNro_linea(), GA.get_Linea(), GA.get_nro_char_actual());
        }
    }

    private Token e7() throws ExcepcionLexica, IOException {
        if (caracter_actual == '\'') {
            lexema = lexema + caracter_actual; //No se si el lexema de los char tienen que incluir las comillas simples
            caracter_actual = GA.getNextChar();
            return e8();
        }
        else {
            throw new ExcepcionLexica("Char no cerrado, deberia haber una comilla simple." , lexema, GA.getNro_linea(), GA.get_Linea(), GA.get_nro_char_actual());
        }
    }
    //  Estado terminador
    private Token e8() throws ExcepcionLexica, IOException {
        return new Token("Char", lexema, GA.getNro_linea());
    }

    /*
     *  Estados para Token String.
     */
    private Token e9() throws ExcepcionLexica, IOException {
        if (caracter_actual == '"') {
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e11();
        }
        else
            if (caracter_actual == '\\'){
                lexema = lexema + caracter_actual;
                caracter_actual = GA.getNextChar();
                return e10();
            }
            else
                if (((int) caracter_actual != 10) && (!GA.esEOF())) {
                    lexema = lexema + caracter_actual;
                    caracter_actual = GA.getNextChar();
                    return e9();
                }
                else {
                    throw new ExcepcionLexica("String no cerrado, deberia haber comillas dobles." , lexema, GA.getNro_linea(), GA.get_Linea(), GA.get_nro_char_actual()-1);
                }
    }

    private Token e10() throws ExcepcionLexica, IOException {
        if (((int) caracter_actual != 10) && (!GA.esEOF()) && ( (caracter_actual == '"') || (caracter_actual == 'n') )){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e9();
        }
        else {
            String tipo_error;
            if (caracter_actual != '"')
                tipo_error = "Invalido caracter despues de '\\', se espera comillas dobles.";
            else tipo_error = "String no cerrado, deberia haber comillas dobles.";
            throw new ExcepcionLexica(tipo_error , lexema,GA.getNro_linea(), GA.get_Linea(), GA.get_nro_char_actual()-1);
        }
    }
    //  Estado terminador
    private Token e11() throws ExcepcionLexica, IOException {
        return new Token("String", lexema, GA.getNro_linea());
    }

    /*
     *  Estados para Token Division
     *  O para ignorar comentarios simples y multilinea
     */
    private Token e12() throws ExcepcionLexica, IOException {
        if (caracter_actual == '/'){
            // Es un comentario simple
            lexema = "";
            caracter_actual = GA.getNextChar();
            return e13();
        }
        else
            if (caracter_actual == '*'){
                // Es un comentario multilinea
                lexema = "";
                caracter_actual = GA.getNextChar();
                return e14();
            }
            else {
                // Es el operador division
                return new Token("op/",lexema, GA.getNro_linea());
            }

    }

    private Token e13() throws ExcepcionLexica, IOException {
        if (((int) caracter_actual == 10) || GA.esEOF())
            return e0();
        else {
            caracter_actual = GA.getNextChar();
            return e13();
        }

    }

    private Token e14() throws ExcepcionLexica, IOException {
        if (caracter_actual == '*') {
            caracter_actual = GA.getNextChar();
            return e15();
        } else {
            if (!GA.esEOF()){
                caracter_actual = GA.getNextChar();
                return e14();
            }
            else throw new ExcepcionLexica("Comentario multilinea no cerrado. Se llego a final del archivo." , lexema,GA.getNro_linea(),GA.get_Linea(),GA.get_nro_char_actual() - 1);
        }
    }

    private Token e15() throws ExcepcionLexica, IOException {
        if (caracter_actual == '/') {
            caracter_actual = GA.getNextChar();
            return e0();
        } else
            if (!GA.esEOF()){
                caracter_actual = GA.getNextChar();
                return e14();
            }
            else throw new ExcepcionLexica("Comentario multilinea no cerrado. Se llego a final del archivo." , lexema, GA.getNro_linea(), GA.get_Linea(), GA.get_nro_char_actual() - 1);
    }

    /*
     *  Estados para Token parentesis (parC)
     */
    private Token e16() throws ExcepcionLexica, IOException {
        return new Token("parAbre", lexema, GA.getNro_linea());
    }

    private Token e17() throws ExcepcionLexica, IOException {
        return new Token("parCierra", lexema, GA.getNro_linea());
    }

    /*
     *  Estados para Token llaves (llaveC)
     */
    private Token e18() throws ExcepcionLexica, IOException {
        return new Token("llaveAbre", lexema, GA.getNro_linea());
    }

    private Token e19() throws ExcepcionLexica, IOException {
        return new Token("llaveCierra", lexema, GA.getNro_linea());
    }

    /*
     *  Estado para Token puntuacion ; (pun;)
     */
    private Token e20() throws ExcepcionLexica, IOException {
        return new Token("pun;", lexema, GA.getNro_linea());
    }

    /*
     *  Estado para Token puntuacion . (pun.)
     */
    private Token e21() throws ExcepcionLexica, IOException {
        return new Token("pun.", lexema, GA.getNro_linea());
    }

    /*
     *  Estado para Token puntiacion , (pun,)
     */
    private Token e22() throws ExcepcionLexica, IOException {
        return new Token("pun,", lexema, GA.getNro_linea());
    }

    /*
     *  Estados para Token operador + (op+)
     *  o para asignacion ++ (asig++)
     */
    private Token e23() throws ExcepcionLexica, IOException {
        if (caracter_actual == '+'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e24();
        }
        else
            return new Token("op+", lexema, GA.getNro_linea());
    }

    private Token e24() throws ExcepcionLexica, IOException {
        return new Token("asig++", lexema, GA.getNro_linea());
    }

    /*
     *  Estados para Token operador - (op-)
     *  o para Token asignacion -- (asig--)
     */
    private Token e25() throws ExcepcionLexica, IOException {
        if (caracter_actual == '-'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e26();
        }
        else
            return new Token("op-", lexema, GA.getNro_linea());
    }

    private Token e26() throws ExcepcionLexica, IOException {
        return new Token("asig--", lexema, GA.getNro_linea());
    }

    /*
     *  Estado para Token operador * (op*)
     */
    private Token e27() throws ExcepcionLexica, IOException {
        return new Token("op*", lexema, GA.getNro_linea());
    }

    /*
     *  Estado para Token operador % (op%)
     */
    private Token e28() throws ExcepcionLexica, IOException {
        return new Token("op%", lexema, GA.getNro_linea());
    }

    /*
     *  Estado para Token operador > (op>)
     *  o operador >= (op>=)
     */
    private Token e29() throws ExcepcionLexica, IOException {
        if (caracter_actual == '='){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e30();
        }
        else {
            return new Token("op>", lexema, GA.getNro_linea());
        }
    }

    private Token e30() throws ExcepcionLexica, IOException {
        return new Token("op>=", lexema, GA.getNro_linea());
    }

    /*
     *  Estado para Token operador < (op<)
     *  o operador <= (op<=)
     */
    private Token e31() throws ExcepcionLexica, IOException {
        if (caracter_actual == '='){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e32();
        }
        else {
            return new Token("op<", lexema, GA.getNro_linea());
        }
    }

    private Token e32() throws ExcepcionLexica, IOException {
        return new Token("op<=", lexema, GA.getNro_linea());
    }

    /*
     *  Estado para Token operador ! (op!)
     *  o operador != (op!=)
     */
    private Token e33() throws ExcepcionLexica, IOException {
        if (caracter_actual == '='){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e34();
        }
        else {
            return new Token("op!", lexema, GA.getNro_linea());
        }
    }

    private Token e34() throws ExcepcionLexica, IOException {
        return new Token("op!=", lexema, GA.getNro_linea());
    }

    /*
     *  Estado para Token operador || (op||)
     */
    private Token e35() throws ExcepcionLexica, IOException {
        if (caracter_actual == '|'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e36();
        }
        else {
            throw new ExcepcionLexica("No es un simbolo valido. Se espera otro '|' ." , lexema, GA.getNro_linea(), GA.get_Linea(), GA.get_nro_char_actual() - 1);
        }
    }

    private Token e36() throws ExcepcionLexica, IOException {
        return new Token("op||", lexema, GA.getNro_linea());
    }

    /*
     *  Estado para Token operador && (op&&)
     */
    private Token e37() throws ExcepcionLexica, IOException {
        if (caracter_actual == '&'){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e38();
        }
        else {
            throw new ExcepcionLexica("No es un simbolo valido. Se espera otro '&' ." , lexema, GA.getNro_linea(), GA.get_Linea(), GA.get_nro_char_actual() - 1);
        }
    }

    private Token e38() throws ExcepcionLexica, IOException {
        return new Token("op&&", lexema, GA.getNro_linea());
    }

    /*
     *  Estado para Token asignacion = (asig=)
     *  o operador == (op==)
     */
    private Token e39() throws ExcepcionLexica, IOException {
        if (caracter_actual == '='){
            lexema = lexema + caracter_actual;
            caracter_actual = GA.getNextChar();
            return e40();
        }
        else {
            return new Token("asig=", lexema, GA.getNro_linea());
        }
    }

    private Token e40() throws ExcepcionLexica, IOException {
        return new Token("op==", lexema, GA.getNro_linea());
    }

    /*
     *  Estado para Token EOF (EOF)
     */
    private Token e41() throws ExcepcionLexica, IOException {
        return new Token("EOF", lexema, GA.getNro_linea());
    }

    private boolean es_Minuscula(char c) {
        int char_int = (int) c;
        boolean toReturn = false;
        if ((char_int >= 97) && (char_int <= 122))
            toReturn = true;
        return toReturn;
    }

    private boolean es_Mayuscula(char c) {
        int char_int = (int) c;
        boolean toReturn = false;
        if ((char_int >= 65) && (char_int <= 90))
            toReturn = true;
        return toReturn;
    }

    private boolean es_Letra(char c){
        boolean toReturn = es_Mayuscula(c) || es_Minuscula(c);

        return toReturn;
    }

    private boolean es_Digito(char c) {
        int char_int = (int) c;
        boolean toReturn = false;
        if ((char_int >= 48) && (char_int <= 57))
            toReturn = true;
        return toReturn;
    }

    private boolean es_espacioBlanco(char c){
        boolean toReturn = false;
        if(((int) caracter_actual == 32) || ((int) caracter_actual == 10) || ((int) caracter_actual == 9))
            toReturn = true;

        return toReturn;
    }

    private void llenar_tabla(){
        tabla_reservadas.put("class",new Token("pr_class","class",0));
        tabla_reservadas.put("extends",new Token("pr_extends","extends",0));
        tabla_reservadas.put("static",new Token("pr_static","static",0));
        tabla_reservadas.put("dynamic",new Token("pr_dynamic","dynamic",0));
        tabla_reservadas.put("void",new Token("pr_void","void",0));
        tabla_reservadas.put("boolean",new Token("pr_boolean","boolean",0));
        tabla_reservadas.put("char",new Token("pr_char","char",0));
        tabla_reservadas.put("int",new Token("pr_int","int",0));
        tabla_reservadas.put("public",new Token("pr_public","public",0));
        tabla_reservadas.put("private",new Token("pr_private","private",0));
        tabla_reservadas.put("if",new Token("pr_if","if",0));
        tabla_reservadas.put("else",new Token("pr_else","else",0));
        tabla_reservadas.put("for",new Token("pr_for","for",0));
        tabla_reservadas.put("return",new Token("pr_return","return",0));
        tabla_reservadas.put("this",new Token("pr_this","this",0));
        tabla_reservadas.put("new",new Token("pr_new","new",0));
        tabla_reservadas.put("null",new Token("pr_null","null",0));
        tabla_reservadas.put("true",new Token("pr_true","true",0));
        tabla_reservadas.put("false",new Token("pr_false","false",0));
        tabla_reservadas.put("String",new Token("pr_String","String",0));
    }

    public boolean hubo_errores(){
        return huboErrores;
    }
}
