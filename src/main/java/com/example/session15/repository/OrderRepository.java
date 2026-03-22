package com.example.session15.repository;

import com.example.session15.dto.response.RevenueResponse;
import com.example.session15.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserId(Long userId, Pageable pageable);

    @Query("""
                select new com.example.session15.dto.response.RevenueResponse(
                    to_char(o.createdDate, 'DD/MM/YYYY'),
                    sum (o.totalMoney)
                )
                from Order o
                where o.status = 'COMPLETED'
                group by date(o.createdDate),  to_char(o.createdDate, 'DD/MM/YYYY')
                order by date(o.createdDate)
            """)
    List<RevenueResponse> getRevenueByDay();

    @Query("""
                select new com.example.session15.dto.response.RevenueResponse(
                    to_char(o.createdDate, 'MM/YYYY'),
                    sum (o.totalMoney)
                )
                from Order o
                where o.status = 'COMPLETED'
                group by to_char(o.createdDate, 'MM/YYYY')
                order by to_char(o.createdDate, 'MM/YYYY')
            """)
    List<RevenueResponse> getRevenueByMonth();

    @Query("""
                select new com.example.session15.dto.response.RevenueResponse(
                    to_char(o.createdDate, 'YYYY'),
                    sum (o.totalMoney)
                )
                from Order o
                where o.status = 'COMPLETED'
                group by to_char(o.createdDate, 'YYYY')
                order by to_char(o.createdDate, 'YYYY')
            """)
    List<RevenueResponse> getRevenueByYear();
}
