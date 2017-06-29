package com.example.com.example.rxjava;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import org.apache.http.client.utils.URIBuilder;
import rx.Observable;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Test Comment Number 2

// Simple update to test

/*
{TODO: Study: java.util.Optional }

 */

/**
 * Created by robertwood on 6/24/17.
 */
public abstract class HttpClient {
    private final URI baseUri;

    protected HttpClient(URI baseUri) {
        this.baseUri = baseUri;
    }

    public Observable<String> getUrlObservable(Stream<String> path) {
        return Optional.of(path)
            .map(p -> {
                final URIBuilder uriBuilder = new URIBuilder(baseUri);
                uriBuilder.setPath(path.collect(Collectors.joining("/")));
                final String url;
                try {
                    url = uriBuilder.build().toString();
                } catch (URISyntaxException exp) {
                    return Observable.<String>error(exp);
                }
                System.out.println("URL: " + url);
                return Observable.just(url);
            }).orElse(Observable.empty());

    }

    static class HystrixCommand<T> extends HystrixObservableCommand<T> {
        private final Observable<T> observable;

        HystrixCommand(String commandName, Observable<T> observable) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(HttpClient.class.getSimpleName()))
                .andCommandKey(HystrixCommandKey.Factory.asKey(commandName))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    .withExecutionTimeoutEnabled(false)
                    .withExecutionTimeoutInMilliseconds(15000)
                    .withExecutionIsolationSemaphoreMaxConcurrentRequests(10000)
                    .withFallbackEnabled(true)
                    .withCircuitBreakerEnabled(true)));
            this.observable = observable;
        }

        @Override
        protected Observable<T> construct() {
            return observable;
        }
    }
}
