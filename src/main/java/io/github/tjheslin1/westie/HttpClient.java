package io.github.tjheslin1.westie;

public interface HttpClient<Request, Response> {

    Response execute(Request request);
}
