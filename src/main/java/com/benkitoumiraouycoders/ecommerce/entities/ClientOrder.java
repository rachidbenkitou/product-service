package com.benkitoumiraouycoders.ecommerce.entities;

import com.benkitoumiraouycoders.ecommerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ClientOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    private String description;

    private LocalDateTime dateCreation;

    private LocalDateTime dateUpdate;

    private OrderStatus orderStatus;

    private Double totalPrice;

    @Column(name = "CLIENT_ID")
    private Long clientId;

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Client client;
}
