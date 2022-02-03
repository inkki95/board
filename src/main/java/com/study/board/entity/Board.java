package com.study.board.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
// 테이블이란 뜻
// 이 클래스가 DB에 있는 테이블이다 라는 뜻
@Data
//롬복 기능. @Data정의하면 컨트롤러에서 Board board인 board.getTittle()같은것 사용 가능
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
// 컬럼들 정의
    private String title;

    private String content;

    private String filename;
    private String filepath;
    //    마리아디비 컬럼을 추가했기떄문에 이것도 추가

}
