package com.king.wanandroid.app.comm;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@IntDef({Status.LOADING,Status.SUCCESS,Status.FAILURE,Status.ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface Status {
    int LOADING = 0;
    int SUCCESS = 1;
    int FAILURE = 2;
    int ERROR = 3;
}
