package io.github.tjheslin1.westie.importrestrictions;

import io.github.tjheslin1.westie.FileViolation;
import io.github.tjheslin1.westie.LineAssertions;
import io.github.tjheslin1.westie.Violation;
import io.github.tjheslin1.westie.testinfrastructure.TestWestieFileReader;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static io.github.tjheslin1.westie.importrestrictions.ImportRestriction.importRestriction;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class ImportsRestrictionAnalyserTest implements WithAssertions {

    private static final ImportRestriction MOCKITO_RESTRICTION
            = importRestriction("io.github.tjheslin1.examples.mockito", "import org.mockito.Mockito;");

    private static final ImportRestriction APACHE_RESTRICITON
            = importRestriction("io.github.tjheslin1.examples.apache", "import org.apache.commons.lang3.builder.HashCodeBuilder;");

    @Test
    public void enforcesOnlySpecifiedPackagesCanUseCertainThirdPartyImports() throws Exception {
        List<ImportRestriction> importRestrictions = singletonList(MOCKITO_RESTRICTION);
        ImportsRestrictionAnalyser importsRestrictionAnalyser = new ImportsRestrictionAnalyser(importRestrictions, new TestWestieFileReader());

        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/thirdparties");
        List<Violation> violations = importsRestrictionAnalyser.checkImportsAreOnlyUsedInAcceptedPackages(pathToCheck);

        violations.forEach(System.out::println);

        assertThat(violations.size()).isEqualTo(2);
        LineAssertions lineAssertions = new LineAssertions(violations);
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithUnacceptedThirdPartyImport.java'\n" +
                "\n" +
                "import org.mockito.Mockito;\n" +
                "\n" +
                "Violation was caused by the above import which was used outside of its accepted package.\n");
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithUnacceptedThirdPartyImportToIgnore.java'\n" +
                "\n" +
                "import org.mockito.Mockito;\n" +
                "\n" +
                "Violation was caused by the above import which was used outside of its accepted package.\n");
    }

    @Test
    public void enforcesMultiplePackageImportRestrictionsWithAnExemption() throws Exception {
        List<ImportRestriction> importRestrictions = asList(MOCKITO_RESTRICTION, APACHE_RESTRICITON);
        ImportsRestrictionAnalyser importsRestrictionAnalyser = new ImportsRestrictionAnalyser(importRestrictions, new TestWestieFileReader());

        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/thirdparties");
        List<Violation> violations = importsRestrictionAnalyser.checkImportsAreOnlyUsedInAcceptedPackages(pathToCheck, singletonList("ClassWithUnacceptedThirdPartyImportToIgnore"));

        assertThat(violations.size()).isEqualTo(2);
        LineAssertions lineAssertions = new LineAssertions(violations);
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithUnacceptedThirdPartyImport.java'\n" +
                "\n" +
                "import org.mockito.Mockito;\n" +
                "\n" +
                "Violation was caused by the above import which was used outside of its accepted package.\n");
        lineAssertions.containsViolationMessage("Violation in file 'ClassWithUnacceptedThirdPartyImport.java'\n" +
                "\n" +
                "import org.apache.commons.lang3.builder.HashCodeBuilder;\n" +
                "\n" +
                "Violation was caused by the above import which was used outside of its accepted package.\n");
    }

    @Test
    public void reportsViolationForEmptyFile() throws Exception {
        List<ImportRestriction> importRestrictions = asList(MOCKITO_RESTRICTION, APACHE_RESTRICITON);
        ImportsRestrictionAnalyser importsRestrictionAnalyser = new ImportsRestrictionAnalyser(importRestrictions, new TestWestieFileReader());

        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/empty");
        List<Violation> violations = importsRestrictionAnalyser.checkImportsAreOnlyUsedInAcceptedPackages(pathToCheck, singletonList("ClassWithUnacceptedThirdPartyImportToIgnore"));

        assertThat(violations).hasSize(1);
        assertThat(violations.get(0)).isEqualTo(new FileViolation(pathToCheck.resolve("Empty.java"),
                "Empty file! - Should this file be in the list of ignored files? Or in a different directory?"));
    }
}