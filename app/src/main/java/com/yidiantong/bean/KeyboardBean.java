package com.yidiantong.bean;

public class KeyboardBean {

    private String keyText;
    private String hintText;

    public String getKeyText() {
        return keyText;
    }

    public void setKeyText(String keyText) {
        this.keyText = keyText;
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    @Override
    public String toString() {
        return "InputKeyboardBean{" +
                "keyText='" + keyText + '\'' +
                ", hintText='" + hintText + '\'' +
                '}';
    }
}
