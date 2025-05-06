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
public class TesteEmLoteParser01CPRL0 {
    
    @Test
    public void testeEmLoteParser01CPRL0() throws IOException, FileNotFoundException {
        
        // testes CPRL0 => 1..29
        int[] intervalos = {  
            1, 29
        };
        
        TestUtils.testeEmLote( intervalos, true );
        
    }
    
}
