package study.datajpa.entity;

import lombok.Data;

@Data
public class MemberDto {

    private Long id;
    private String userName;
    private String teamName;

    public MemberDto(Long id, String userName, String teamName) {
    }
}
