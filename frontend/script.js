function createChessboard() {
    const chessboard = document.getElementById('chessboard');
    const pieceImages = {
        'wr': 'WhiteRook.png', 'wn': 'WhiteKnight.png', 'wb': 'WhiteBishop.png',
        'wq': 'WhiteQueen.png', 'wk': 'WhiteKing.png', 'wp': 'WhitePawn.png',
        'br': 'BlackRook.png', 'bn': 'BlackKnight.png', 'bb': 'BlackBishop.png',
        'bq': 'BlackQueen.png', 'bk': 'BlackKing.png', 'bp': 'BlackPawn.png'
    };

    // Initial board setup (using chess notation)
    const initialSetup = [
        ['br', 'bn', 'bb', 'bq', 'bk', 'bb', 'bn', 'br'],
        ['bp', 'bp', 'bp', 'bp', 'bp', 'bp', 'bp', 'bp'],
        [null, null, null, null, null, null, null, null],
        [null, null, null, null, null, null, null, null],
        [null, null, null, null, null, null, null, null],
        [null, null, null, null, null, null, null, null],
        ['wp', 'wp', 'wp', 'wp', 'wp', 'wp', 'wp', 'wp'],
        ['wr', 'wn', 'wb', 'wq', 'wk', 'wb', 'wn', 'wr']
    ];

    for (let rank = 0; rank < 8; rank++) {
        for (let file = 0; file < 8; file++) {
            const square = document.createElement("div");
            square.className = `square ${(rank + file) % 2 === 0 ? "light" : "dark"}`;
            const pieceCode = initialSetup[rank][file];
            
            // Add event listener to each square
            square.addEventListener('click', function() {
                // Toggle the 'istapped' class to change the color of the tapped square
                square.classList.toggle('istapped');
            });

            if (pieceCode) {
                const pieceImage = document.createElement("img");
                pieceImage.src = `assets/pieces/${pieceImages[pieceCode]}`;
                pieceImage.alt = pieceCode;
                pieceImage.classList.add("piece");
                square.appendChild(pieceImage);
            }
            chessboard.appendChild(square);
        }
    }
}

document.addEventListener('DOMContentLoaded', createChessboard);
