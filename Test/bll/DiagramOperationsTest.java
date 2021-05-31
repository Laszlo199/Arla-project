package bll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiagramOperationsTest {

    @Test
    void getArray() {
        DiagramOperations diagramOperations = new DiagramOperations();
        double[] actual = diagramOperations.getArray("25,34,45");
        double[] expected = new double[]{25.0, 34.0, 45.0};
        Assertions.assertArrayEquals(expected, actual);
    }
}