package pt.rumos.mymovies.model;

public record Movie(
        Long id,
        String title,
        String director,
        Integer year,
        Double rating
) {
    // Quando só são passados 4 argumentos, é chamado este construtor;
    public Movie(
            Long id,
            String title,
            String director,
            Integer year) {

        this(id, title, director, year, null);
    }
}
