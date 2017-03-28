package io.github.tjheslin1.westie;

import org.junit.Test;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class WestieAnalyserTest {

    @Test
    public void blah() throws Exception {
        Path pathToCheck = null;
        new WestieAnalyser().analyseDirectory(pathToCheck).forJavaFiles().analyse(this::doStuff);

    }

    private List<Violation> doStuff(Path path) {
        return null;
    }
}