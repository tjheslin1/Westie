package io.github.tjheslin1.westie.katacalisthenicsenforcer;

import io.github.tjheslin1.westie.WestieAnalyser;
import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.util.List;

/**
 * Not yet in use.
 */
public class KataCalisthenicsAnalyser extends WestieAnalyser {

    public KataCalisthenicsAnalyser() {
        super();
    }

    /**
     * Not yet in use.
     */
    public KataCalisthenicsAnalyser(WestieFileReader fileReader, List<String> javaFilesToIgnore) {
        super(javaFilesToIgnore, fileReader);
    }
}
