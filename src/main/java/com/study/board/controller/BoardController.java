package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class BoardController {


    @Autowired
    private BoardService boardService;
//    컨트롤러 입장에서는 보드서비스가 뭔지 모름. 선언해주기 오토와이어 활용해서

    @GetMapping("/board/write") //localhost:8090/board/write
    public String boardWriteForm() {

        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception {
//boardwrite.html에서 title이랑 content로 넘겨줬는데 public String boardWritePro(String title, String contnet){
//        로 가져와서 자바 코드처럼 코딩 가능 (객체로)
//        이후에는 엔티티로 만든 폼(Board 형식으로 한번에 가져옴)
//        예외처리, 파일받기
        boardService.write(board, file);
//        서비스의 보드서비스 구문을 가져온것

        model.addAttribute("message", "글 작성이 완료되었습니다.");
//        새로운창, 창 닫고 갈 주소
        model.addAttribute("searchUrl", "/board/list");

// html 파일 message임
        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
//                            페이져블 (도메인 인터페이스꺼) 선언
//                            어노테이션으로 페이지 및 사이즈 및 정렬 가능
//                            DESC 정렬 역순으로 정렬 (최신글이 맨위로)
                            String searchKeyword) {
//                          검색 키워드
        Page<Board> list = null;
//      페이지 클래스에 보드 제네릭으로 감싼 리스트 변수
        if (searchKeyword == null) {
//            검색어가 안들어왔을때 그대로 던져줌
            list = boardService.boardList(pageable);
//            보드서비스에 페이져블 형태로 던져줌
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);
//            검색어가 들어왓을때는 보드서치리스트로 넘겨줌
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
//        0페이지 부터 시작이므로 1을 더해야함
        int startPage = Math.max(nowPage - 4, 1);
//        1이거나 page-4 둘중에 큰 수를 반환
        int endPage = Math.min(nowPage + 5, list.getTotalPages());
//        전체 페이지랑 page+5 둘중에 작은 수를 반환
        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }

    @GetMapping("/board/view") // localhost:8080/board/view?id=1
    public String boardView(Model model, Integer id) {

        model.addAttribute("board", boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {
// pathVariable을 하면 주소의 board/modify/{id} 에서 {id} 부분이 @PathVariable("id") Integer id
//        에서 인티저 id로 변환되어 들어온다는 소리
//        주소창에 ?id=2 이런식이 아니라 /2 이렇게 깔끔하게
        model.addAttribute("board", boardService.boardView(id));

        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, MultipartFile file) throws Exception {

        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp, file);

        return "redirect:/board/list";

    }

//    보드딜리드 주소로 요청이 오면 넘어온 id값을 보드서비스 함수(서비스 파일)로 가서 딜리트 함수 해서 리턴
    @GetMapping("board/delete")
//    실험방법은 localhost8080/board/delete?id=5 이런식으로
    public String boardDelete(Integer id) {
        boardService.boardDelete(id);
        return "redirect:/board/list";
    }
}
