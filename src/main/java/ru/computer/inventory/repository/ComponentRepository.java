package ru.computer.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.computer.inventory.entity.Component;

import java.util.List;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {

    List<Component> findAllByCategory(String category);

    List<Component> findAllByQuantityLessThan(Integer quantity);

    @Query(value = "SELECT * FROM components c WHERE " +
            "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', CAST(:name AS text), '%'))) AND " +
            "(:category IS NULL OR c.category = CAST(:category AS text)) AND " +
            "(:minPrice IS NULL OR c.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR c.price <= :maxPrice)",
            nativeQuery = true)
    List<Component> findByFilters(@Param("name") String name,
                                  @Param("category") String category,
                                  @Param("minPrice") Double minPrice,
                                  @Param("maxPrice") Double maxPrice);

}
