package io.github.tjheslin1.westie.importrestrictions;

import io.github.tjheslin1.westie.FileLineViolation;
import io.github.tjheslin1.westie.LineAssertions;
import io.github.tjheslin1.westie.TestWestieFileReader;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static io.github.tjheslin1.westie.importrestrictions.ImportRestriction.importRestriction;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class ImportsRestrictionCheckerTest implements WithAssertions {

    private static final ImportRestriction MOCKITO_RESTRICTION
            = importRestriction("io.github.tjheslin1.examples.mockito", "import org.mockito.Mockito;");

    private static final ImportRestriction APACHE_RESTRICITON
            = importRestriction("io.github.tjheslin1.examples.apache", "import org.apache.commons.lang3.builder.HashCodeBuilder;");

    @Test
    public void enforcesOnlySpecifiedPackagesCanUseCertainThirdPartyImports() throws Exception {
        List<ImportRestriction> importRestrictions = singletonList(MOCKITO_RESTRICTION);
        ImportsRestrictionChecker importsRestrictionChecker = new ImportsRestrictionChecker(importRestrictions, new TestWestieFileReader(), emptyList());

        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/thirdparties");
        List<FileLineViolation> violations = importsRestrictionChecker.checkImportsAreOnlyUsedInAcceptedPackages(pathToCheck);

        assertThat(violations.size()).isEqualTo(2);
        LineAssertions lineAssertions = new LineAssertions(violations);
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithUnacceptedThirdPartyImport.java'\n" +
                "\n" +
                "import org.mockito.Mockito;\n" +
                "\n" +
                "Violation was caused by an import which does not matching any of the import restrictions.\n");
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithUnacceptedThirdPartyImportToIgnore.java'\n" +
                "\n" +
                "import org.mockito.Mockito;\n" +
                "\n" +
                "Violation was caused by an import which does not matching any of the import restrictions.\n");
    }

    @Test
    public void enforcesMultiplePackageImportRestrictionsWithAnExemption() throws Exception {
        List<ImportRestriction> importRestrictions = asList(MOCKITO_RESTRICTION, APACHE_RESTRICITON);
        ImportsRestrictionChecker importsRestrictionChecker = new ImportsRestrictionChecker(importRestrictions, new TestWestieFileReader(), singletonList("ClassWithUnacceptedThirdPartyImportToIgnore"));

        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/thirdparties");
        List<FileLineViolation> violations = importsRestrictionChecker.checkImportsAreOnlyUsedInAcceptedPackages(pathToCheck);

        assertThat(violations.size()).isEqualTo(2);
        LineAssertions lineAssertions = new LineAssertions(violations);
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithUnacceptedThirdPartyImport.java'\n" +
                "\n" +
                "import org.mockito.Mockito;\n" +
                "\n" +
                "Violation was caused by an import which does not matching any of the import restrictions.\n");
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithUnacceptedThirdPartyImport.java'\n" +
                "\n" +
                "import org.apache.commons.lang3.builder.HashCodeBuilder;\n" +
                "\n" +
                "Violation was caused by an import which does not matching any of the import restrictions.\n");
    }
}