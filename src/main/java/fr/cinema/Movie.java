package fr.cinema;

import java.util.List;

public record Movie(
    long id,
    String title,
    List<Short> years,
    float price,
    List<String> times
) {
}
