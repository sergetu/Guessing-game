import React from 'react';
import axios from 'axios';
import {NavigateFunction, useNavigate} from 'react-router-dom';

type GameStatus = {
    gameId: number;
    attemptNumber: number;
    m: number;
    p: number;
};

type GameResponse = {
    status: GameStatus;
    win: boolean;
    gameOver: boolean;
};

type GameProps = {
    navigate: NavigateFunction;
};

type TryEntry = {
    guess: string;
    result: string; // M:x; P:y
};

type GameState = {
    playerName: string;
    userId: number;
    gameId: number;
    nameSubmitted: boolean;
    inputDigits: string[];
    triesLeft: number;
    triesLog: TryEntry[];
    error: string;
};

class GameClass extends React.Component<GameProps, GameState> {
    maxTries = 8;
    inputRefs: (HTMLInputElement | null)[] = [];

    state: GameState = {
        playerName: '',
        userId: 0,
        gameId: 0,
        nameSubmitted: false,
        inputDigits: ['', '', '', ''],
        triesLeft: this.maxTries,
        triesLog: [],
        error: '',
    };

    async componentDidMount() {
        const playerName = localStorage.getItem('playerName');
        const userIdStr = localStorage.getItem('userId');

        if (!playerName || !userIdStr) {
            window.location.href = '/game/login';
            return;
        }

        const userId = parseInt(userIdStr);

        try {
            const res = await axios.get('/api/game/new-game', {
                params: {userId}
            });

            const status = res.data.status;

            this.setState({
                playerName,
                userId,
                gameId: status.gameId,
                nameSubmitted: true,
                triesLeft: this.maxTries - status.attemptNumber,
                triesLog: [],
                inputDigits: ['', '', '', ''],
                error: '',
            });
        } catch {
            this.setState({error: 'Failed to start a new game.'});
        }
    }

    handleDigitChange = (index: number, value: string) => {
        if (!/^\d?$/.test(value)) return;
        const updatedDigits = [...this.state.inputDigits];
        updatedDigits[index] = value;
        this.setState({inputDigits: updatedDigits}, () => {
            if (value && index < this.inputRefs.length - 1) {
                this.inputRefs[index + 1]?.focus();
            }
        });
    };

    makeGuess = async () => {
        const {inputDigits, userId} = this.state;
        const guess = inputDigits.join('');

        if (guess.length !== 4 || new Set(guess).size !== 4) {
            this.setState({error: 'Guess must have 4 different digits.'});
            return;
        }

        this.setState({error: ''});

        try {
            const response = await axios.post('/api/game/guess', {
                userId,
                guess
            });

            const data: GameResponse = response.data;

            if (data.gameOver) {
                localStorage.setItem('gameResult', data.win ? 'win' : 'lose');
                window.location.href = '/game/over';
            }

            if (data.status === null) {
                this.props.navigate('/game/login');
                return;
            }

            const result = `M:${data.status.m}; P:${data.status.p}`;
            const newLogEntry: TryEntry = {guess, result};

            this.setState((prev) => ({
                triesLog: [...prev.triesLog, newLogEntry],
                triesLeft: prev.triesLeft - 1,
                inputDigits: ['', '', '', '']
            }), () => {
                this.inputRefs[0]?.focus();
            });

        } catch (err) {
            this.setState({error: 'Server error. Try again.'});
        }
    };

    handleFocus = (index: number) => {
        const updatedDigits = [...this.state.inputDigits];
        updatedDigits[index] = '';
        this.setState({inputDigits: updatedDigits});
    };

    render() {
        const {playerName, inputDigits, triesLeft, triesLog, error} = this.state;

        return (
            <div className="container py-5">
                <div>
                    <h3 className="mb-4">Welcome, {playerName}!</h3>

                    <div className="d-flex gap-3 justify-content-center mb-3">
                        {inputDigits.map((digit, i) => (
                            <input
                                key={i}
                                ref={(el: HTMLInputElement | null) => {
                                    this.inputRefs[i] = el;
                                }}
                                type="text"
                                maxLength={1}
                                className="form-control text-center"
                                style={{width: '60px', fontSize: '1.5rem'}}
                                value={digit}
                                onChange={(e) => this.handleDigitChange(i, e.target.value)}
                                onFocus={() => this.handleFocus(i)}
                            />
                        ))}
                    </div>

                    <div className="text-center mb-3">
                        <button className="btn btn-primary" onClick={this.makeGuess}>
                            Make Guess
                        </button>
                    </div>

                    {error && <div className="alert alert-danger text-center">{error}</div>}

                    <div className="text-center mb-4">
                        <strong>Tries left:</strong> {triesLeft}
                    </div>

                    <h5>Previous Tries</h5>
                    <ul className="list-group">
                        {triesLog.map((entry, i) => (
                            <li className="list-group-item d-flex justify-content-between" key={i}>
                                <span>#{i + 1} → {entry.guess}</span>
                                <span>{entry.result}</span>
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        );
    }
}

export default function Game() {
    const navigate = useNavigate();
    return <GameClass navigate={navigate}/>;
}
