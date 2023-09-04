package com.example.onlinebookstore.model;

import com.example.onlinebookstore.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@SQLDelete(sql = "UPDATE order SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    @Column(nullable = false)
    private BigDecimal total;
    @Column(name ="shipping_address",nullable = false)
    private String shippingAddress;
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems;
    @Column(nullable = false)
    private boolean isDeleted;
}
