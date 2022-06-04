package study.datajpa.entity;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    // 영속성 context Annotation이  EntityManager를 주입 받아온다.
    @PersistenceContext
    EntityManager em;

    @Test
    public void entityTest() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("corock", 5, teamA);
        Member member2 = new Member("coalong", 15, teamA);
        Member member3 = new Member("cofreeman", 25, teamB);
        Member member4 = new Member("cororing", 35, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 영속성 컨텍스트에 쌓인 쿼리 실제 DB에 날린다.
        em.flush();
        // 영속성 컨텍스트 캐시를 지워버린다.
        em.clear();

        // 자바 JPA 쿼리로 조회
        List<Member> members = em.createQuery(
                "SELECT m FROM Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("-> member.team = " + member.getTeam());
        }
    }

}
