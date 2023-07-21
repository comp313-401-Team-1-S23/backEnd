package com.meplus.meplusartifact.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "diaries")
@Getter
@Setter
@ToString
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @ManyToOne()
    @JoinColumn(name="user_id")
    private User user;

    private String title;

    private String content;

    public Diary() {}


    public Diary(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
