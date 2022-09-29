package com.android.blog.designpattern.creational.abstractfactory.abstractproduct;

public abstract class AbstractProductScreen {

    public AbstractProductScreen() {
        createScreen();
    }

    public abstract String getScreenName();

    protected abstract void createScreen();

}
