// axiosInstance.js

import axios from 'axios';

const axiosInstance = (userId, role, username) => {
    const instance = axios.create();

    // 요청 인터셉터 등록
    instance.interceptors.request.use(
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

                    try {
                        const result = await axios.get("http://localhost:8080/refresh", {
                            headers: {
                                "Authorization": `Bearer ${token}`,
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

                    } catch (error) {
                        console.error("Error refreshing token:", error);
                    }
                } else {
                    //만료된게 아니라면 그냥 기존의 토큰을 헤더에 넣어서 요청을 보내면 됌
                    config.headers['Authorization'] = `Bearer ${token}`;
                }
            }
            return config;
        },
        (error) => {
            return Promise.reject(error);
        }
    );

    return instance;
};

export default axiosInstance;
