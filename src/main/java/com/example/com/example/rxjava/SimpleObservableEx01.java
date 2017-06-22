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
    public void example00() {
        System.out.println("\nStart: example00");
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

    public void example01() {
        System.out.println("\nStart: example01");
        Observable<Integer> obs = Observable.create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            //  Only useful if Subscriber implements onCompleted(),
            //    but lambda implementation below seems to only implement onNext()
            //s.onCompleted();
        });

        obs.map(i -> "Number: " + i)
                .subscribe(s -> System.out.println(s));

        // Verify: can "reuse" an Observable
        obs.map(i -> "Round 2 Number: " + i)
                .subscribe(s -> System.out.println(s));
    }

    public void example02() {
        System.out.println("\nStart: example02");
        Observable<Integer> obs = Observable.create(s -> {
            for (int ix = 0; ix < 4; ix++) {
                s.onNext(ix);
            }
            //s.onCompleted();
        });

        obs.subscribe(s -> System.out.println(s));
    }

    public void example03() {
        System.out.println("\nStart: example03");
        Observable<Integer> obs = Observable.create(s -> {
            for (int ix = 0; ix < 10; ix++) {
                s.onNext(ix);
            }
            //s.onCompleted();
        });

        obs.filter(i -> i % 2 == 0)
                .subscribe(s -> System.out.println(s));
    }

    public void example04() {
        System.out.println("\nStart: example04");
        Observable<Integer> obs = Observable.create(s -> {
            for (int ix = 0; ix < 10; ix++) {
                s.onNext(ix);
            }
            s.onCompleted();
        });

        obs.filter(i -> i % 2 == 0)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("End: example04\n");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("Error: " + throwable.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("Number: " + integer);
                    }
                });
    }

    public void example05() {
        System.out.println("\nStart: example05");
        Observable<Integer> obs = Observable.create(s -> {
            for (int ix = 0; ix < 10; ix++) {
                s.onNext(ix);
            }
            s.onCompleted();
        });

        obs.filter(i -> i % 2 == 0)
                .map(i -> "Mapped Number " + i)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("End: example05\n");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("Error: " + throwable.getMessage());
                    }

                    @Override
                    public void onNext(String text) {
                        System.out.println(text);
                    }
                });
    }

    public static void main(String[] args) {
        SimpleObservableEx01 tester = new SimpleObservableEx01();

        String message = tester.getMessage_just_01();
        System.out.println("Message: " + message);

        tester.example00();

        tester.example01();

        tester.example02();

        tester.example03();

        tester.example04();

        tester.example05();
    }
}
