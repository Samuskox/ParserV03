/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.cprl;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author Prof. Dr. David Buzatto
 */
public class TesteEmLoteParser02Subprograms {
    
    @Test
    public void testeEmLoteParser02Subprograms() throws IOException, FileNotFoundException {
        
        // testes Subprograms => 30..51
        int[] intervalos = { 
            30, 51
        };
        
        TestUtils.testeEmLote( intervalos, true );
        
    }
    
}
