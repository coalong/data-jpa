package study.datajpa.exception;

public class MemberNotFound extends IllegalArgumentException{
    public MemberNotFound() {
        super("멤버 정보가 없습니다.");
    }
}
