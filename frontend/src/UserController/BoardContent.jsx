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
export default BoardContent;

function BoardContent() {

    const {id} = useParams();//id값 가져오기

    // zustand 를 이용한 전역 상태 사용&관리
    const {isLogined, setIsLogined} = useStore(state => state);
    const {username, setUsername} = useStore(state => state);
    const {userId, setUserId} = useStore(state => state);
    const {role, setRole} = useStore(state => state);

    const [boardContent, setBoardContent] = useState([]);
    const [showModalEditBoardContent, setshowModalEditBoardContent] = useState(false);
    const [showModalDeleteBoardContent, setshowModalDeleteBoardContent] = useState(false);
    const navigate = useNavigate();


    const handleEditBoardContent = () => {
        setshowModalEditBoardContent(true);
    };
    const handleDeleteBoardContent = () => {
        setshowModalDeleteBoardContent(true);
    };

    const handleCloseModal = () => {
        setshowModalEditBoardContent(false);
        setshowModalDeleteBoardContent(false);
    };

    const onInputChange = (e) => {
        console.log(e.target.name);
        console.log(e.target.value);
        setBoardContent({...boardContent, [e.target.name]: e.target.value});
    };

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

    const getBoardContent = async (id) => {
        const result = await axios.get(`http://localhost:8080/board/${id}`);
        await setBoardContent(JSON.parse(JSON.stringify(result.data)));
        console.log("result.data: " + JSON.stringify(result.data));
    };

    //db에서 컨텐츠 가져오기
    useEffect(() => {
        const fetchData = async ()=>{
            await getBoardContent(id)
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
        padding: "30px"
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
    // imgUrl.setAttribute('src', `url/timestamp=${new Date().getTime()}`);
    return (
        <div className="mt-4 d-flex justify-content-center align-items-center">
            <div id='my-div' className="">
                <div className="col">
                    <div id="download" style={containerStyle}>
                    {boardContent?.imgUrl && (
                        <div><img src={imgUrl} alt="Board Image" style={{ borderRadius: '10px' }} /></div>
                    )}
                    <div>작성자: {boardContent?.username}</div>
                    <div>제목: {boardContent?.title}</div>
                    <div>내용: {boardContent?.content}</div>
                    <div>작성 날짜: {new Date(boardContent.createdDate).toLocaleString('ko-KR')}</div>
                    <div>수정 날짜: {new Date(boardContent.modifiedDate).toLocaleString('ko-KR')}</div>
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
                            <Button type="submit" variant="primary" className='mx-2' onClick={htmlToImg}>이미지로 내려받기</Button>
                        </div>
                    ) : null}
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

        </div>
    );
}
