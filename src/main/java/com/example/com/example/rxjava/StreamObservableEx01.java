package com.example.com.example.rxjava;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import rx.Observable;

import java.net.URI;
import java.net.URISyntaxException;
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

    public Observable<Response> processNumbers(Integer numValues) {
        System.out.println("processNumbers: numValues: " + numValues);
        // TODO: build req request object
        //   Request Object may need to use @Builder -- check org.asynchttpclient.BoundRequestBuilder docs
        NumbersRequestMsg req = new NumbersRequestMsg(20);

        final Stream<String> path = Stream.of("/values01");
        return getCommand("process-numbers", getUrlObservable(path)
            .map(url -> {
                System.out.println("Sending numValues: " + numValues);
                // ?numValues=20
                url += "?numValues=" + numValues;
                System.out.println("Adjusted URL: " + url);
                BoundRequestBuilder builder = asyncHttpClient.prepareGet(url)
                    .addHeader("Content-type", "application/json");
                return builder;
            })
            .flatMap(boundRequestBuilder -> toObservable(() -> boundRequestBuilder))
            .onErrorResumeNext(throwable -> {
                System.out.println("Error: " + throwable.getMessage());
                return Observable.empty();
            }));

//        return getCommand("process-numbers", getUrlObservable(path)
//                .map(url -> {
//                    System.out.println("Sending numValues: " + numValues);
//                    Observable<Integer> builder = asyncHttpClient.preparePost(url)
//                        .addHeader("Content-type", "application/json")
//                        .setBody(writeValue(req))
//                        .toObservable(() -> boundRequestBuilder);
//                    return builder;
//                })
//                .onErrorResumeNext(throwable -> {
//                    System.out.println("Error: " + throwable.getMessage());
//                    return Observable.empty();
//            }));

    }

    public static void main(String[] args) {

        try {
            StreamObservableEx01 tester = new StreamObservableEx01(new URI("http://localhost:8033"), new DefaultAsyncHttpClient());

            // Imperitive style thinking:
            //tester.processNumbers(20);

            // NOTE: HttpClient.java: Observable.just(url) ... so does this make sense?:
            Observable.just(20)
                    .flatMap(numValues -> tester.processNumbers(numValues))
                        // Tried map() but does not call .map() in processNumbers() -- why?
                    .doOnNext(response -> System.out.println(response))
                    .doOnCompleted(() -> {
                    })
                    .subscribe(response -> System.out.println("Process number done"));


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
