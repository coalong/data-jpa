package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    @Query("select m from Member m where m.userName = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // update, delete, insert  사용할 때 어노테이션 붙이기
    // clearAutomatically = true  벌크 연산은 자동으로 오토클리어 true 로 설정해도 좋다.
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();


    // 스프링 데이터 JPA
    Page<Member> findByAge(int age, Pageable pageable);

    List<Member> findByUserName(String username);

    // Fetch Join 설정을 Entity Graph로 설정 (Fetch Join의 간단 버전)
    @Override
    @EntityGraph(attributePaths = ("team"))
    List<Member> findAll();

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUserName(String userName);

    // select for update, 비관적 Lock (JPA 책에서 트랜잭션과 Lock 부분 참고)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUserName(String userName);
}
