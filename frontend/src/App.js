import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import User from './UserController/User';
import NavigationBar from './component/NavigationBar';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import Board from "./UserController/Board";
import BoardContent from "./UserController/BoardContent";


function App() {
  return (

    <div className="container" >
        <BrowserRouter>
            <NavigationBar />
            <Routes>
                <Route path="/" element={<User />}></Route>
                <Route path="/board/:id" element={<BoardContent />}></Route>
                <Route path="/board" element={<Board />}></Route>
                {/* 상단에 위치하는 라우트들의 규칙을 모두 확인, 일치하는 라우트가 없는경우 처리 */}
            </Routes>
        </BrowserRouter>
    </div>
  );
}

export default App;
