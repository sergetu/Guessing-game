import React from 'react';
import {NavigateFunction, useNavigate} from 'react-router-dom';
import axios from 'axios';

type LeaderboardEntry = {
    name: string;
    gamesPlayed: number;
    gamesWon: number;
    totalTries: number;
};

type LeaderboardProps = {
    navigate: NavigateFunction;
};

type LeaderboardState = {
    entries: LeaderboardEntry[];
    minGames: number;
    error: string;
};

class LeaderboardClass extends React.Component<LeaderboardProps, LeaderboardState> {
    state: LeaderboardState = {
        entries: [],
        minGames: 1,
        error: ''
    };

    componentDidMount() {
        this.loadLeaderboard();
    }

    loadLeaderboard = async () => {
        try {
            const res = await axios.get('/api/leaderboard', {
                params: {minPlayedGames: this.state.minGames}
            });
            this.setState({entries: res.data, error: ''});
        } catch (err) {
            this.setState({error: 'Failed to load leaderboard.'});
        }
    };

    handleMinGamesChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = parseInt(e.target.value) || 1;
        this.setState({minGames: value}, this.loadLeaderboard);
    };

    render() {
        const {entries, minGames, error} = this.state;

        return (
            <div className="container py-5">
                <h2 className="mb-4 text-center">Leaderboard</h2>

                <div className="mb-4 text-center">
                    <label className="form-label me-2">Min Games Played:</label>
                    <input
                        type="number"
                        min={1}
                        value={minGames}
                        onChange={this.handleMinGamesChange}
                        className="form-control d-inline-block"
                        style={{width: '100px'}}
                    />
                </div>

                {error && <div className="alert alert-danger text-center">{error}</div>}

                <table className="table table-bordered table-striped text-center">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Player</th>
                        <th>Games Played</th>
                        <th>Games Won</th>
                        <th>Total Tries</th>
                        <th>Success Rate (%)</th>
                    </tr>
                    </thead>
                    <tbody>
                    {entries.map((entry, i) => {
                        const successRate = entry.gamesPlayed > 0
                            ? ((entry.gamesWon / entry.gamesPlayed) * 100).toFixed(1)
                            : '0.0';

                        return (
                            <tr key={i}>
                                <td>{i + 1}</td>
                                <td>{entry.name}</td>
                                <td>{entry.gamesPlayed}</td>
                                <td>{entry.gamesWon}</td>
                                <td>{entry.totalTries}</td>
                                <td>{successRate}</td>
                            </tr>
                        );
                    })}
                    </tbody>
                </table>
                <div className="text-center mt-4">
                    <button className="btn btn-secondary" onClick={() => this.props.navigate('/')}>
                        Back to Home
                    </button>
                </div>
            </div>
        );
    }
}

export default function Leaderboard() {
    const navigate = useNavigate();
    return <LeaderboardClass navigate={navigate}/>;
}
