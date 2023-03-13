package fr.cinema;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;
    private List<Short> years;
    private float price;

    @ElementCollection // 1
    
    @CollectionTable(name = "movie_times") // 2
    @Column(name = "times") // 3
    private List<String> times;

    public Movie() {
    }

    public Movie(Long id, String title, List<Short> years, float price, List<String> times) {
        this.id = id;
        this.title = title;
        this.years = years;
        this.price = price;
        this.times = times;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<Short> getYears() {
        return years;
    }
    public void setYears(List<Short> years) {
        this.years = years;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public List<String> getTimes() {
        return times;
    }
    public void setTimes(List<String> times) {
        this.times = times;
    }

    
}
