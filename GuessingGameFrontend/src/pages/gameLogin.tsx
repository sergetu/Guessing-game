import React from 'react';
import {NavigateFunction, useNavigate} from 'react-router-dom';
import axios from "axios";

type GameLoginProps = {
    navigate: NavigateFunction;
};

type GameLoginState = {
    playerName: string;
};

class GameLoginClass extends React.Component<GameLoginProps, GameLoginState> {
    state: GameLoginState = {
        playerName: '',
    };

    handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        this.setState({playerName: e.target.value});
    };

    handleSubmit = async () => {
        const playerName = this.state.playerName.trim();
        if (!playerName) return;

        try {
            const response = await axios.post(`/api/user/get-or-create`, null, {
                params: {userName: playerName}
            });

            localStorage.setItem('playerName', response.data.name);
            localStorage.setItem('userId', response.data.id);
            this.props.navigate('/game/play');
        } catch (err) {
            console.error("User creation failed", err);
            alert("Unable to start game. Please try again.");
        }
    };

    render() {
        return (
            <div className="container py-5 text-center">
                <h2 className="mb-4">Enter your name to start</h2>
                <input
                    type="text"
                    className="form-control mb-3"
                    placeholder="Your name"
                    value={this.state.playerName}
                    onChange={this.handleChange}
                />
                <button
                    className="btn btn-success"
                    onClick={this.handleSubmit}
                    disabled={!this.state.playerName.trim()}
                >
                    Start Game
                </button>
            </div>
        );
    }
}

export default function GameLogin() {
    const navigate = useNavigate();
    return <GameLoginClass navigate={navigate}/>;
}
