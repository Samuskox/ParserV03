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
public class TesteIncorrect129 {
    
    @Test
    public void testeIncorrect129() throws IOException, FileNotFoundException {
        
        testar( "TesteIncorrect_129",
                "../examples/Incorrect/CPRL0/Incorrect_129.cprl",
                "../examples/Incorrect/CPRL0/Incorrect_129-ParserV3-Results.txt" );
        
    }
    
}
