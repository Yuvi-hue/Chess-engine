package backend.src.main.java.com.chess.core;

import static backend.src.main.java.com.chess.core.BitboardUtils.*;

import backend.src.main.java.com.chess.core.BoardState.PieceColor;
import backend.src.main.java.com.chess.core.BoardState.PieceType;

public class Movegen {

    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
    public enum MoveType {
        NORMAL,
        CAPTURE,
        CASTLE,
        EN_PASSANT,
        PROMOTION
    }

    private long whiteAttacks;
    private long blackAttacks;
    private long whiteOccupiedSquares;
    private long blackOccupiedSquares;

    public boolean isKingInCheck(PieceColor color) {
    //Get position of the king
    long kingPosition = (color == PieceColor.WHITE) ? whitePieces.get(PieceType.KING) : blackPieces.get(PieceType.KING);
    int kingPositionBit = getLSB(kingPosition);
    // Get opponent's color and their attacks
    PieceColor opponentColor = (color == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
    long opponentAttackBitboard = getCombinedAttackBitboard(opponentColor);
    // Check if the king's position is attacked by any of the opponent's pieces
    return isBitSet(opponentAttackBitboard, kingPositionBit);
    }
}