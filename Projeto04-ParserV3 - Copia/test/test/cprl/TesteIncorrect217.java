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
public class TesteIncorrect217 {
    
    @Test
    public void testeIncorrect217() throws IOException, FileNotFoundException {
        
        testar( "TesteIncorrect_217",
                "../examples/Incorrect/Subprograms/Incorrect_217.cprl",
                "../examples/Incorrect/Subprograms/Incorrect_217-ParserV3-Results.txt" );
        
    }
    
}
