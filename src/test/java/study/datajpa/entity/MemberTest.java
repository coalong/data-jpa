package study.datajpa.entity;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;

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

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

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

    @Test
    public void findMemberLazy() throws Exception {
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        em.flush();
        em.clear();

        //when
        //지연 로딩 실행 될 때 (N + 1 문제 발생)
        //team의 데이터 조회할 때마다 쿼리가 실행
//        List<Member> members = memberRepository.findAll();
        //join fetch하는 쿼리문 실행 (연관된 엔티티를 한번에 조회할 때 필요)
        List<Member> members = memberRepository.findMemberFetchJoin();

        //then
        for (Member member : members) {
            member.getTeam().getName();
        }
    }

}
