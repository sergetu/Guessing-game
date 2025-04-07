import React from 'react';
import {NavigateFunction, useNavigate} from 'react-router-dom';
import axios from "axios";

type GameOverProps = {
    navigate: NavigateFunction;
};

type GameOverState = {
    playerName: string;
    gameResult: 'win' | 'lose';
    digits: string;
};

class GameOverClass extends React.Component<GameOverProps, GameOverState> {
    state: GameOverState = {
        playerName: '',
        gameResult: 'lose',
        digits: ''
    };

    async componentDidMount() {
        const playerName = localStorage.getItem('playerName') || 'Unknown';
        const result = (localStorage.getItem('gameResult') as 'win' | 'lose') || 'lose';
        const userId = localStorage.getItem('userId');
        const response = await axios.get(`/api/game/get-code`, {
            params: {userId: userId}
        });
        const digits = response.data;
        this.setState({playerName, gameResult: result, digits: digits});
    }

    handleBackToHome = () => {
        this.props.navigate('/');
    };

    handleNewGame = () => {
        this.props.navigate('/game/play');
    };


    render() {
        const {playerName, gameResult, digits} = this.state;
        const message = gameResult === 'win' ? 'You Win!' : 'You Lose 😢';
        const messageClass = gameResult === 'win' ? 'text-success' : 'text-danger';

        return (
            <div className="container py-5 text-center">
                <h2 className="mb-4">Game Over, {playerName}!</h2>
                <h1 className={`display-4 fw-bold ${messageClass} mb-4`}>{message}</h1>
                <p className="mb-4">Code was: {digits}</p>

                <button className="btn btn-primary btn-lg m-4" onClick={this.handleNewGame}>
                    New Game
                </button>

                <button className="btn btn-outline-secondary btn-lg pl-3 m-4" onClick={this.handleBackToHome}>
                    Back to Home
                </button>
            </div>
        );
    }
}

export default function GameOver() {
    const navigate = useNavigate();
    return <GameOverClass navigate={navigate}/>;
}
