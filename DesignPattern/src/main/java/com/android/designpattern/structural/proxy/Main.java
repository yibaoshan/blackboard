package com.android.designpattern.structural.proxy;


import com.android.designpattern.structural.proxy.sample.proxy.ProxyStack;

import org.junit.Test;

public class Main {

    @Test
    public void main() {
        ProxyStack stack = new ProxyStack();
        for (int i = 0; i < 10; i++) {
            stack.push(i);
        }
        while (!stack.empty()) {
            System.out.println(stack.pop() + "," + stack.size());
        }
    }

}
