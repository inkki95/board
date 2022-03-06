package com.study.board.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.SystemProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static jdk.nashorn.internal.objects.Global.print;

@Service
// 로직 알고리즘 처리하는 곳
public class BoardService {

    @Autowired
//    BoardRepository boardRepository = new BoardRepository 이런식으로 해야하지만
//    오토와이어드를 하면 스프링 빈이 와서 알아서 주입을 해줌
    private BoardRepository boardRepository;

    // 글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception{
//        throws Exception  예외 처리.
// MultipartFile file 파일을 받기 위한 구문
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
//System.getProperty("user.dir")하면 우리의 프로젝트 경로를 담아줌 뒤에 추가로 더 들어가기
//        저 경로에 파일을 저장함
        if(file!=null) {
            // 일반 글쓰기랑 수정이랑 겹쳐서.. 수정할때는 파일업로드가 불가능
            // 수정할때는 파일이 안들어가므로 파일 변수에 null이 들어감
            UUID uuid = UUID.randomUUID();
            //uuid는 식별자...랜덤유유아이디 랜덤으로 파일 네임을 만들기
            //        System.out.print(uuid);
            String fileName = uuid + "_" + file.getOriginalFilename();
            //랜덤으로 식별자가 붙고 _ 붙고 파일이름이 붙어서 전체 파일명
            if (!file.isEmpty()) {
                File saveFile = new File(projectPath, fileName);
                file.transferTo(saveFile);
                //        경로에 이름은 이렇게로 저장해라.
                board.setFilename(fileName);
                board.setFilepath("/files/" + fileName);
            }
            if (file.isEmpty()) {
                fileName = "X.jpg";
                File saveFile = new File(projectPath, fileName);
                file.transferTo(saveFile);
                board.setFilename(null);
                board.setFilepath(null);
            }
            }
        boardRepository.save(board);
    }

    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {
//  Page클래스로 받기 페이져블이 들어오니까 페이져블없엇으면 리스트 클래스로 받음
        return boardRepository.findAll(pageable);
    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    // 특정 게시글 불러오기
    public Board boardView(Integer id) {

        return boardRepository.findById(id).get();
    }

    public void boardDelete(Integer id) {

        boardRepository.deleteById(id);
    }

}
