package com.king.wanandroid.app.comm;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@IntDef({Tree.SYSTEM,Tree.PROJECT,Tree.SEARCH})
@Retention(RetentionPolicy.SOURCE)
public @interface Tree {
    int SYSTEM = 0;
    int PROJECT = 1;
    int SEARCH = 2;
}
