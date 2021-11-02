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

    private static String directorio_archivos = "Test/archivos-test-etapa3/";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private String errorCode;

    @Before
    public  void setUpClass() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public  void tearDownClass() {
        System.setOut(originalOut);
    }

    @Test
    public void main() {
        assertEquals("asd","asd");

        for(int i=6;i<=6;i++){
            pruebas_etapa_3(i);
        }

        //pruebas_etapa_2();

        //pruebas_etapa_4();
    }

    void pruebas_etapa_2(){
        acceso_estatico();
    }

    void acceso_estatico(){
        //Prueba dos clases con el mismo nombre
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos+"acceso_estatico.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }
    //-----------------------------------------ETAPA4------------------------------------------------------------------//

    void pruebas_etapa_4(){
        mostrarAST();
    }

    void mostrarAST(){
        EntradaClase entradaClase;
        Enumeration<EntradaClase> enum_clases = TablaSimbolos.getInstance().get_tabla_clases().elements();

        while (enum_clases.hasMoreElements()) {
            entradaClase = enum_clases.nextElement();

            Enumeration<LinkedList<EntradaMetodo>> enum_metodos = entradaClase.get_tabla_metodos().elements();
            for(EntradaMetodo metodo : enum_metodos.nextElement()){
                NodoBloque bloque_metodo = metodo.get_bloque_sentencias();

            }

        }

    }

    void mostrarBloque(NodoBloque bloque){
        for (NodoSentencia sentencia : bloque.get_lista_sentencias()){

        }
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
        String [] args = {directorio_archivos+"redefinicion.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void atr_tipo_no_declarado(){
        //Prueba dos clases con el mismo nombre
        errorCode = "no esta declarada";
        String [] args = {directorio_archivos+"atr-tipo-no-declarado.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void sin_errores(){
        //Prueba dos clases con el mismo nombre
        errorCode = "[SinErrores]";
        String [] args = {directorio_archivos+"sin-errores.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void clase_duplicada () {
        //Prueba dos clases con el mismo nombre
        errorCode = "ya esta declarada";
        String [] args = {directorio_archivos+"clases-repetidas.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void atributos_duplicados() {
        //Prueba una clase con dos atributos con el mismo nombre
        errorCode = "atributo declarado";
        String [] args = {directorio_archivos+"atr-repetidos.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void herencia_circular() {
        //Prueba una clase con dos atributos con el mismo nombre
        errorCode = "herencia circular";
        String [] args = {directorio_archivos+"herencia-circular.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

    void super_no_declarado(){
        //Prueba una clase con dos atributos con el mismo nombre
        errorCode = "no esta declarada";
        String [] args = {directorio_archivos+"super-nulo.txt"};
        ModuloPrincipal.main(args);
        assertThat("No se encontro el codigo: " + errorCode,  outContent.toString(), CoreMatchers.containsString(errorCode));
    }

//--------------------------------------------------------------------------------------------------------------------------//
}