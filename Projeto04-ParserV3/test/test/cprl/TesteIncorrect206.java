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
public class TesteIncorrect206 {
    
    @Test
    public void testeIncorrect206() throws IOException, FileNotFoundException {
        
        testar( "TesteIncorrect_206",
                "../examples/Incorrect/Subprograms/Incorrect_206.cprl",
                "../examples/Incorrect/Subprograms/Incorrect_206-ParserV3-Results.txt" );
        
    }
    
}
