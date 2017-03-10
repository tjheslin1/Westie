package io.github.tjheslin1.westie.katacalisthenicsenforcer;

import io.github.tjheslin1.westie.WestieChecker;
import io.github.tjheslin1.westie.infrastructure.WestieCachedFileReader;
import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.util.List;

import static java.util.Collections.emptyList;

public class KataCalisthenicsChecker extends WestieChecker {

    public KataCalisthenicsChecker() {
        super(new WestieCachedFileReader(), emptyList());
    }

    public KataCalisthenicsChecker(WestieFileReader fileReader, List<String> javaFilesToIgnore) {
        super(fileReader, javaFilesToIgnore);
    }
}
