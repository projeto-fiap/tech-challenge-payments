package tech.fiap.project.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.fiap.project.infra.entity.ItemEntity;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

}