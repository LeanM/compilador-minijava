import AST.NodoBloque;
import AST.Sentencia.NodoSentencia;
import AnalizadorSemantico.EntradaClase;
import AnalizadorSemantico.EntradaMetodo;
import AnalizadorSemantico.EntradaUnidad;
import AnalizadorSemantico.TablaSimbolos;
import kotlin.test.AssertionsKt;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class ModuloPrincipalTest {

    private static String directorio_archivos_etapa3 = "Test/archivos-test-etapa3/";
    private static String directorio_archivos_etapa4 = "Test/archivos-test-etapa4/";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private String errorCode;

    @Before
    public void setUpClass() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDownClass() {
        System.setOut(originalOut);
    }

    @Test
    public void main() {
        assertEquals("asd", "asd");
        /*
        for (int i = 6; i <= 6; i++) {
            pruebas_etapa_3(i);
        }
        */
        pruebas_etapa_4();
    }

    void pruebas_etapa_2() {
        acceso_estatico();
    }

    void acceso_estatico() {
        //Prueba dos clases con el mismo nombre
        errorCode = "[SinErrores]";
        String[] args = {directorio_archivos_etapa3 + "acceso_estatico.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode, outContent.toString(), CoreMatchers.containsString(errorCode));
    }
    //-----------------------------------------ETAPA4------------------------------------------------------------------//

    void pruebas_etapa_4() {
        //expresiones();
        //sentencias();
        //accesos();
        //prueba_llamada_metodo();
        //prueba_metodo_encadenado();
        //prueba_acceso_variable();
        //prueba_var_encadenada();
        //prueba_invoca_constructor();
        prueba_var_local_y_acceso();
        //chequeo_expresion_unaria();
        //chequeo_expresion_binaria();
        //chequeo_casting();
        //chequeo_unidades();
        //chequeo_exp_encadenada();
        //prueba_bloques();
    }

    void asd(){
        int y;
        {
            int x;
            {
                //int y;
            }
        }
        int x;
    }

    void prueba_var_local_y_acceso(){
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"prueba_var_locales_y_acceso.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void chequeo_exp_encadenada(){
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"chequeo_exp_encadenada.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void prueba_bloques(){
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"prueba_bloques.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void chequeo_unidades(){
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"chequeo_unidades.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void chequeo_casting(){
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"chequeo_casting.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void chequeo_expresion_unaria(){
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"chequeo_expresiones_unarias.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void chequeo_expresion_binaria(){
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"chequeo_expresiones_binarias.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void prueba_llamada_metodo(){
        //Llamada a metodo a la izquierda de expresion punto o sin expresion punto
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"prueba_llamada_metodo.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void prueba_metodo_encadenado(){
        //Llamada a metodo encadenado
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"prueba_metodo_encadenado.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void prueba_var_encadenada() {
        //Llamada a metodo encadenado
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"prueba_var_encadenada.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void prueba_acceso_variable(){
        //La variable debe ser un atributo visible de la clase, o un parametro del metodo, o una variable local
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"prueba_acceso_variable.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void prueba_invoca_constructor(){
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"prueba_invoca_constructor.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void prueba_random(){
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"prueba.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void expresiones(){
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"prueba_expresiones.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void sentencias() {
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"prueba_sentencias.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void accesos() {
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa4+"prueba_accesos.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    //------------------------------------------ETAPA 3-----------------------------------------------------------------------------//
    void pruebas_etapa_3(int tipo_prueba){
        switch (tipo_prueba) {
            case 1 : clase_duplicada();
            case 2 : atributos_duplicados();
            case 3 : herencia_circular();
            case 4 : super_no_declarado();
            case 5 : atr_tipo_no_declarado();
            case 6 : redefinicion();
            case 0 : sin_errores();
        }
    }

    void redefinicion(){
        //Prueba dos clases con el mismo nombre
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa3+"redefinicion.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void atr_tipo_no_declarado(){
        //Prueba dos clases con el mismo nombre
        errorCode = "no esta declarada";
        String [] args = {directorio_archivos_etapa3+"atr-tipo-no-declarado.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void sin_errores(){
        //Prueba dos clases con el mismo nombre
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos_etapa3+"sin-errores.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void clase_duplicada () {
        //Prueba dos clases con el mismo nombre
        errorCode = "ya esta declarada";
        String [] args = {directorio_archivos_etapa3+"clases-repetidas.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void atributos_duplicados() {
        //Prueba una clase con dos atributos con el mismo nombre
        errorCode = "atributo declarado";
        String [] args = {directorio_archivos_etapa3+"atr-repetidos.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void herencia_circular() {
        //Prueba una clase con dos atributos con el mismo nombre
        errorCode = "herencia circular";
        String [] args = {directorio_archivos_etapa3+"herencia-circular.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void super_no_declarado(){
        //Prueba una clase con dos atributos con el mismo nombre
        errorCode = "no esta declarada";
        String [] args = {directorio_archivos_etapa3+"super-nulo.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

//--------------------------------------------------------------------------------------------------------------------------//
}