package com.example.com.example.rxjava;



/**
 * Created by robertwood on 5/29/17.
 */
public class SimpleObservable {
    String message;     // Must declare at class level to avoid lambda expression error: "message must be final or effectively final"

    public String getMessage_just_01() {
        message = "";
        Observable<String> observer = Observable.just("Hello");
        observer.subscribe(s -> message = s);
        return message;
    }

    // http://reactivex.io/documentation/operators/just.html   RxJava1.x
    //  Seems that Subscriber<> was deprecated with 2.x
//    public void getValues_just_01() {
//        Observable.just(1, 2, 3)
//                .subscribe(new Subscriber<Integer>() {
//                    //public void onSubscribe(Subscription )
//                    public void onNext(Integer item) {
//                        System.out.println("Next: item (" + item + ")");
//                    }
//                    public void onError(Throwable error) {
//                        System.out.println("Error: [" + error.getMessage() + "]");
//                    }
//                    public void onCompleted() {
//                        System.out.println("Done!");
//                    }
//                });
//    }


    private String getTheMessage() {
        return "Hello";
    }

    // http://reactivex.io/documentation/operators/start.html
//    public String getMessage_start() {
//        message = "";
//        Observable<String> observer = Observable.start(::getTheMessage);
//    }


    public static void main(String[] args) {
        SimpleObservable tester = new SimpleObservable();

        String message = tester.getMessage_just_01();
        System.out.println("Message: " + message);
    }
}
