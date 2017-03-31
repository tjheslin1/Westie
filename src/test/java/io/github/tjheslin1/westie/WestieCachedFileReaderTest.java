package io.github.tjheslin1.westie;

import io.github.tjheslin1.westie.importrestrictions.ImportsRestrictionAnalyser;
import io.github.tjheslin1.westie.infrastructure.FileLinesReader;
import io.github.tjheslin1.westie.infrastructure.WestieCachedFileReader;
import io.github.tjheslin1.westie.todostructure.TodosStructureAnalyser;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Collections.emptyList;

public class WestieCachedFileReaderTest implements WithAssertions, WithMockito {

    private FileLinesReader fileLinesReader = mock(FileLinesReader.class);

    @Test
    public void blowsUpIfAnythingOtherThanARegularFileIsPassedIn() throws Exception {
        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/lineReading");
        WestieCachedFileReader fileReader = new WestieCachedFileReader();

        assertThatThrownBy(() -> fileReader.readAllLines(pathToCheck))
                .hasMessage("Expected a file to read. Instead was provided: 'src/test/resources/io/github/tjheslin1/examples/lineReading'");
    }

    @Test
    public void readsActualFileOnFirstReadThenReadsFromCache() throws Exception {
        when(fileLinesReader.readAllLines(any())).thenReturn(emptyList());

        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/lineReading/ReadMyLines.java");

        WestieCachedFileReader fileReader = new WestieCachedFileReader(fileLinesReader);
        fileReader.readAllLines(pathToCheck);
        fileReader.readAllLines(pathToCheck);
        fileReader.readAllLines(pathToCheck);

        verify(fileLinesReader).readAllLines(pathToCheck);
        verifyNoMoreInteractions(fileLinesReader);
    }

    @Test
    public void reusesCacheBetweenAnalysers() throws Exception {
        when(fileLinesReader.readAllLines(any())).thenReturn(emptyList());

        Path pathToCheck = Paths.get("src/test/resources/io/github/tjheslin1/examples/lineReading");

        WestieCachedFileReader fileReader = new WestieCachedFileReader(fileLinesReader);

        TodosStructureAnalyser todosStructureAnalyser = new TodosStructureAnalyser("", fileReader);
        ImportsRestrictionAnalyser importsRestrictionChecker = new ImportsRestrictionAnalyser(emptyList(), fileReader);

        todosStructureAnalyser.checkAllTodosFollowExpectedStructure(pathToCheck, emptyList());
        importsRestrictionChecker.checkImportsAreOnlyUsedInAcceptedPackages(pathToCheck);

        Mockito.verify(fileLinesReader).readAllLines(pathToCheck.resolve("ReadMyLines.java"));
        verifyNoMoreInteractions(fileLinesReader);
    }
}