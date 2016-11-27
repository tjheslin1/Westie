package io.github.tjheslin1.westie;

import io.github.tjheslin1.westie.http.QueryParameter;
import io.github.tjheslin1.westie.http.Request;
import org.assertj.core.api.WithAssertions;
import org.junit.Test;

import java.util.List;

import static io.github.tjheslin1.westie.http.QueryParameter.queryParameter;
import static java.util.Arrays.asList;

public class RequestTest implements WithAssertions {

    @Test
    public void formatsRequest() throws Exception {
        List<QueryParameter> queryParameters = asList(queryParameter("test", "testValue"), queryParameter("test2", "testValue2"));
        Request getRequest = new Request("http://test.com", "GET", "{\"key\": \"value\"}", queryParameters);

        assertThat(getRequest).hasToString("GET http://test.com?test=testValue?test2=testValue2\n" +
                "{\"key\": \"value\"}");
    }
}