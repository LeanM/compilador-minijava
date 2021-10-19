public class PruebaClase_sin_errores extends Nada{

    private static boolean a = false;
    public String texto;

    public PruebaClase_sin_errores (){
        if (a)
            texto = "asd";
        else texto = "hola";
    }

    private void m1(){
        int a = 0; char c = 'a';
    }

    /*
     * metodo m2
     * @return
     */
    public char m2(){
        char c = '\'';
        for (int i = 0; i<=5; i++){
            if (this == null);
            //nada
        }
        return c;
    }
}