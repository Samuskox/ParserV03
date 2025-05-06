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
public class TesteEmLoteParser03Arrays {
    
    @Test
    public void testeEmLoteParser03Arrays() throws IOException, FileNotFoundException {
        
        // testes Arrays => 52..56
        int[] intervalos = { 
            52, 56
        };
        
        TestUtils.testeEmLote( intervalos, true );
        
    }
    
}
