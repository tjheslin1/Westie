package io.github.tjheslin1.westie;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.json.JSONObject;

import static java.lang.String.format;

public class Issues {

    private final HttpClient httpClient;
    private final String teamCityUsername;
    private final String teamCityPassword;

    public Issues(HttpClient httpClient, String teamCityUsername, String teamCityPassword) {
        this.httpClient = httpClient;
        this.teamCityUsername = teamCityUsername;
        this.teamCityPassword = teamCityPassword;
    }

    private final LoadingCache<String, String> issueStatusCache = CacheBuilder.<String, String>newBuilder()
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String issueNumber) throws Exception {
                    return jiraIssueStatus(issueNumber);
                }
            });

    public boolean isJiraIssueInDevelopment(String issueNumber) {
        String issueStatus = issueStatusCache.getUnchecked(issueNumber);
        return issueStatus.equals("Development") || issueStatus.equals("Created") || issueStatus.equals("Ready To Play");
    }

    public String jiraIssueStatus(String issueNumber) {
        Response response = jiraIssue(issueNumber);
        if (!response.isSuccessful()) {
            throw new IllegalStateException(format("Problem fetching issue:%n%s", response));
        }
        JSONObject jsonObject = new JSONObject(response.body);
        return status(jsonObject);
    }

    private Response jiraIssue(String issueNumber) {
        HttpUriRequest apacheRequest = RequestBuilder.get()
                .setUri("https://tasktracker.sns.sky.com/rest/api/2/issue/" + issueNumber + "?&os_username=" + teamCityUsername + "&os_password=" + teamCityPassword)
                .build();
        return (Response) httpClient.execute(apacheRequest);
    }

    private String status(JSONObject jsonObject) {
        return jsonObject.getJSONObject("fields").getJSONObject("status").getString("name");
    }
}
