package io.github.tjheslin1.westie;

import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.tjheslin1.westie.WestieRegexes.*;

public class WestieRegexesTest implements WithAssertions {

    @Test
    public void todoRegexTest() throws Exception {
        String todoComment = "    // TODO pull this into another class.";

        Pattern pattern = Pattern.compile(TODO_REGEX);
        Matcher matcher = pattern.matcher(todoComment);

        assertThat(matcher.find()).isTrue();
        assertThat(matcher.group()).isEqualTo(todoComment);
    }

    @Test
    public void todoMustHaveADateRegexTest() throws Exception {
        String todoComment = "    // TODO 22/10/2016 write the test first.";

        Pattern pattern = Pattern.compile(TODOS_MUST_HAVE_DATE_REGEX);
        Matcher matcher = pattern.matcher(todoComment);

        assertThat(matcher.find()).isTrue();
        assertThat(matcher.group()).isEqualTo(todoComment);
    }

    @Test
    public void extractNumberTest() throws Exception {
        String todoComment = "    // TODO Git-101 finish this issue.";

        Pattern pattern = Pattern.compile(EXTRACT_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(todoComment);

        assertThat(matcher.find()).isTrue();
        assertThat(matcher.group()).isEqualTo("101");
    }
}