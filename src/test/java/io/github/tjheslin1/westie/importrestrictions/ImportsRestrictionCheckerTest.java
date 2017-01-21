package io.github.tjheslin1.westie.importrestrictions;

import io.github.tjheslin1.westie.Violation;
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
        ImportsRestrictionChecker importsRestrictionChecker = new ImportsRestrictionChecker(importRestrictions, emptyList());

        List<Violation> violations = importsRestrictionChecker.checkImportsAreOnlyUsedInAcceptedPackages(Paths.get("src/test/resources/io/github/tjheslin1/examples/thirdparties"));

        assertThat(violations.size()).isEqualTo(2);
        assertThat(violations.get(0).toString()).matches("'import org\\.mockito\\.Mockito;' in file '.*ClassWithUnacceptedThirdPartyImport\\.java'.*");
        assertThat(violations.get(1).toString()).matches("'import org\\.mockito\\.Mockito;' in file '.*ClassWithUnacceptedThirdPartyImportToIgnore.java'.*");
    }

    @Test
    public void enforcesMultiplePackageImportRestrictionsWithAnExemption() throws Exception {
        List<ImportRestriction> importRestrictions = asList(MOCKITO_RESTRICTION, APACHE_RESTRICITON);
        ImportsRestrictionChecker importsRestrictionChecker = new ImportsRestrictionChecker(importRestrictions, singletonList("ClassWithUnacceptedThirdPartyImportToIgnore"));

        List<Violation> violations = importsRestrictionChecker.checkImportsAreOnlyUsedInAcceptedPackages(Paths.get("src/test/resources/io/github/tjheslin1/examples/thirdparties"));

        assertThat(violations.size()).isEqualTo(2);
        assertThat(violations.get(0).toString()).startsWith("'import org.mockito.Mockito;");
        assertThat(violations.get(1).toString()).startsWith("'import org.apache.commons.lang3.builder.HashCodeBuilder;");
    }
}