package textfilemerger;

import textfilemerger.SequenceDetector;
import org.junit.Test;
import static org.junit.Assert.*;

public class SequenceParserTest {

    @Test
    public void check() {
        String[] originalSequence = new String[]{"1", "a", "2", "b", "c", "d", "3"};
        String[] proposedSequence = new String[]{"x", "a", "y", "b", "d", "z"};
        
        int[][] control = {{ 0, -1, -1, -1, -1, -1},
                           { 1,  0,  0, -1, -1, -1},
                           { 1,  0,  0, -1, -1, -1},
                           { 1,  1,  1,  0, -1, -1},
                           { 1,  1,  1,  0, -1, -1},
                           { 1,  1,  1,  1,  0,  0},
                           { 1,  1,  1,  1,  0,  0}};
        
        int[][] detected = new int[7][6];
        
        SequenceDetector parser = new SequenceDetector(originalSequence, proposedSequence);
        
        for (int i = 0; i < originalSequence.length; i++) {
            for (int j = 0; j < proposedSequence.length; j++) {
                detected[i][j] = parser.compareSequence(i, j);
            }
        }

        assertArrayEquals(control, detected);
    }

}