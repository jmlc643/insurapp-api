package com.upao.insurApp.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fields")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private int fieldId;

    @Column(name = "description")
    private String description;

    @Column(name = "typeField")
    private String typeField;

    @Column(name = "numberField")
    private int numberField;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTypeField(String typeField) {
        this.typeField = typeField;
    }

    public void setNumberField(int numberField) {
        this.numberField = numberField;
    }

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Reserve> reserves = new ArrayList<>();
}
