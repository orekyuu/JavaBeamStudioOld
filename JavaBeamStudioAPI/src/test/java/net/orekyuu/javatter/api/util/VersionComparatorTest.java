package net.orekyuu.javatter.api.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class VersionComparatorTest {

    @Test
    public void testCompare() throws Exception {
        VersionComparator comparator = new VersionComparator();
        assertEquals(comparator.compare("1.0.0", "1.0.0"), 0);
        assertEquals(comparator.compare("1.0.0", "1.0"), 0);
        assertEquals(comparator.compare("1.0", "1.0.0"), 0);
        assertEquals(comparator.compare("1.2", "1.2"), 0);
        assertEquals(comparator.compare("3.1.2.5.7", "3.1.2.5.7"), 0);
        assertEquals(comparator.compare("3.1.20", "3.1.20"), 0);

        assertTrue(comparator.compare("1.0.0", "2.0.0") < 0);
        assertTrue(comparator.compare("1.0", "2.0.0") < 0);
        assertTrue(comparator.compare("1.0.4", "2.0") < 0);
        assertTrue(comparator.compare("2.0", "2.0.1") < 0);

        assertTrue(comparator.compare("2.0.0", "1.0.0") > 0);
        assertTrue(comparator.compare("2.0", "1.0.0") > 0);
        assertTrue(comparator.compare("2.0.0.1", "2.0") > 0);
    }
}