package com.github.lucasgois.tcc.common;

import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class UtilTest {

    @Test
    void listFilesTest() throws Exception {
        Path testDirectory = Path.of("src/test/resources/testListFile");
        String expectedHashEqual = "9F0FA1CD32380E2541D81AD69B0D0B5B6BF4C56533E760F29520E4EFD7346288";
        String expectedHashDifferent = "A21BE189EBC1DC423117B0FEF4C5C0B74D819BE0B667625E83A190F234D9A89D";
        Path[] expectedFilePaths = {
                testDirectory.resolve("File1.txt"),
                testDirectory.resolve("File2.txt"),
                testDirectory.resolve("File3.txt"),
        };

        List<Pair<String, String>> resultList = Util.listFilesWithHashes(testDirectory);

        assertEquals(3, resultList.size());
        for (int i = 0; i < expectedFilePaths.length; i++) {
            assertEquals(expectedFilePaths[i].toString(), resultList.get(i).getValue());
        }
        assertEquals(expectedHashEqual, resultList.get(0).getKey());
        assertEquals(expectedHashEqual, resultList.get(1).getKey());
        assertEquals(expectedHashDifferent, resultList.get(2).getKey());
    }

    @Test
    void calculateHashTest() throws Exception {
        final byte[] actual = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        final String expected = "3E5718FEA51A8F3F5BACA61C77AFAB473C1810F8B9DB330273B4011CE92C787E";

        assertEquals(expected, Util.calculateHash(actual));
    }

    @Test
    void byteToHexTest() {
        final byte[] actual = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        final String expected = "000102030405060708090A0B0C0D0E0F10";

        assertEquals(expected, Util.byteToHex(actual));
    }
}