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
public class TesteIncorrect113 {
    
    @Test
    public void testeIncorrect113() throws IOException, FileNotFoundException {
        
        testar( "TesteIncorrect_113",
                "../examples/Incorrect/CPRL0/Incorrect_113.cprl",
                "../examples/Incorrect/CPRL0/Incorrect_113-ParserV3-Results.txt" );
        
    }
    
}
