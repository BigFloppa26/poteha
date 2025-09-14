package ru.poteha.rent.boat.boat.impl.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import ru.poteha.rent.boat.boat.model.BoatDesign;
import ru.poteha.rent.boat.boat.model.BoatMaterial;
import ru.poteha.rent.boat.boat.model.BoatType;
import ru.poteha.rent.boat.comment.impl.jpa.Comment;
import ru.poteha.rent.user.impl.jpa.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "APP_BOAT")
public class Boat {

    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime modified;

    @ManyToOne
    private User owner;

    private String name;
    private String brand;
    private boolean enabled;
    private String description;

    private double latitude;
    private double longitude;

    private Integer maxLoad;
    private Integer capacity;

    private Integer buildYear;
    private Integer retrofitYear;

    @Embedded
    private BoatSize size;

    @Enumerated(EnumType.STRING)
    private BoatType type;

    @Enumerated(EnumType.STRING)
    private BoatDesign design;

    @Enumerated(EnumType.STRING)
    private BoatMaterial material;

    @OneToMany(mappedBy = "boat")
    private List<Comment> comments;

    @ElementCollection
    @JoinTable(name = "APP_BOAT_OPTION")
    private List<BoatOption> options = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Formula("(select array_agg(abo.option_id) from app_boat_option abo where abo.boat_id = id and include = true)")
    private List<UUID> optionIncludes;
}
