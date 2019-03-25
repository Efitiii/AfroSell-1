package com.erihackton.shopping;

import android.os.Handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by aelaf on 2/5/19.
 */


public class UseCaseThreadPoolScheduler implements  UseCaseScheduler{

    private final Handler mHandler = new Handler();

    public static final int POOL_SIZE = 2;

    public static final int MAX_POOL_SIZE = 4;

    public static final int TIMEOUT = 30;
    ThreadPoolExecutor mThreadPoolExecutor;

    public UseCaseThreadPoolScheduler() {
        mThreadPoolExecutor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(POOL_SIZE));
    }

    @Override
    public void execute(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }

    @Override
    public <V extends UseCase.ResponseValue,S> void notifyResponse(final V response, final UseCase.UseCaseCallback<V,S> callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSucess(response);
            }
        });
    }

    @Override
    public <V extends UseCase.ResponseValue,S> void onError(final S err,final UseCase.UseCaseCallback<V,S> callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(err);
            }
        });
    }
}