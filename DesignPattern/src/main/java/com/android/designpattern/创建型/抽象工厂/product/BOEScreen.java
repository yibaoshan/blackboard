package com.android.designpattern.创建型.抽象工厂.product;

import com.android.designpattern.创建型.抽象工厂.abstractproduct.AbstractScreen;

public class BOEScreen extends AbstractScreen {

    @Override
    public String getScreenName() {
        return "京东方(BOE)";
    }

    @Override
    protected void createScreen() {
        System.out.println("加班加点生产京东方屏幕中...");
    }

}
