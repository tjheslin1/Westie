package io.github.tjheslin1.westie.importrestrictions;

import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.WestieStaticAnalysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

public class ImportsRestrictedToSpecifiedPackages extends WestieStaticAnalysis {

    private final List<ImportRestriction> importRestrictions;

    public ImportsRestrictedToSpecifiedPackages(List<ImportRestriction> importRestrictions, List<String> javaFilesToIgnore) {
        super(javaFilesToIgnore);
        this.importRestrictions = importRestrictions;
    }

    public List<Violation> checkImportsAreOnlyUsedInAcceptedPackages(Path pathToCheck) throws IOException {
        return Files.list(pathToCheck)
                .filter(this::isAJavaFile)
                .filter(this::notAnExemptFile)
                .flatMap(this::verifyImports)
                .peek(this::reportViolation)
                .collect(toList());
    }

    private Stream<Violation> verifyImports(Path file) {
        try {
            List<String> lines = Files.lines(file).collect(toList());
            String packageLine = lines.stream().findFirst().get();
            return lines.stream()
                    .filter(this::importLines)
                    .filter(importLine -> importUsedOutsideOfAcceptedPackage(packageLine, importLine))
                    .map(importLine -> new Violation(file, importLine));
        } catch (IOException e) {
            return Stream.of(new Violation(file, "Unable to read file."));
        }
    }

    private boolean importLines(String line) {
        return line.startsWith("import ");
    }

    private boolean importUsedOutsideOfAcceptedPackage(String packageLine, String importLine) {
        for (ImportRestriction importRestriction : importRestrictions) {
            if (importLine.matches(importRestriction.importRegex)) {
                return !packageLine.startsWith(format("package %s", importRestriction.packagePath));
            }
        }
        return false;
    }

    private void reportViolation(Violation violation) {
        System.out.println(format("Violation!%n'%s'%nThe above violation was caused by an import which " +
                        "does not matching any of the import restrictions specified in the Westie class: '%s'",
                violation, this.getClass().getSimpleName()));
    }
}
