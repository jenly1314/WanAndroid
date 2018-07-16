package com.king.wanandroid.bean;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.king.wanandroid.app.comm.Status;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class Resource<T> {

    @Status
    public int status;

    @Nullable
    public String message;

    @Nullable
    public T data;

    public Throwable error;

    public Resource(@Status int status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public Resource(@Status int status,Throwable t) {
        this.status = status;
        this.error = t;
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> response(@Nullable BaseResult<T> data) {
        if(data!=null){
            if(data.isSuccess()){
                return new Resource<>(Status.SUCCESS, data.getData(), null);
            }
            return new Resource<>(Status.FAILURE,null,data.getErrorMsg());
        }
        return new Resource<>(Status.ERROR,null,null);
    }

    public static <T> Resource<T> failure(String msg) {
        return new Resource<>(Status.FAILURE, null, msg);
    }

    public static <T> Resource<T> error(Throwable t) {
        return new Resource<>(Status.ERROR,t);
    }

    public void handle(@NonNull OnHandleCallback<T> callback){
        switch (status){
            case Status.LOADING:
                callback.onLoading();
                break;
            case Status.SUCCESS:
                callback.onSuccess(data);
                break;
            case Status.FAILURE:
                callback.onFailure(message);
                break;
            case Status.ERROR:
                callback.onError(error);

                break;
        }

        if(status!= Status.LOADING){
            callback.onCompleted();
        }
    }

    @Override
    public String toString() {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }


    public interface OnHandleCallback<T>{
        void onLoading();
        void onSuccess(T data);
        void onFailure(String msg);
        void onError(Throwable error);
        void onCompleted();
    }
}
