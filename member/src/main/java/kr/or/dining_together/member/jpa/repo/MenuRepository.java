package kr.or.dining_together.member.jpa.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.or.dining_together.member.jpa.entity.Menu;
import kr.or.dining_together.member.jpa.entity.Store;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	List<Menu> findMenusByStore(Store store);

}
