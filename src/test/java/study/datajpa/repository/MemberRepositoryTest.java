package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.MemberDto;
import study.datajpa.exception.MemberNotFound;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow(MemberNotFound::new);

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUserName()).isEqualTo(member.getUserName());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get(); Member findMember2 = memberRepository.findById(member2.getId()).get(); assertThat(findMember1).isEqualTo(member1); assertThat(findMember2).isEqualTo(member2);
        //리스트 조회 검증
        List<Member> all = memberRepository.findAll(); assertThat(all.size()).isEqualTo(2);
        //카운트 검증
        long count = memberRepository.count(); assertThat(count).isEqualTo(2);
        //삭제 검증 memberRepository.delete(member1); memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void paging() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "userName"));

        //when
        // Member를 그대로 반환하지 말고 DTO로 바꿔서 보낸다. Entity를 외부로 노출시키면 X
        Page<Member> page = memberRepository.findByAge(10, pageRequest);
        // EX) DTO로 변환하고 api에 반환
        Page<MemberDto> memberDtoPage = page.map(member -> new MemberDto(member.getId(), member.getUserName(), null));


        //then
        List<Member> content = page.getContent();
//        long totalElements = page.getTotalElements();

//        for (Member member : content) {
//            System.out.println("member = " + member);
//        }
//        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);              // 컨텐트 갯수
        assertThat(page.getTotalElements()).isEqualTo(5);     // 전체 엘리먼트 갯수
        assertThat(page.getNumber()).isEqualTo(0);            // 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2);        // 전체 페이지 갯수
        assertThat(page.isFirst()).isTrue();                           // 첫번째 페이지 여부
        assertThat(page.hasNext()).isTrue();                           // 다음 페이지 여부
    }

    @Test
    public void bulkUpdate() {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        // 벌크 연산은 영속성 컨텍스트를 무시하고 DB에 바로 쿼리 날린다. 따라서 벌크 연산 하고 나서는 영속성 컨택스트 초기화해야한다.
        em.flush();
        em.clear();
        // 위에 초기화 유무에 따라서 아래 예시 결과 확인해보기
        List<Member> result = memberRepository.findByUserName("members");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberCustom() {
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }

}
