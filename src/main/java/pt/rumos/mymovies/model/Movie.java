package pt.rumos.mymovies.model;

import jakarta.persistence.*;

@Entity                     // Identifica a classe como entidade gerida pelo JPA
@Table(name = "movies")     // Associa a entidade à tabela "movies"
public class Movie {

    @Id     // Identifica a chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // Define como o identificador é gerado
    private Long id;

    @Column(name = "title", nullable = false, length = 120)
    private String title;

    @Column(nullable = false)   // Configurações da coluna correspondente ao atributo
    private String director;

    @Column(name = "release_year", nullable = false)
    private Integer year;

    @Column(name = "rating")    // Redundante, o JPA mapearia automaticamente a coluna pelo nome da variável
    private Double rating;

    /* --- Sobrecarga de Construtores: ---
    A mesma classe pode ter vários construtores, desde que tenham listas de parâmetros diferentes. */

    // Construtor sem argumentos
    public Movie() {
        // Necessário para o JPA conseguir instanciar entidades sem fornecer todos os atributos antecipadamente.
        // Por exemplo, quando lê um registo da base de dados, o Hibernate pode inicialmente criar um objeto Movie e só depois preencher os seus atributos.
    }

    // Construtor Completo
    public Movie(
            Long id,
            String title,
            String director,
            Integer year,
            Double rating) {

        this.id = id;
        this.title = title;
        this.director = director;
        this.year = year;
        this.rating = rating;
    }

    // Construtor sem parâmetro "rating"
    public Movie(
            Long id,
            String title,
            String director,
            Integer year) {

        this(id, title, director, year, null);
    }

    // ----- GETTERS-----
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public Integer getYear() {
        return year;
    }

    public Double getRating() {
        return rating;
    }

    // ----- SETTERS -----
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
