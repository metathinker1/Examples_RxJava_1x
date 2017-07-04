package com.example.com.example.rxjava;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Response;
import rx.Observable;

import java.net.URI;
import java.util.stream.Stream;

import static org.asynchttpclient.extras.rxjava.AsyncHttpObservable.toObservable;

/**
 * Created by robertwood on 6/24/17.
 */
public class StreamObservableEx01 extends HttpClient {

    private final AsyncHttpClient asyncHttpClient;

    public StreamObservableEx01(URI baseUri, AsyncHttpClient asyncHttpClient) {
        super(baseUri);
        this.asyncHttpClient = asyncHttpClient;
    }

    public Observable<Response> processNumbers(Integer number) {

        // TODO: build req request object
        //   Request Object may need to use @Builder -- check org.asynchttpclient.BoundRequestBuilder docs
        NumbersRequestMsg req = new NumbersRequestMsg(20);

        final Stream<String> path = Stream.of("/values01");
        return getCommand("process-numbers", getUrlObservable(path)
            .map(url -> {
                System.out.println("Sending number: " + number);
                BoundRequestBuilder builder = asyncHttpClient.preparePost(url)
                    .addHeader("Content-type", "application/json")
                    .setBody(writeValue(req));
                return builder;
            })
            .flatMap(boundRequestBuilder -> toObservable(() -> boundRequestBuilder))
            .onErrorResumeNext(throwable -> {
                System.out.println("Error: " + throwable.getMessage());
                return Observable.empty();
            }));
    }
}
