package com.android.designpattern.创建型.抽象工厂.abstractfactory;

import com.android.designpattern.创建型.抽象工厂.abstractproduct.AbstractProductBattery;
import com.android.designpattern.创建型.抽象工厂.abstractproduct.AbstractProductScreen;

public abstract class AbstractPhoneFactory {

    protected String brand;
    protected String model;
    protected AbstractProductScreen phoneScreen;
    protected AbstractProductBattery phoneBattery;

    public AbstractPhoneFactory(String brand, String model) {
        this.brand = brand;
        this.model = model;
        this.phoneScreen = createPhoneScreen();
        this.phoneBattery = createPhoneBattery();
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public AbstractProductScreen getPhoneScreen() {
        return phoneScreen;
    }

    public AbstractProductBattery getPhoneBattery() {
        return phoneBattery;
    }

    protected abstract AbstractProductScreen createPhoneScreen();

    protected abstract AbstractProductBattery createPhoneBattery();
}
