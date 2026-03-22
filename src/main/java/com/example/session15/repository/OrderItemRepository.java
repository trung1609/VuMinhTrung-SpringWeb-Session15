package com.example.session15.repository;

import com.example.session15.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select count(oi) > 0 from OrderItem oi where " +
            "oi.order.user.id = :userId " +
            "and oi.product.id = :productId " +
            "and oi.order.status = 'COMPLETED'")
    boolean existsByUsersAndProduct(@Param("userId") Long userId,@Param("productId") Long productId);

}
