package com.study.board.repository;
//인터페이스로 생성
import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//디비에 저장하기 위한
@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
// jpa 리퍼지토리 상속 <어떤 엔티티, primary(id)(보드엔티티에 id부분)키로 지정한 변수 타입>
    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
//    검색 관련 정해진 "함수" JPA에서 제공...디비에 접근하기 때문?
//    함수임 JPA 파인드(컬럼이름)컨테이닝 타이틀에서 해당 키워드가 포함된 것을 찾겠다.
//    한코딩 검색할떄 한 만 써도 다나옴 컨테이닝 빼고 넣고 둘다 해보기
    
//    결국 JpaRepository 를 JPA 를 사용하는 것이므로 리포짓토리 어노테이션에서 선언

}
