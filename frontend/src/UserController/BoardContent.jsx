import React, {useEffect, useState} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';
import {json, useNavigate, useParams} from "react-router-dom";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import useStore from "../store";

export default BoardContent;

function BoardContent() {

    const {id} = useParams();//id값 가져오기

    const axiosInstance = axios.create();
    axiosInstance.interceptors.request.use(
        (config) => {
            const token = localStorage.getItem("key")
            if (token) {
                config.headers['Authorization'] = `Bearer ${token}`;
            }
            return config;
        },
        (error) => {
            return Promise.reject(error);
        }
    )

    // zustand 를 이용한 전역 상태 사용&관리
    const {isLogined, setIsLogined} = useStore(state => state);
    const {username, setUsername} = useStore(state => state);

    const [boardContent, setBoardContent] = useState({});
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
        const result = await axiosInstance.post(`http://localhost:8080/modify/board/${id}`, boardContent)
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
        setBoardContent(JSON.parse(JSON.stringify(result.data)));
        console.log("result.data: " + JSON.stringify(result.data));
    };

    //db에서 컨텐츠 가져오기
    useEffect(() => {
        getBoardContent(id);
    }, []);


    return (
        <div className="mt-4 d-flex justify-content-center align-items-center">
            <div className="">
                <div className="col">
                    {boardContent?.imgUrl && (
                        <img src={boardContent.imgUrl} alt="Board Image" style={{ borderRadius: '10px' }} />
                    )}
                    <div>작성자: {boardContent?.username}</div>
                    <div>제목: {boardContent?.title}</div>
                    <div>내용: {boardContent?.content}</div>
                    <div>작성 날짜: {boardContent?.createdDate}</div>
                    <div>수정 날짜: {boardContent?.modifiedDate}</div>
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
