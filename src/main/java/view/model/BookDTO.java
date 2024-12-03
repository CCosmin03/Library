package view.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class BookDTO {
    private StringProperty author;
    private StringProperty title;
    private IntegerProperty stock;
    private DoubleProperty price;
    private ObjectProperty<LocalDate> publishedDate;

    public void setAuthor(String author) {
        authorProperty().set(author);
    }

    public StringProperty authorProperty() {
        if (author == null) {
            author = new SimpleStringProperty(this, "author");
        }
        return author;
    }

    public String getAuthor() {
        return authorProperty().get();
    }

    public void setTitle(String title) {
        titleProperty().set(title);
    }

    public StringProperty titleProperty() {
        if (title == null) {
            title = new SimpleStringProperty(this, "title");
        }
        return title;
    }
    public String getTitle() {
        return titleProperty().get();
    }

    public void setStock(int stock) {
        stockProperty().set(stock);
    }

    public IntegerProperty stockProperty() {
        if (stock == null) {
            stock = new SimpleIntegerProperty(this, "stock");
        }
        return stock;
    }

    public int getStock() {
        return stockProperty().get();
    }

    public void setPrice(double price) {
        priceProperty().set(price);
    }

    public DoubleProperty priceProperty() {
        if (price == null) {
            price = new SimpleDoubleProperty(this, "price");
        }
        return price;
    }
    public double getPrice() {
        return priceProperty().get();
    }
    public LocalDate getPublishedDate() {
        return publishedDateProperty().get();
    }

    public void setPublishedDate(LocalDate publishedDate) {
        publishedDateProperty().set(publishedDate);
    }

    public ObjectProperty<LocalDate> publishedDateProperty() {
        if (publishedDate == null) {
            publishedDate = new SimpleObjectProperty<>(this, "publishedDate");
        }
        return publishedDate;
    }

}
