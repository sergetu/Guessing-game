import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './pages/home';
import Game from './pages/game';
import GameLogin from "./pages/gameLogin";
import GameOver from "./pages/GameOver";
import Leaderboard from "./pages/Leaderboard";

const App: React.FC = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="/home" element={<Home/>}/>
                <Route path="/game/login" element={<GameLogin/>}/>
                <Route path="/game/play" element={<Game/>}/>
                <Route path="/game/over" element={<GameOver/>}/>
                <Route path="/game/leaderboard" element={<Leaderboard/>}/>
            </Routes>
        </Router>
    );
};

export default App;
