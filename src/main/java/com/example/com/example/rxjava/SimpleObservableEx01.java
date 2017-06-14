package com.example.com.example.rxjava;


import rx.Observable;
import rx.Subscriber;

/**
 * Created by robertwood on 5/29/17.
 */
public class SimpleObservableEx01 {
    String message;     // Must declare at class level to avoid lambda expression error: "message must be final or effectively final"

    public String getMessage_just_01() {
        message = "";
        Observable<String> observer = Observable.just("Hello");
        observer.subscribe(s -> message = s);
        return message;
    }

    // http://reactivex.io/documentation/operators/just.html   RxJava1.x
    public void getValues_just_01() {
        Observable.just(1, 2, 3)
                .subscribe(new Subscriber<Integer>() {
                    //public void onSubscribe(Subscription )
                    public void onNext(Integer item) {
                        System.out.println("Next: item (" + item + ")");
                    }
                    public void onError(Throwable error) {
                        System.out.println("Error: [" + error.getMessage() + "]");
                    }
                    public void onCompleted() {
                        System.out.println("Done!");
                    }
                });
    }

    // {TODO: Add example: forced Throwable}

    // {TODO: Add example: random wait times; using Thread.sleep() ?;  log profiling to validate wait times }
    // https://stackoverflow.com/questions/33948441/rxjava-thread-sleep-interrupted-exception
    // https://stackoverflow.com/questions/32915351/rxjava-observable-delay-work-strange-lacks-some-items-at-the-end

    private String getTheMessage() {
        return "Hello";
    }



    public static void main(String[] args) {
        SimpleObservableEx01 tester = new SimpleObservableEx01();

        String message = tester.getMessage_just_01();
        System.out.println("Message: " + message);

        tester.getValues_just_01();
    }
}
