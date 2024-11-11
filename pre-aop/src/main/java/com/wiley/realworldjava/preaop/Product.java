package com.wiley.realworldjava.preaop;

import java.util.Objects;

public class Product {
    private String styleNum;
    private String description;

    public Product() {
    }

    public Product(String styleNum, String description) {
        this.styleNum = styleNum;
        this.description = description;
    }

    public String getStyleNum() {
        return styleNum;
    }

    public void setStyleNum(String styleNum) {
        this.styleNum = styleNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "styleNum='" + styleNum + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) {
            return true;
        }
        if(o==null || getClass()!=o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(getStyleNum(), product.getStyleNum()) && Objects.equals(getDescription(), product.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStyleNum(), getDescription());
    }
}
