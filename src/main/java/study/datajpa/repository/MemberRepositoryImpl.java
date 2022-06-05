package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select  m from Member m")
                .getResultList();
    }
}


/**
 * 사용자 정의 레포지토리 네이밍
 * MemberRepository + Impl  : 네이밍 규칙 지키기!
 *
 * 클래스 분리 고민..!
 * 핵심 비지니스, 화면에 보여지는 쿼리
 * 커맨드, 쿼리
 */