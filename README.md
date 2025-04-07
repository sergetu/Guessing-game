# Guessing Game

A simple web-based guessing game.

## How It Works

- The game picks a secret 4-digit number.
- All digits are different.
- You have 8 tries to guess it.
- After each guess, you’ll see feedback:
  - `P:p` → correct digits in the correct place.
  - `M:m` → correct digits in the wrong place.

### Example

Secret: `7046`  
Guess: `8724`  
Output: `M:2; P:0`

---

## UI Flow

- **Start Screen**: shows rules and a button to start the game.  
- **Game Screen**:
  - Enter your name
  - 4-digit input
  - See number of tries left
  - See result of last guess and a log of all previous guesses  
- **Game Over Screen**:
  - Shows if you won or lost
  - Shows the secret number
  - Option to start a new game  
- **Leaderboard**:
  - Based on success rate (wins / games played)
  - If equal, fewer total tries wins
  - You can filter by minimum number of games played

---

## Tech

- Backend: Java Spring.
- Frontend: node.js, react, typescript, bootstrap.
- Game logic runs on backend
- Uses: in-memory DB, postgresql
- Input is validated both on frontend and backend
- Unit tests included

---

## How to Run

1. Go to `/dist`
2. Run the app with one script (e.g. `run.sh` or `run.bat`)
