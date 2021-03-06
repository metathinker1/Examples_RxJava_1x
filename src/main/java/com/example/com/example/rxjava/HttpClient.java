package com.example.com.example.rxjava;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.apache.http.client.utils.URIBuilder;
import rx.Observable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

    // {TextLink:URL:https://stackoverflow.com/questions/5837698/converting-any-object-to-a-byte-array-in-java}
    // {TextLink:URL:https://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array}
    private byte[] convertObjectToByteArray(Object value) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(value);
            }
            return b.toByteArray();
        }
    }

    byte[] writeValue(Object value) {
        try {
            return convertObjectToByteArray(value);
        } catch (Exception excp) {
            throw new HystrixRuntimeException(HystrixRuntimeException.FailureType.COMMAND_EXCEPTION, null, "Bad format?", excp, null);
        }
    }

    public<T> Observable<T> getCommand(String commandName, Observable<T> observable) {
        return new HystrixCommand<>(commandName, observable).toObservable();
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
