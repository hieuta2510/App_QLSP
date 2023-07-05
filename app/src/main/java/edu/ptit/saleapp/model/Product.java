package edu.ptit.saleapp.model;

public class Product {
    private String key, name, price, category, desc, image;

    public Product() {
    }

    public Product(String key, String name, String price, String category, String des, String image) {
        this.key = key;
        this.name = name;
        this.price = price;
        this.category = category;
        this.desc = des;
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImg(String img) {
        this.image = img;
    }
}
