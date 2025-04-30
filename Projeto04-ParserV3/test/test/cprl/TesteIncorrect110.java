/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.cprl;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Test;
import static test.cprl.TestUtils.testar;

/**
 *
 * @author Prof. Dr. David Buzatto
 */
public class TesteIncorrect110 {
    
    @Test
    public void testeIncorrect110() throws IOException, FileNotFoundException {
        
        testar( "TesteIncorrect_110",
                "../examples/Incorrect/CPRL0/Incorrect_110.cprl",
                "../examples/Incorrect/CPRL0/Incorrect_110-ParserV3-Results.txt" );
        
    }
    
}
