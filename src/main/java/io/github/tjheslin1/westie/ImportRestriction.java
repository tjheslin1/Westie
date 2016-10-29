package io.github.tjheslin1.westie;

public class ImportRestriction extends ValueType {

    public final String packagePath;
    public final String importRegex;

    private ImportRestriction(String acceptedPackagePath, String importRegex) {
        this.packagePath = acceptedPackagePath;
        this.importRegex = importRegex;
    }

    public static ImportRestriction importRestriction(String packagePath, String importRegex) {
        return new ImportRestriction(packagePath, importRegex);
    }
}
