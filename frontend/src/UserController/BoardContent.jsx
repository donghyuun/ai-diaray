import React, {useEffect, useState} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';
import {json, useNavigate, useParams} from "react-router-dom";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import useStore from "../store";
// ES6
import * as Vibrant from 'node-vibrant'
//html to png 변환
import * as htmlToImage from 'html-to-image';
import axiosInstance from "../Axios/AxiosInstance";
import {queries} from "@testing-library/react";
import board from "./Board";

export default BoardContent;

function BoardContent() {

    const {id} = useParams();//id값 가져오기

    // zustand 를 이용한 전역 상태 사용&관리
    const {isLogined, setIsLogined} = useStore(state => state);
    const {username, setUsername} = useStore(state => state);
    const {userId, setUserId} = useStore(state => state);
    const {role, setRole} = useStore(state => state);


    // useState 훅
    const [boardContent, setBoardContent] = useState([]);
    //게시글 수정, 삭제
    const [showModalEditBoardContent, setshowModalEditBoardContent] = useState(false);
    const [showModalDeleteBoardContent, setshowModalDeleteBoardContent] = useState(false);
    //댓글 수정, 삭제
    const [showModalEditComment, setshowModalEditComment] = useState({});
    const [showModalDeleteComment, setshowModalDeleteComment] = useState({});

    // 댓글 작성
    const [comment, setComment] = useState("")

    // 댓글 리스트
    const [comments, setComments] = useState([]);

    // 전환용
    const navigate = useNavigate();


    //**************게시글 수정, 삭제 모달창 "열기"**************//
    const handleEditBoardContent = () => {
        setshowModalEditBoardContent(true);
    };
    const handleDeleteBoardContent = () => {
        setshowModalDeleteBoardContent(true);
    };
    //***********************************//


    //**************댓글 수정, 삭제 모달창 "열기"**************//
    const handleEditComment = (commentId, commentContent) => {
        setshowModalEditComment({tf: true, commentId: commentId, commentContent: commentContent});
    };
    const handleDeleteComment = (commentId) => {
        setshowModalDeleteComment({tf: true, commentId: commentId});
    };

    //**********댓글, 게시글 수정, 삭제 모달창 "닫기"**********//
    const handleCloseModal = () => {
        // 게시글 모달창
        setshowModalEditBoardContent(false);
        setshowModalDeleteBoardContent(false);
        // 댓글 모달창
        setshowModalEditComment(prevState => ({...prevState, tf: false}));
        setshowModalDeleteComment(false);
    };

    //************** 댓글 수정, 삭제 제출 함수 **************//
    const onEditCommentSubmit = async (e) => {
        e.preventDefault();
        const data = {
            boardId: boardContent.id, // 필요없음
            commentId: showModalEditComment.commentId,
            content: commentModiContent
        }
        console.log("댓글 수정 내용 data: " + data);
        const token = localStorage.getItem("key");
        const result = await axiosInstance(userId, role, username).post(`http://localhost:8080/modify/comment`, data)
        console.log('댓글 수정 완료입니다.');

        handleCloseModal();
    };

    const onDeleteCommentSubmit = async (commentId) => {
        console.log(id);
        await axios.delete(`http://localhost:8080/delete/comment/${commentId}`);
        console.log('Board Deleted');

        //댓글 다시 불러오기
        getComments(id);
    }
    //***********************************//

    //****************게시글 입력 함수*************//
    const onInputChange = (e) => {
        console.log(e.target.name);
        console.log(e.target.value);
        setBoardContent({...boardContent, [e.target.name]: e.target.value});
    };

    //**************댓글 작성 함수****************//
    const onCommentInputChange = (e) => {
        setComment(e.target.value);
    };


    //************ 게시글 수정 제출 함수**************//
    const onEditSubmit = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem("key");
        const result = await axiosInstance(userId, role, username).post(`http://localhost:8080/modify/board/${id}`, boardContent)
        getBoardContent(id);

        console.log('Board modified');
        // setmessage('New post Added');
        // setalertColor("success")
        // getBoards(); // Fetch users again after adding a new user
        handleCloseModal();
    };

    //************ 게시글 삭제 제출 함수**************//
    const onDeleteSubmit = async () => {
        console.log(id);
        await axios.delete(`http://localhost:8080/delete/board/${id}`);
        // setmessage('Board deleted');
        // setalertColor('warning')
        console.log('Board Deleted');
        // getBoards(); // Fetch users again after adding a new user

        //삭제 후 일기 게시판으로 이동
        navigate("/board");
    }

    //********** 게시글 내용 불러오기 ***********//
    const getBoardContent = async (id) => {
        const result = await axios.get(`http://localhost:8080/board/${id}`);
        await setBoardContent(JSON.parse(JSON.stringify(result.data)));
        console.log("result.data: " + JSON.stringify(result.data));
    };

    //********** 게시글 댓글들 불러오기 *********//
    const getComments = async (id) => {
        const result = await axios.get(`http://localhost:8080/comments/${id}`); //게시글 id로 가져옴
        await setComments(JSON.parse(JSON.stringify(result.data)));
        console.log("getComments result.data: " + JSON.stringify(result.data, null, 2));
    }

    //********* db에서 컨텐츠 가져오기 *********//
    useEffect(() => {
        const fetchData = async () => {
            await getBoardContent(id);
            await getComments(id);
        }
        fetchData()

    }, []);


    //rgb 색상값을 HEX 값으로 변환
    const [backgroundColorCode, setBackgroundColorCode] = useState("")

    function ColorToHex(color) {
        var hexadecimal = parseInt(color).toString(16);
        console.log(hexadecimal);
        return hexadecimal.length === 1 ? '0' + hexadecimal : hexadecimal;
    }

    async function ConvertRGBtoHex(red, green, blue) {
        return '#' + ColorToHex(red) + ColorToHex(green) + ColorToHex(blue);
    }

    const containerStyle = {
        backgroundImage: `linear-gradient(to bottom, ${backgroundColorCode}, white)`,
        padding: "30px",
        display: "flex"
    };

    async function htmlToImg() {
        const image = new Image();
        image.crossOrigin = 'Anonymous'
        image.src = imgUrl + "?not-from-cache-please"

        const paletteData = await Vibrant.from(image).getPalette()
        console.log(paletteData.DarkVibrant.rgb);
        const rgbArray = paletteData.DarkVibrant.rgb

        const hexColor = await ConvertRGBtoHex(rgbArray[0], rgbArray[1], rgbArray[2])
        console.log(hexColor);
        setBackgroundColorCode(hexColor)

        htmlToImage.toPng(document.getElementById('download'), {
            cacheBust: true, backgroundColor: hexColor
        })
            .then(function (dataUrl) {
                require("downloadjs")(dataUrl, 'my-node.png');
            });
    }

    let imgUrl = boardContent.imgUrl;


    //*********** 댓글 수정 입력 함수 *************//
    const [commentModiContent, setCommentModiContent] = useState("")
    const onCommentModiInputChange = (e) => {
        console.log(e.target.value);
        setCommentModiContent(e.target.value);
    };

    //*********** 댓글 수정 제출 함수 ************//
    async function onCommentModiSubmit(e) {
        console.log()
        e.preventDefault();
        const data = {
            boardId: boardContent.id,
            commentId: showModalEditComment.commentId,
            content: commentModiContent
        }
        try {
            console.log(data)
            const result = await axios.post('http://localhost:8080/modify/comment', data);
            console.log(result.data);
            console.log("댓글 수정 완료입니다.")
            handleCloseModal()
            getComments(id)
        } catch (error) {
            console.error('Error submitting modified comment:', error);
        }
    }


    //************* 댓글 작성 제출 함수 **********//
    async function onCommentSubmit(e) {
        console.log()
        e.preventDefault();
        const data = {
            boardId: parseInt(id),
            username: username,
            userId: parseInt(userId),
            content: comment
        }
        try {
            console.log(data)
            const result = await axios.post('http://localhost:8080/write/comment', data);
            console.log(result.data);
            setComment("");
            getComments(id);
        } catch (error) {
            console.error('Error submitting comment:', error);
        }
    }

    // imgUrl.setAttribute('src', `url/timestamp=${new Date().getTime()}`);
    return (
        <div className="mt-4 justify-content-center align-items-center">
            <div id='my-div' className="">
                <div className="col">
                    <div id="download" style={containerStyle}>
                        {boardContent?.imgUrl && (
                            <div style={{flex: '1', marginRight: '10px'}}>
                                <div><img src={imgUrl} alt="Board Image" style={{borderRadius: '10px'}}/></div>
                            </div>
                        )}
                        <div style={{flex: '1', overflow: 'hidden'}}>
                            <div>작성자: {boardContent?.username}</div>
                            <div>제목: {boardContent?.title}</div>
                            <div>내용: {boardContent?.content}</div>
                            <div>작성 날짜: {new Date(boardContent.createdDate).toLocaleString('ko-KR')}</div>
                            <div>수정 날짜: {new Date(boardContent.modifiedDate).toLocaleString('ko-KR')}</div>
                            <div style={{maxHeight: '130px', overflowY: 'auto'}}>
                                {comments.map((comment, index) => (
                                    <div key={index}>
                                        <div className="d-flex justify-content-between align-items-center"
                                             style={{margin: '3px'}}>
                                            <div>
                                                {comment.username}: {comment.content}
                                            </div>
                                            {username === comment.username && isLogined === true && (
                                                <div className="d-flex justify-content-center align-items-center">
                                                    <Button type="button"
                                                            className="d-flex align-items-center btn btn-success mx-2 btn-sm"
                                                            onClick={() => {
                                                                handleEditComment(comment.id, comment.content)
                                                            }}>
                                                        Edit
                                                    </Button>
                                                    <Button variant="danger" className="btn btn-success mx-2 btn-sm"
                                                            onClick={() => {
                                                                handleDeleteComment(comment.id);
                                                            }}>Delete</Button>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                ))}
                            </div>

                            {username == boardContent.username && isLogined == true ? (
                                <div className="mt-4 d-flex justify-content-center align-items-center">
                                    <Button type="button" className="btn btn-success mx-2" onClick={() => {
                                        handleEditBoardContent()
                                    }}>
                                        Edit
                                    </Button>
                                    <Button variant="danger" onClick={() => {
                                        handleDeleteBoardContent();
                                    }}>Delete</Button>
                                    <Button type="submit" variant="primary" className='mx-2' onClick={htmlToImg}>이미지로
                                        내려받기</Button>
                                </div>
                            ) : null}
                            <div id="commentArea">
                                <form id="commentForm" onSubmit={onCommentSubmit}>
                                    <div className="mb-3">
                                        {/*<div>{boardContent?.username}</div>*/}
                                    </div>
                                    <div className="mb-3">
                                        <label htmlFor="commentContent" className="form-label">댓글 내용</label>
                                        <textarea className="form-control" id="commentContent" name="commentContent"
                                                  rows="3" value={comment}
                                                  required onChange={(e) => onCommentInputChange(e)}></textarea>
                                    </div>
                                    <button type="submit" className="btn btn-primary">댓글 작성</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div>
                <Modal show={showModalEditBoardContent} onHide={handleCloseModal}>
                    <Modal.Header closeButton>
                        <Modal.Title>일기 수정</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <form onSubmit={(e) => onEditSubmit(e)}>
                            <div>
                                <Form.Group className="mb-3">
                                    <Form.Label>현재 그림을 유지하시겠습니까?</Form.Label>
                                    <div>
                                        <Form.Check
                                            inline
                                            label="Yes"
                                            type="radio"
                                            id="yesRadio"
                                            name="yesNoValue"
                                            value="Yes"
                                            checked={boardContent.yesNoValue === 'Yes'}
                                            onChange={(e) => onInputChange(e)}
                                        />
                                        <Form.Check
                                            inline
                                            label="No"
                                            type="radio"
                                            id="noRadio"
                                            name="yesNoValue"
                                            value="No"
                                            checked={boardContent.yesNoValue === 'No'}
                                            onChange={(e) => onInputChange(e)}
                                        />
                                    </div>
                                    <Form.Label>제목</Form.Label>
                                    <Form.Control
                                        name="title"
                                        placeholder="title"
                                        defaultValue={boardContent.title}
                                        readOnly={false}
                                        onChange={(e) => onInputChange(e)}
                                        required
                                    />
                                    <Form.Label>내용</Form.Label>
                                    <Form.Control
                                        name="content"
                                        placeholder="content"
                                        defaultValue={boardContent.content}
                                        onChange={(e) => onInputChange(e)}
                                        required
                                    />
                                </Form.Group>
                            </div>
                            <Button type="submit" variant="primary" className='mx-2'>
                                Save
                            </Button>
                            <Button variant="secondary" onClick={handleCloseModal}>
                                close
                            </Button>
                        </form>
                    </Modal.Body>
                </Modal>
            </div>


            <div>
                <Modal show={showModalDeleteBoardContent} onHide={handleCloseModal}>
                    <Modal.Body className='bg-danger text-white'>
                        <p>Are you sure you want to delete this board content?</p>
                        <Button variant="primary" onClick={() => {
                            onDeleteSubmit();
                            handleCloseModal()
                        }} className='mx-2'>
                            Yes
                        </Button>
                        <Button variant="secondary" onClick={handleCloseModal}>
                            Close
                        </Button>
                    </Modal.Body>

                </Modal>
            </div>

            {/*댓글 수정, 삭제*/}
            <div>
                <Modal show={showModalEditComment.tf} onHide={handleCloseModal}>
                    <Modal.Header closeButton>
                        <Modal.Title>댓글 수정</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <form onSubmit={(e) => onCommentModiSubmit(e)}>
                            <div>
                                <Form.Group className="mb-3">
                                    <Form.Label>내용</Form.Label>
                                    <Form.Control
                                        name="content"
                                        placeholder="content"
                                        defaultValue={showModalEditComment.commentContent}
                                        onChange={(e) => onCommentModiInputChange(e)}
                                        required
                                    />
                                </Form.Group>
                            </div>
                            <Button type="submit" variant="primary" className='mx-2'>
                                Save
                            </Button>
                            <Button variant="secondary" onClick={handleCloseModal}>
                                close
                            </Button>
                        </form>
                    </Modal.Body>
                </Modal>
            </div>


            <div>
                <Modal show={showModalDeleteComment.tf} onHide={handleCloseModal}>
                    <Modal.Body className='bg-danger text-white'>
                        <p>댓글을 삭제하시겠습니까?</p>
                        <Button variant="primary" onClick={() => {
                            onDeleteCommentSubmit(showModalDeleteComment.commentId);
                            handleCloseModal()
                        }} className='mx-2'>
                            Yes
                        </Button>
                        <Button variant="secondary" onClick={handleCloseModal}>
                            Close
                        </Button>
                    </Modal.Body>

                </Modal>
            </div>
        </div>
    );
}
