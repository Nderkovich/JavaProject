package com.derkovich.springdocuments.service.dto;

import com.derkovich.springdocuments.service.utils.JsonSerializers.DocumentView;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @Column(name = "document_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(DocumentView.Simple.class)
    private Integer id;
    @Column(name = "document_name")
    @JsonView(DocumentView.Simple.class)
    @Size(min = 1, max = 100)
    private String name;
    @JsonView(DocumentView.Simple.class)
    @Size(min = 1)
    private String description;

    public Document(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "document")
    private Set<Rating> ratings;

    @JsonManagedReference
    @JsonView(DocumentView.Detailed.class)
    @OneToMany(mappedBy = "document")
    private Set<Comment> comments;

    @Transient
    @JsonView(DocumentView.Simple.class)
    private Double rating;

    public Set<Rating> getRatings() {
        return ratings;
    }

    @PostLoad
    public void getRating() {
        double rate = 0.0;
        if (this.ratings != null){
            if (this.ratings.size() != 0){
                for (Rating r: this.ratings) {
                    if (r.getDocument().getId() == id)
                        rate += r.getRating();
                }
                this.rating = (rate / this.ratings.size());
                return;
            }
            this.rating = rate;
        }
        this.rating = rate;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Document() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
