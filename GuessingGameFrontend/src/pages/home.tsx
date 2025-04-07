import React from 'react';
import {NavigateFunction, useNavigate} from 'react-router-dom';

type HomeProps = {
    navigate: NavigateFunction;
};

class HomeClass extends React.Component<HomeProps> {
    handleStart = () => {
        this.props.navigate('/game/login');
    };

    handleLeaderboard = () => {
        this.props.navigate('/game/leaderboard');
    };

    render() {
        return (
            <div
                className="d-flex flex-column justify-content-center align-items-center min-vh-100 text-center bg-light px-4">
                <h1 className="display-4 fw-bold mb-4">Guessing Game</h1>
                <p className="lead mb-5" style={{maxWidth: '600px'}}>
                    The program chooses a secret 4-digit number with all different digits. You have 8 tries to guess it.
                    After each guess, you’ll get feedback like “M:2; P:1” where M means correct digits in the wrong
                    place, and P means correct digits in the right place.
                </p>

                <div className="d-flex gap-3">
                    <button className="btn btn-primary btn-lg" onClick={this.handleStart}>
                        Start Game
                    </button>
                    <button className="btn btn-outline-secondary btn-lg" onClick={this.handleLeaderboard}>
                        View Leaderboard
                    </button>
                </div>
            </div>
        );
    }
}

export default function Home() {
    const navigate = useNavigate();
    return <HomeClass navigate={navigate}/>;
}
