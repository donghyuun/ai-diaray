package com.example.springbackend.Controller;

import com.example.springbackend.DTO.BoardDto;
import com.example.springbackend.DTO.BoardModifyDto;
import com.example.springbackend.Entity.Board;
import com.example.springbackend.Exception.UserNotFoundException;
import com.example.springbackend.Service.BoardService;
import com.example.springbackend.Service.ImageGeneratorService;
import com.example.springbackend.Service.S3Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000")
public class BoardController {

    private final BoardService boardService;
    private final ImageGeneratorService imageGeneratorService;
    private final S3Service s3Service;

    public BoardController(BoardService boardService, ImageGeneratorService imageGeneratorService, S3Service s3Service) {
        this.boardService = boardService;
        this.imageGeneratorService = imageGeneratorService;
        this.s3Service = s3Service;
    }

    @PostMapping("/board/write")
    public ResponseEntity<Object> writeBoard(@RequestBody BoardDto boardDto) throws Exception {

        System.out.println("/board/write PostMapping");
        Board board = new Board();
        board.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setCreatedDate(new Date());
        board.setModifiedDate(new Date());

        //------------------//
        //content 로부터 이미지 Url 추출
        String imgUrl = imageGeneratorService.getAiImageUrl(board.getContent());
        // 이미지 Url 을 이용해 s3 에 이미지 저장
        String objUrl = s3Service.uploadImageFromUrlToS3(imgUrl);
        // board 엔티티에 s3에 저장된 이미지 객체의 주소 저장
        board.setImgUrl(objUrl);

        Board boardForReturn = boardService.post(board);
        System.out.println(board.getTitle());
        System.out.println(board.getContent());
        System.out.println(boardForReturn.getContent());
        System.out.println(boardForReturn.getTitle());
        System.out.println(boardForReturn.getImgUrl());

        // Creating HttpHeaders instance to add custom headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "Value"); // Add custom header


        headers.add("Access-Control-Expose-Headers", "Object-Url");
        headers.add("Object-Url", objUrl);
        //------------------//
        return ResponseEntity.ok().headers(headers).body(boardForReturn);
    }

    @GetMapping("/board")
    List<Board> getAllBoards() {
        System.out.println("/board getMapping");
        return boardService.findAll();
    }

    @GetMapping("/board/{id}")
    public Board showContent(@PathVariable("id") int id) {
        Optional<Board> boardContent = boardService.findById(id);
        if (boardContent.isEmpty()) System.out.println("게시글의 내용을 db에서 찾을 수 없습니다.");
        return boardContent.get();
    }

    @PostMapping("/modify/board/{id}")
    public Board modifyBoard(@PathVariable("id") int id, @RequestBody BoardModifyDto boardModifyDto) throws Exception {
        Board existBoardDto = boardService.findById(id).get();
        existBoardDto.setTitle(boardModifyDto.getTitle());
        existBoardDto.setContent(boardModifyDto.getContent());
        existBoardDto.setCreatedDate(new Date());
        //-------------그림 수정 동의 시 ------------//
        if (Objects.equals(boardModifyDto.getYesNoValue(), "No")) {
            String imgUrl = imageGeneratorService.getAiImageUrl(existBoardDto.getContent());
            // 이미지 Url 을 이용해 s3 에 이미지 저장
            String objUrl = s3Service.uploadImageFromUrlToS3(imgUrl);
            // board 엔티티에 s3에 저장된 이미지 객체의 주소 저장
            existBoardDto.setImgUrl(objUrl);
        }

        return boardService.modifyBoard(id, existBoardDto, boardModifyDto.getYesNoValue());
    }

    @DeleteMapping("/delete/board/{id}")
    public void deleteBoard(@PathVariable(name = "id") int id) {
        int deletedId = boardService.deleteById(id);
        System.out.println(deletedId + "번째 id의 일기 게시글이 삭제되었습니다.");
    }
}
