package org.example.route;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.functions.Function;

import java.util.concurrent.TimeoutException;

public class RxUtil {
    public static <T> Function<Throwable, SingleSource<T>> mapCheckedTimeoutException() {
        //RX wraps checked exceptions in RuntimeException so the catch block does not catch intended exceptions.
        //So we convert checked exceptions to rest exceptions before their emission.
        return throwable -> throwable instanceof TimeoutException
                ? Single.error(new RuntimeException(throwable))
                : Single.error(throwable);
    }

    private RxUtil() {
    }
}
