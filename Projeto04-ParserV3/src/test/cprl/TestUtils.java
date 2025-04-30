/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.cprl;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.citadel.compiler.ErrorHandler;
import edu.citadel.compiler.InternalCompilerException;
import edu.citadel.compiler.Source;
import edu.citadel.cprl.Parser;
import edu.citadel.cprl.ast.AST;
import edu.citadel.cprl.ast.ExitStmt;
import edu.citadel.cprl.ast.Program;
import edu.citadel.cprl.ast.ReturnStmt;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import static org.junit.Assert.assertEquals;

/**
 * Métodos utilitários para os testes.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class TestUtils {
    
    // <editor-fold defaultstate="collapsed" desc="Dados dos Testes (não mexa!)">
        
    private static String[] titulos = { 
            "TesteIncorrect_101",
            "TesteIncorrect_102",
            "TesteIncorrect_103",
            "TesteIncorrect_104",
            "TesteIncorrect_105",
            "TesteIncorrect_106",
            "TesteIncorrect_107",
            "TesteIncorrect_108",
            "TesteIncorrect_109",
            "TesteIncorrect_110",
            "TesteIncorrect_111",
            "TesteIncorrect_112",
            "TesteIncorrect_113",
            "TesteIncorrect_114",
            "TesteIncorrect_115",
            "TesteIncorrect_116",
            "TesteIncorrect_117",
            "TesteIncorrect_118",
            "TesteIncorrect_119",
            "TesteIncorrect_120",
            "TesteIncorrect_121",
            "TesteIncorrect_122",
            "TesteIncorrect_123",
            "TesteIncorrect_124",
            "TesteIncorrect_125",
            "TesteIncorrect_126",
            "TesteIncorrect_127",
            "TesteIncorrect_128",
            "TesteIncorrect_129",
            "TesteIncorrect_201",
            "TesteIncorrect_202",
            "TesteIncorrect_203",
            "TesteIncorrect_204",
            "TesteIncorrect_205",
            "TesteIncorrect_206",
            "TesteIncorrect_207",
            "TesteIncorrect_208",
            "TesteIncorrect_209",
            "TesteIncorrect_210",
            "TesteIncorrect_211",
            "TesteIncorrect_212",
            "TesteIncorrect_213",
            "TesteIncorrect_214",
            "TesteIncorrect_215",
            "TesteIncorrect_216",
            "TesteIncorrect_217",
            "TesteIncorrect_218",
            "TesteIncorrect_219",
            "TesteIncorrect_220",
            "TesteIncorrect_221",
            "TesteIncorrect_222",
            "TesteIncorrect_301",
            "TesteIncorrect_302",
            "TesteIncorrect_303",
            "TesteIncorrect_304",
            "TesteIncorrect_305"
    };

    private static String[] caminhosFontes = {
            "../examples/Incorrect/CPRL0/Incorrect_101.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_102.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_103.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_104.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_105.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_106.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_107.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_108.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_109.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_110.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_111.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_112.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_113.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_114.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_115.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_116.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_117.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_118.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_119.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_120.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_121.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_122.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_123.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_124.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_125.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_126.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_127.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_128.cprl",
            "../examples/Incorrect/CPRL0/Incorrect_129.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_201.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_202.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_203.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_204.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_205.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_206.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_207.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_208.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_209.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_210.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_211.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_212.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_213.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_214.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_215.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_216.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_217.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_218.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_219.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_220.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_221.cprl",
            "../examples/Incorrect/Subprograms/Incorrect_222.cprl",
            "../examples/Incorrect/Arrays/Incorrect_301.cprl",
            "../examples/Incorrect/Arrays/Incorrect_302.cprl",
            "../examples/Incorrect/Arrays/Incorrect_303.cprl",
            "../examples/Incorrect/Arrays/Incorrect_304.cprl",
            "../examples/Incorrect/Arrays/Incorrect_305.cprl"
    };// </editor-fold>
    
    private static final Gson gson = new GsonBuilder().
            setPrettyPrinting().
            serializeNulls().
            setExclusionStrategies( new ExclusionStrategy(){
                @Override
                public boolean shouldSkipField( FieldAttributes fa ) {
                    return fa.getDeclaringClass() == ExitStmt.class &&
                           fa.getName().equals( "loopStmt" );
                }

                @Override
                public boolean shouldSkipClass( Class<?> type ) {
                    return false;
                }
            }, new ExclusionStrategy(){
                @Override
                public boolean shouldSkipField( FieldAttributes fa ) {
                    return fa.getDeclaringClass() == ReturnStmt.class &&
                           fa.getName().equals( "subprogramDecl" );
                }

                @Override
                public boolean shouldSkipClass( Class<?> type ) {
                    return false;
                }
            }).
            create();
    
    /*
     * Executa um teste comparando a saída do escaneamento de um arquivo de
     * código fonte e um arquivo de resultado.
     */
    public static void testar( 
            String titulo, 
            String caminhoFonte, 
            String caminhoResultado ) throws FileNotFoundException, IOException {
        
        System.out.println( titulo );
        String expResult = carregar( caminhoResultado );
        String result = gerar( caminhoFonte );
        
        result = result.replace( "\r", "" );
        expResult = expResult.replace( "\r", "" );
        
        System.out.println( "\n\n*********** Resultado:" );
        System.out.println( result );
        
        System.out.println( "\n\n*********** Esperado:" );
        System.out.println( expResult );
        
        assertEquals( expResult, result );
        
    }
    
    public static void testarVarios( 
            String[] titulos, 
            String[] caminhosFontes,
            boolean executarTestesDeUnidade ) throws FileNotFoundException, IOException {
        
        int total = titulos.length;
        int corretos = 0;
        
        try ( 
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream( baos, true, StandardCharsets.UTF_8 ) ) {
            
            PrintStream ant = System.out;
            System.setOut( ps );
            
            String resultadosFinais = "";
            
            // coleta todas as compilações e resultados
            for ( int i = 0; i < titulos.length; i++ ) {
                
                String titulo = titulos[i];
                String caminhoFonte = caminhosFontes[i];
                
                System.err.println( "Processando " + titulo );
                
                executar( caminhoFonte );
                System.out.println( "---fim---" );
                System.out.flush();

            }
            
            resultadosFinais = baos.toString().trim();
            resultadosFinais = resultadosFinais.replace( "\r", "" );
            
            System.setOut( ant );
            
            Map<String, String> resultadosEsperados = carregarResultados();
            int i = 0;
            
            for ( String resultado : resultadosFinais.split( "---fim---" ) ) {
                
                resultado = resultado.trim();
                
                System.out.println( "#### Testando " + titulos[i] + " ####" );
                String expResult = resultadosEsperados.get( titulos[i] );
                
                if ( executarTestesDeUnidade ) {

                    System.out.println( "*********** Resultado:" );
                    System.out.println( resultado );

                    System.out.println( "\n*********** Esperado:" );
                    System.out.println( expResult );
                    
                    assertEquals( expResult, resultado );
                    System.out.println( "\n### " + titulos[i] + " ok! ###\n" );
                    System.out.println( "------------------------------------\n\n");
                    
                } else {
                    
                    boolean correto = expResult.equals( resultado );
                    
                    if ( correto ) {
                        corretos++;
                        System.out.println( "correto!" );
                    } else {
                        System.out.println( "errado!" );
                    }
                    
                }
                
                i++;
                
            }
            
            if ( !executarTestesDeUnidade ) {
                System.out.println( "   Total: " + total );
                System.out.println( "Corretos: " + corretos );
                System.out.println( "    Nota: " + (double) corretos / total * 10.0 );
            }
        
        }
        
    }
    
    /*
     * Carrega um arquivo de solução e retorna a String com o conteúdo do mesmo.
     */
    private static String carregar( String path ) throws FileNotFoundException {
        
        StringBuilder expResultBuilder = new StringBuilder();
        try ( Scanner s = new Scanner( new File( path ) ) ) {
            while ( s.hasNextLine() ) {
                expResultBuilder.append( s.nextLine() ).append( "\n" );
            }
            return expResultBuilder.toString().trim();
        }
        
    }
    
    /*
     * Carrega um arquivo de solução geral e retorna um mapa com o conteúdo do
     * mesmo associando os títulso com os conteúdos esperados.
     */
    public static Map<String, String> carregarResultados() throws FileNotFoundException, IOException {
        
        StringBuilder expResultBuilder = new StringBuilder();
        
        try ( Scanner s = new Scanner( new File( "../examples/testParser3_all Results - projeto.txt" ), StandardCharsets.UTF_8 ) ) {
            while ( s.hasNextLine() ) {
                expResultBuilder.append( s.nextLine() ).append( "\n" );
            }
        }
        
        Map<String, String> r = new LinkedHashMap<>();
        
        for ( String s : expResultBuilder.toString().replace( "\r", "" ).trim().split( "<sep>" ) ) {
            s = s.trim();
            r.put( s.split( "\n" )[0].replace( "Parsing ", "Teste" ).replace( ".cprl...", "" ).trim(), s );
        }
        
        return r;
        
    }
    
    /*
     * Executa o Parser em um arquivo de código fonte e retorna toda a saída
     * gerada pelo mesmo na saída padrão em uma String.
     */
    private static String gerar( String path ) throws IOException {
        
        try ( 
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream( baos, true, StandardCharsets.UTF_8 ) ) {
            
            PrintStream ant = System.out;
            System.setOut( ps );
            
            executar( path );
            System.out.flush();
            
            System.setOut( ant );
            
            return baos.toString().trim();
            
        }
        
    }
    
    public static void executar( String fileName ) throws IOException {
        
        File f = new File( fileName );
        FileReader fileReader = null;

        fileReader = new FileReader( fileName, StandardCharsets.UTF_8 );

        System.out.println( "Parsing " + f.getName() + "..." );

        Source source = new Source( fileReader );
        edu.citadel.cprl.Scanner scanner = new edu.citadel.cprl.Scanner( source );
        Parser parser = new Parser( scanner );
        boolean ok = true;
        
        Program program = null;
        
        try {
            
            program = parser.parseProgram();
            
            if ( ErrorHandler.getInstance().errorsExist() ) {
                System.out.println( "Errors detected in " + f.getName() + " -- parsing terminated." );
                ok = false;
            }
            
        } catch ( IllegalStateException e ) {
            ok = false;
        } catch ( InternalCompilerException e ) {
            ok = false;
        }
        
        if ( ok ) {
            System.out.println( "Parsing complete." );
        }
                
        System.out.println( "\n*** AST Dump ***" );
        if ( program != null ) {
            System.out.println( "Program: " + gson.toJson( program ) );
        } else {
            System.out.println( "The AST could not be built." );
        }
        
        /*if ( program != null ) {
            XMLArvoreVisitor xav = new XMLArvoreVisitor();
            program.accept( xav );
            System.out.println( xav.getXMLArvore() );
        }*/
        
        ErrorHandler.getInstance().resetErrorCount();
        AST.resetCurrentLabelNum();
        fileReader.close();
        
    }
    
    public static void testeEmLote( int[] intervalos, boolean executarTestesDeUnidade ) throws IOException, FileNotFoundException {
        
        List<String> titulosL = new ArrayList<>();
        List<String> caminhosFontesL = new ArrayList<>();
        
        for ( int i = 0; i < intervalos.length; i += 2 ) {
            int inicio = intervalos[i];
            int fim = intervalos[i+1];
            for ( int j = inicio; j <= fim; j++ ) {
                titulosL.add( titulos[j-1] );
                caminhosFontesL.add( caminhosFontes[j-1] );
            }
        }
        
        testarVarios( 
                titulosL.toArray( new String[0] ), 
                caminhosFontesL.toArray( new String[0] ),
                executarTestesDeUnidade );
        
    }
    
}
