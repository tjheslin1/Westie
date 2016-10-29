package io.github.tjheslin1.westie;

import java.nio.file.Path;
import java.util.List;

public abstract class WestieStaticAnalysis {

    private final List<String> javaFilesToIgnore;

    public WestieStaticAnalysis(List<String> javaFilesToIgnore) {
        this.javaFilesToIgnore = javaFilesToIgnore;
    }

    protected boolean isAJavaFile(Path file) {
        return file.toString().endsWith(".java");
    }

    protected boolean notAnExemptFile(Path file) {
        return !javaFilesToIgnore.stream()
                .map(this::postFixedWithJavaExtension)
                .anyMatch(exemptFile -> file.toString().endsWith((exemptFile)));
    }

    protected String filenameFromPath(Path path) {
        return path.subpath(path.getNameCount() - 1, path.getNameCount()).toString();
    }

    private String postFixedWithJavaExtension(String javaFile) {
        return javaFile.endsWith(".java") ? javaFile : javaFile + ".java";
    }
}
