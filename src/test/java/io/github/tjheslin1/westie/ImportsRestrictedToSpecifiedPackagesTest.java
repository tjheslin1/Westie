package io.github.tjheslin1.westie;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static io.github.tjheslin1.westie.ImportRestriction.importRestriction;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class ImportsRestrictedToSpecifiedPackagesTest implements WithAssertions {

    private static final ImportRestriction MOCKITO_RESTRICTION
            = importRestriction("io.github.tjheslin1.examples.thirdparties.mockito", "import org.mockito.Mockito;");

    private static final ImportRestriction APACHE_RESTRICITON
            = importRestriction("io.github.tjheslin1.examples.thirdparties.apache", "import org.apache.commons.lang3.builder.HashCodeBuilder;");

    @Test
    public void enforcedOnlySpecifiedPackagesCanUseCertainThirdPartyImports() throws Exception {
        List<ImportRestriction> importRestrictions = singletonList(MOCKITO_RESTRICTION);
        ImportsRestrictedToSpecifiedPackages importsRestrictedToSpecifiedPackages = new ImportsRestrictedToSpecifiedPackages(importRestrictions, emptyList());

        Path testFilePath = Paths.get(TODOsMustFollowStructure.class.getClassLoader().getResource("io/github/tjheslin1/examples/ClassWithTodos.java").toURI());
        List<Violation> violations = importsRestrictedToSpecifiedPackages.checkImportsAreOnlyUsedInAcceptedPackages(testFilePath.getParent());

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations.get(0).toString()).startsWith("Line 'import org.mockito.Mockito;");
    }

    @Test
    public void enforcesMultiplePackageImportRestrictionsWithAnExemption() throws Exception {
        List<ImportRestriction> importRestrictions = asList(MOCKITO_RESTRICTION, APACHE_RESTRICITON);
        ImportsRestrictedToSpecifiedPackages importsRestrictedToSpecifiedPackages = new ImportsRestrictedToSpecifiedPackages(importRestrictions, singletonList("ClassWithUnacceptedThirdPartyImportToIgnore"));

        Path testFilePath = Paths.get(TODOsMustFollowStructure.class.getClassLoader().getResource("io/github/tjheslin1/examples/ClassWithTodos.java").toURI());
        List<Violation> violations = importsRestrictedToSpecifiedPackages.checkImportsAreOnlyUsedInAcceptedPackages(testFilePath.getParent());

        assertThat(violations.size()).isEqualTo(2);
        assertThat(violations.get(0).toString()).startsWith("Line 'import org.mockito.Mockito;");
        assertThat(violations.get(1).toString()).startsWith("Line 'import org.apache.commons.lang3.builder.HashCodeBuilder;");
    }
}