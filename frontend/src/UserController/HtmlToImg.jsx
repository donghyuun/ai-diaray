import React, {useEffect, useState} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';
import {json, useNavigate, useParams} from "react-router-dom";

import useStore from "../store";
import * as htmlToImage from 'html-to-image';

export default BoardContent;

function BoardContent() {

    const {id} = useParams();//id값 가져오기

    // zustand 를 이용한 전역 상태 사용&관리
    const {isLogined, setIsLogined} = useStore(state => state);
    const {username, setUsername} = useStore(state => state);
    const {userId, setUserId} = useStore(state => state);
    const {role, setRole} = useStore(state => state);

    const axiosInstance = axios.create();
    axiosInstance.interceptors.request.use(
        async (config) => {
            let token = localStorage.getItem("key")
            if (token) {
                //*****************************************//
                // JWT 토큰에서 만료 일자 추출
                function extractExpiration(token) {
                    const tokenPayload = JSON.parse(atob(token.split('.')[1]));
                    return new Date(tokenPayload.exp * 1000); // 만료 일자를 밀리초로 변환하여 반환
                }

                function isTokenExpired(token) {
                    const expirationDate = extractExpiration(token);
                    const currentDate = new Date();
                    if(expirationDate < currentDate) console.log("엑세스 토큰의 유효기간이 만료되었습니다.")
                    return expirationDate < currentDate; // 만료 일자와 현재 일자를 비교하여 토큰이 만료되었는지 확인
                }

                if (isTokenExpired(token)) {

                    const refreshToken = localStorage.getItem("rfkey");

                    console.log("get 요청, /refresh")
                    const result = await axios.get("http://localhost:8080/refresh",
                        { headers:
                                {   "Authorization": `Bearer ${token}`,
                                    "Refresh-Token": `Bearer ${refreshToken}`,
                                    "User-Id":`${userId}`,
                                    "User-Role": `${role}`,
                                    "Username": `${username}`
                                }
                        });
                    console.log("get 요쳥 결과 수신, result 변수")
                    const newAccessToken = result.data;
                    console.log(newAccessToken);
                    localStorage.setItem("key", newAccessToken);

                    config.headers['Authorization'] = `Bearer ${newAccessToken}`;
                    // //새로운 엑세스 토큰을 로컬 스토리지에 저장
                    // localStorage.setItem("key", newAccessToken);
                    // token = newAccessToken;

                }
                //******************************************//
                else{
                    //만료된게 아니라면 그냥 기존의 토큰을 헤더에 넣어서 요청을 보내면 됌
                    config.headers['Authorization'] = `Bearer ${token}`;
                }
            }
            return config;
        },
        (error) => {
            return Promise.reject(error);
        }
    )

    function htmlToPng() {
        var node = document.getElementById("my-div");
        htmlToImage.toPng(node)
            .then(function (dataUrl) {
                require("downloadjs")(dataUrl, 'my-node.png');
            })
    }

    return (
        <div className="mt-4 d-flex justify-content-center align-items-center">
            <div>
                <div>This is sample div element.</div>
                <p>spanspasapn</p>

            </div>
            <div id="ShowImage"></div>
            <button onClick={htmlToPng}>Convert</button>
        </div>
    );
}
