package backend.src.main.java.com.chess.core;  // Must match the folder path!
import java.util.EnumMap;
import java.util.EnumSet;
import static backend.src.main.java.com.chess.core.BitboardUtils.*;
public class BoardState {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        //temporary test
    }
    public enum PieceColor {
        WHITE,
        BLACK
    }
    public enum PieceType {
        PAWN, 
        KNIGHT,
        BISHOP,
        ROOK,
        QUEEN,
        KING
    }
    public enum CastlingRights {
        WHITE_KINGSIDE,
        WHITE_QUEENSIDE,
        BLACK_KINGSIDE,
        BLACK_QUEENSIDE
    }
    private EnumMap<PieceType, Long> whitePieces = new EnumMap<>(PieceType.class);
    private EnumMap<PieceType, Long> blackPieces = new EnumMap<>(PieceType.class);
    private PieceColor ActivePlayer;
    private EnumSet<CastlingRights> castlingRights;
    private Integer enPassantTargetSquare;
    private int halfMoveClock;
    private int fullMoveNumber;

    //Constructor
    public BoardState() {
        resetBoard();

    }
    //Initialize the board
    public void resetBoard() {
        //Initializing the white pieces
        whitePieces.put(PieceType.PAWN, 0xFF00L);
        whitePieces.put(PieceType.KNIGHT, 0x42L);
        whitePieces.put(PieceType.BISHOP, 0x24L);
        whitePieces.put(PieceType.ROOK, 0x81L);
        whitePieces.put(PieceType.QUEEN, 0x8L);
        whitePieces.put(PieceType.KING, 0x10L);
        //Initializing the black pieces
        blackPieces.put(PieceType.PAWN, 0xFF000000000000L);
        blackPieces.put(PieceType.KNIGHT, 0x4200000000000000L);
        blackPieces.put(PieceType.BISHOP, 0x2400000000000000L);
        blackPieces.put(PieceType.ROOK, 0x8100000000000000L);
        blackPieces.put(PieceType.QUEEN, 0x800000000000000L);
        blackPieces.put(PieceType.KING, 0x1000000000000000L);

        //Game state
        ActivePlayer = PieceColor.WHITE;
        castlingRights = EnumSet.allOf(CastlingRights.class);
        enPassantTargetSquare = null;
        halfMoveClock = 0;
        fullMoveNumber = 1;
    }

    //Getters 
    //for white pieces
    public long getWhitePieces(PieceType type) {
        return whitePieces.get(type);
    }
    //for black pieces
    public long getBlackPieces(PieceType type) {
        return blackPieces.get(type);
    }
    public EnumSet<CastlingRights> getCastlingRights() {
        return castlingRights;
    }
    public Integer getEnPassantTargetSquare() {
        return enPassantTargetSquare;
    }
    public int getHalfMoveClock() {
        return halfMoveClock;
    }
    public int getFullMoveNumber() {
        return fullMoveNumber;
    }
    //active player
    public PieceColor getActivePlayer() {
        return ActivePlayer;
    }
    public long getAllOccupiedSquares() {
        // Combine all pieces into a single bitboard
        long occupiedSquares = 0L;
        occupiedSquares = addBitboards(getOccupiedSquares(PieceColor.WHITE), getOccupiedSquares(PieceColor.BLACK))
        return occupiedSquares;
    }
    public long getOccupiedSquares(PieceColor color) {
        // Get the pieces of the player
        EnumMap<PieceType, Long> pieces = (color == PieceColor.WHITE) ? whitePieces : blackPieces;
        // Combine all pieces into a single bitboard
        long occupiedSquares = 0L;
        for (PieceType type : PieceType.values()) {
            occupiedSquares = addBitBoards(occupiedSquares, pieces.get(type));
        }
        return occupiedSquares;
    }
    public long getCombinedAttackBitboard(PieceColor color) {
        long attackBitboard = 0L;
        // Get the pieces of the player
        EnumMap<PieceType, Long> pieces = (color == PieceColor.WHITE) ? whitePieces : blackPieces;
        // Iterate through each piece type and add its attack bitboard to the combined attack bitboard
        attackBitboard=addBitBoards(attackBitboard, getPawnAttacksBitboard(pieces.get(PieceType.PAWN), color));
        attackBitboard=addBitBoards(attackBitboard, getKnightAttacksBitboard(pieces.get(PieceType.KNIGHT)));
        attackBitboard=addBitBoards(attackBitboard, getBishopAttacksBitboard(pieces.get(PieceType.BISHOP)));
        attackBitboard=addBitBoards(attackBitboard, getRookAttacksBitboard(pieces.get(PieceType.ROOK)));
        attackBitboard=addBitBoards(attackBitboard, getQueenAttacksBitboard(pieces.get(PieceType.QUEEN)));
        attackBitboard=addBitBoards(attackBitboard, getKingAttacksBitboard(pieces.get(PieceType.KING)));
        return attackBitboard;
    }
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