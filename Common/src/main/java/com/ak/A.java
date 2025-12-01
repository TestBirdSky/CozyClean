package com.ak;

import com.demo.dex.EventImpl;

/**
 * Date：2025/11/4
 * Describe:
 * com.ak.A
 */
public class A {

    // 这个类需要keep 因为admin 里面需要
    public static com.ak.c o() {
        return new EventImpl();
    }

}
