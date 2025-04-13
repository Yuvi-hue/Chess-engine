package backend.src.main.java.com.chess.core;
import static backend.src.main.java.com.chess.core.BitboardUtils.*;

import java.util.List;
public abstract class NormalPiecesTables {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        //temporary test
    }
    protected static final long[] Files = {0x0101010101010101L,
        0x0202020202020202L,
        0x0404040404040404L,
        0x0808080808080808L,
        0x1010101010101010L,
        0x2020202020202020L,
        0x4040404040404040L,
        0x8080808080808080L};
    protected static final long[] Ranks = {0xFFL,
        0xFF00L,
        0xFF0000L,
        0xFF000000L,
        0xFF00000000L,
        0xFF0000000000L,
        0xFF000000000000L,
        0xFF00000000000000L};

    private static boolean isMoveValid(int currentSquare, int targetSquare) {
        // Check if the target square is within the board boundaries
        int currentFile=getFile(currentSquare);
        int targetFile=getFile(targetSquare);
        if (targetSquare < 0 || targetSquare >= 64) {
            return false; // Out of bounds
        }
        else if (Math.abs(currentFile - targetFile) > 2) {
            return false; //Wrapping around the board
        }
        else
        return true;

    }

    public final class SimplePieceTables extends NormalPiecesTables {
        // Precomputed move tables for simple pieces
        private static final long[] knightMoves = new long[64];
        private static final long[] kingMoves = new long[64];

            //initialize the tables 
            static {
                precomputeKnightMoves();
                precomputeKingMoves();
                //precomputePawnMoves();
            }

    
        private static void precomputeKnightMoves() { //temporary
            for (int square = 0; square < 64; square++) {
                knightMoves[square] = calculateKnightMoves(square);
            }
        }

        //Knight moves table
        private static long calculateKnightMoves(int square) {
            long moves = 0L;
            int[] knightOffsets = {-17, -15, -10, -6, 6, 10, 15, 17};
            for (int offset : knightOffsets) {
                int targetSquare = square + offset;
                if (isMoveValid(square, targetSquare)) {
                    moves = setBit(moves, targetSquare);
                }
            }
            return moves;
        }

        //King moves table
        private static void precomputeKingMoves() { //temporary
            for (int square = 0; square < 64; square++) {
                kingMoves[square] = calculateKingMoves(square);
            }
        }
        private static long calculateKingMoves(int square) {
            long moves = 0L;
            int[] kingOffsets = {-1, 1, -8, 8, -7, 7, -9, 9};
            for (int offset : kingOffsets) {
                int targetSquare = square + offset;
                if (isMoveValid(square, targetSquare)) {
                    moves = setBit(moves, targetSquare);
                }
            }
            return moves;
        }
    }
    public final class SlidingPiecesTables extends NormalPiecesTables {
        public static void main(String[] args) {
            System.out.println(verifyMagicBitboards());
        }
        private static final long[] ROOK_MAGICS = {
            /* a1 */ 0x0080001020400080L, /* b1 */ 0x0040001000200040L,
            /* c1 */ 0x0080081000200080L, /* d1 */ 0x0080040800100080L,
            /* e1 */ 0x0080020400080080L, /* f1 */ 0x0080010200040080L,
            /* g1 */ 0x0080008001000200L, /* h1 */ 0x0080002040800100L,
            /* a2 */ 0x0000800020400080L, /* b2 */ 0x0000400020005000L,
            /* c2 */ 0x0000801000200080L, /* d2 */ 0x0000800800100080L,
            /* e2 */ 0x0000800400080080L, /* f2 */ 0x0000800200040080L,
            /* g2 */ 0x0000800100020080L, /* h2 */ 0x0000800040800100L,
            /* a3 */ 0x0000208000400080L, /* b3 */ 0x0000404000201000L,
            /* c3 */ 0x0000808010002000L, /* d3 */ 0x0000808008001000L,
            /* e3 */ 0x0000808004000800L, /* f3 */ 0x0000808002000400L,
            /* g3 */ 0x0000010100020004L, /* h3 */ 0x0000020000408100L,
            /* a4 */ 0x0000200080004000L, /* b4 */ 0x0000400040002000L,
            /* c4 */ 0x0000800020001000L, /* d4 */ 0x0000800010000800L,
            /* e4 */ 0x0000800008000400L, /* f4 */ 0x0000800004000200L,
            /* g4 */ 0x0000800002000100L, /* h4 */ 0x0000200004000800L,
            /* a5 */ 0x0000400000802000L, /* b5 */ 0x0000200040001000L,
            /* c5 */ 0x0000100020000800L, /* d5 */ 0x0000080010000400L,
            /* e5 */ 0x0000040008000200L, /* f5 */ 0x0000020004000100L,
            /* g5 */ 0x0000010002000080L, /* h5 */ 0x0000004080010002L,
            /* a6 */ 0x0000002000400080L, /* b6 */ 0x0000001000200040L,
            /* c6 */ 0x0000000800100020L, /* d6 */ 0x0000000400080010L,
            /* e6 */ 0x0000000200040008L, /* f6 */ 0x0000000100020004L,
            /* g6 */ 0x0000000080010002L, /* h6 */ 0x0000000040008001L,
            /* a7 */ 0x0000000020004000L, /* b7 */ 0x0000000010002000L,
            /* c7 */ 0x0000000008001000L, /* d7 */ 0x0000000004000800L,
            /* e7 */ 0x0000000002000400L, /* f7 */ 0x0000000001000200L,
            /* g7 */ 0x0000000000800100L, /* h7 */ 0x0000000000400080L,
            /* a8 */ 0x0000000000200040L, /* b8 */ 0x0000000000100020L,
            /* c8 */ 0x0000000000080010L, /* d8 */ 0x0000000000040008L,
            /* e8 */ 0x0000000000020004L, /* f8 */ 0x0000000000010002L,
            /* g8 */ 0x0000000000008001L, /* h8 */ 0x0020008040008100L
        };    
        private static final long[] BISHOP_MAGICS = {
            /* a1 */ 0x0002020202020200L, /* b1 */ 0x0002020202020000L,
            /* c1 */ 0x0004010202000000L, /* d1 */ 0x0004040080000000L,
            /* e1 */ 0x0001104000000000L, /* f1 */ 0x0000821040000000L,
            /* g1 */ 0x0000410410400000L, /* h1 */ 0x0000104104104000L,
            /* a2 */ 0x0000040404040400L, /* b2 */ 0x0000020202020200L,
            /* c2 */ 0x0000040102020000L, /* d2 */ 0x0000040400800000L,
            /* e2 */ 0x0000011040000000L, /* f2 */ 0x0000008210400000L,
            /* g2 */ 0x0000004104104000L, /* h2 */ 0x0000002082082000L,
            /* a3 */ 0x0004000808080800L, /* b3 */ 0x0002000404040400L,
            /* c3 */ 0x0001000202020200L, /* d3 */ 0x0000800802004000L,
            /* e3 */ 0x0000400400802000L, /* f3 */ 0x0000200200401000L,
            /* g3 */ 0x0000100100200800L, /* h3 */ 0x0000040080100400L,
            /* a4 */ 0x0000020008040400L, /* b4 */ 0x0000010004020200L,
            /* c4 */ 0x0000008002010100L, /* d4 */ 0x0000004081020000L,
            /* e4 */ 0x0000002040810000L, /* f4 */ 0x0000001020408000L,
            /* g4 */ 0x0000000810204000L, /* h4 */ 0x0000000408102000L,
            /* a5 */ 0x0000020002020200L, /* b5 */ 0x0000010001010100L,
            /* c5 */ 0x0000008000808080L, /* d5 */ 0x0000004100404040L,
            /* e5 */ 0x0000002080202020L, /* f5 */ 0x0000001040101010L,
            /* g5 */ 0x0000000820080808L, /* h5 */ 0x0000000410040404L,
            /* a6 */ 0x0000000202000000L, /* b6 */ 0x0000000101000000L,
            /* c6 */ 0x0000000080800000L, /* d6 */ 0x0000000040400000L,
            /* e6 */ 0x0000000020200000L, /* f6 */ 0x0000000010100000L,
            /* g6 */ 0x0000000008080000L, /* h6 */ 0x0000000004040000L,
            /* a7 */ 0x0000000002020000L, /* b7 */ 0x0000000001010000L,
            /* c7 */ 0x0000000000808000L, /* d7 */ 0x0000000000404000L,
            /* e7 */ 0x0000000000202000L, /* f7 */ 0x0000000000101000L,
            /* g7 */ 0x0000000000080800L, /* h7 */ 0x0000000000040400L,
            /* a8 */ 0x0000000000020200L, /* b8 */ 0x0000000000010100L,
            /* c8 */ 0x0000000000008080L, /* d8 */ 0x0000000000004040L,
            /* e8 */ 0x0000000000002020L, /* f8 */ 0x0000000000001010L,
            /* g8 */ 0x0000000000000808L, /* h8 */ 0x0000000000000404L
        };       
        
            // Attack tables
            private static final long[][] ROOK_ATTACKS = new long[64][1 << BitboardUtils.RookIndexBits];
            private static final long[][] BISHOP_ATTACKS = new long[64][1 << BitboardUtils.BishopIndexBits];
        
            static {
                initializeMagicBitboards();
            }
        
            // ========== Public API ==========
            public static long getRookAttacks(int square, long blockers) {
                long magic = ROOK_MAGICS[square];
                long key = (blockers * magic) >>> (64 - BitboardUtils.RookIndexBits);
                return ROOK_ATTACKS[square][(int)key];
            }
        
            public static long getBishopAttacks(int square, long blockers) {
                long magic = BISHOP_MAGICS[square];
                long key = (blockers * magic) >>> (64 - BitboardUtils.BishopIndexBits);
                return BISHOP_ATTACKS[square][(int)key];
            }
        
            public static long getQueenAttacks(int square, long blockers) {
                return BitboardUtils.addBitboards(
                    getRookAttacks(square, blockers),
                    getBishopAttacks(square, blockers)
                );
            }
        
            // ========== Initialization ==========
            private static void initializeMagicBitboards() {
                for (int square = 0; square < 64; square++) {
                    initializeRookAttacks(square);
                    initializeBishopAttacks(square);
                }
            }
        
            // ===== ROOK =====
            private static void initializeRookAttacks(int square) {
                long mask = calculateRookBlockMask(square);
                List<Integer> blockerSquares = BitboardUtils.bitboardToSquares(mask);
                int permutations = 1 << blockerSquares.size();
        
                for (int i = 0; i < permutations; i++) {
                    long blockers = generateBlockers(i, blockerSquares);
                    int index = calculateMagicIndex(blockers, ROOK_MAGICS[square], BitboardUtils.RookIndexBits);
                    ROOK_ATTACKS[square][index] = calculateRookAttacks(square, blockers);
                }
            }
        
            private static long calculateRookBlockMask(int square) {
                long mask = 0L;
                int rank = square / 8;
                int file = square % 8;
        
                // North
                for (int r = rank + 1; r <= 6; r++) 
                    mask = BitboardUtils.setBit(mask, r * 8 + file);
                // South
                for (int r = rank - 1; r >= 1; r--)
                    mask = BitboardUtils.setBit(mask, r * 8 + file);
                // East
                for (int f = file + 1; f <= 6; f++)
                    mask = BitboardUtils.setBit(mask, rank * 8 + f);
                // West
                for (int f = file - 1; f >= 1; f--)
                    mask = BitboardUtils.setBit(mask, rank * 8 + f);
        
                return mask;
            }
        
            private static long calculateRookAttacks(int square, long blockers) {
                long attacks = 0L;
                int[] directions = {-8, 8, -1, 1}; // N, S, W, E
        
                for (int dir : directions) {
                    for (int to = square + dir; ; to += dir) {
                        if (!BitboardUtils.isValidSquare(to) || 
                            Math.abs(BitboardUtils.getFile(to) - BitboardUtils.getFile(to - dir)) > 1) 
                            break;
        
                        attacks = BitboardUtils.setBit(attacks, to);
                        if (BitboardUtils.isBitSet(blockers, to)) break;
                    }
                }
                return attacks;
            }
        
            // ===== BISHOP =====
            private static void initializeBishopAttacks(int square) {
                long mask = calculateBishopBlockMask(square);
                List<Integer> blockerSquares = BitboardUtils.bitboardToSquares(mask);
                int permutations = 1 << blockerSquares.size();
        
                for (int i = 0; i < permutations; i++) {
                    long blockers = generateBlockers(i, blockerSquares);
                    int index = calculateMagicIndex(blockers, BISHOP_MAGICS[square], BitboardUtils.BishopIndexBits);
                    BISHOP_ATTACKS[square][index] = calculateBishopAttacks(square, blockers);
                }
            }
        
            private static long calculateBishopBlockMask(int square) {
                long mask = 0L;
                int rank = square / 8;
                int file = square % 8;
        
                // Northwest
                for (int r = rank + 1, f = file - 1; r <= 6 && f >= 1; r++, f--)
                    mask = BitboardUtils.setBit(mask, r * 8 + f);
                // Northeast
                for (int r = rank + 1, f = file + 1; r <= 6 && f <= 6; r++, f++)
                    mask = BitboardUtils.setBit(mask, r * 8 + f);
                // Southwest
                for (int r = rank - 1, f = file - 1; r >= 1 && f >= 1; r--, f--)
                    mask = BitboardUtils.setBit(mask, r * 8 + f);
                // Southeast
                for (int r = rank - 1, f = file + 1; r >= 1 && f <= 6; r--, f++)
                    mask = BitboardUtils.setBit(mask, r * 8 + f);
        
                return mask;
            }
        
            private static long calculateBishopAttacks(int square, long blockers) {
                long attacks = 0L;
                int[] directions = {-9, -7, 7, 9}; // NW, NE, SW, SE
        
                for (int dir : directions) {
                    for (int to = square + dir; ; to += dir) {
                        if (!BitboardUtils.isValidSquare(to) || 
                            Math.abs(BitboardUtils.getFile(to) - BitboardUtils.getFile(to - dir)) > 1) 
                            break;
        
                        attacks = BitboardUtils.setBit(attacks, to);
                        if (BitboardUtils.isBitSet(blockers, to)) break;
                    }
                }
                return attacks;
            }
        
            // ========== Shared Helpers ==========
            private static long generateBlockers(int index, List<Integer> squares) {
                long blockers = 0L;
                for (int i = 0; i < squares.size(); i++) {
                    if ((index & (1 << i)) != 0) {
                        blockers = BitboardUtils.setBit(blockers, squares.get(i));
                    }
                }
                return blockers;
            }
        
            private static int calculateMagicIndex(long blockers, long magic, int shift) {
                return (int)((blockers * magic) >>> (64 - shift));
            }
            //Verification 
            public static String verifyMagicBitboards() {
                StringBuilder result = new StringBuilder();
                result.append("=== Magic Bitboard Verification ===\n");
                
                // 1. Test edge squares
                result.append(verifyEdgeSquares());
                
                // 2. Test center squares
                result.append(verifyCenterSquares());
                
                // 3. Test with blockers
                result.append(verifyWithBlockers());
                
                // 4. Print sample attack maps
                result.append("\nSample Attack Maps:\n");
                result.append(printSampleAttackMaps());
                
                return result.toString();
            }
            
            // ===== Detailed Verification Methods =====
            private static String verifyEdgeSquares() {
                StringBuilder sb = new StringBuilder("\nEdge Square Tests:\n");
                
                // Rook tests
                sb.append("Rook:\n");
                verifySquare(sb, 0,  0x01010101010101FEL, "a1");  // a1
                verifySquare(sb, 7,  0x808080808080807EL, "h1");  // h1
                verifySquare(sb, 56, 0xFE01010101010100L, "a8");  // a8
                verifySquare(sb, 63, 0x7E80808080808080L, "h8");  // h8
                
                // Bishop tests
                sb.append("\nBishop:\n");
                verifySquare(sb, 0,  0x8040201008040200L, "a1");
                verifySquare(sb, 7,  0x0102040810204080L, "h1");
                verifySquare(sb, 56, 0x0002040810204080L, "a8");
                verifySquare(sb, 63, 0x0040201008040201L, "h8");
                
                return sb.toString();
            }
            
            private static String verifyCenterSquares() {
                StringBuilder sb = new StringBuilder("\nCenter Square Tests:\n");
                
                // e4 (square 28)
                verifySquare(sb, 28, 0x2828FE28280000L, "Rook e4");
                verifySquare(sb, 28, 0x8040201008040200L & ~(1L<<28), "Bishop e4");
                
                // d5 (square 35)
                verifySquare(sb, 35, 0x1414FE14140000L, "Rook d5");
                verifySquare(sb, 35, 0x40201008040201L << 7, "Bishop d5");
                
                return sb.toString();
            }
            
            private static String verifyWithBlockers() {
                StringBuilder sb = new StringBuilder("\nBlockers Tests:\n");
                
                // Rook with center blocker
                long blockers = BitboardUtils.setBit(0L, 27); // e3
                long expected = 0x00000001010100L; // e4-e2, e4-f4-h4
                verifySquare(sb, 28, expected, "Rook e4 with e3 blocker", blockers);
                
                // Bishop with corner blocker
                blockers = BitboardUtils.setBit(0L, 45); // f6
                expected = 0x201008040200L; // e4-d5-c6, e4-f5
                verifySquare(sb, 28, expected, "Bishop e4 with f6 blocker", blockers);
                
                return sb.toString();
            }
            
            // ===== Helper Methods =====
            private static void verifySquare(StringBuilder sb, int square, long expected, String description) {
                verifySquare(sb, square, expected, description, 0L);
            }
            
            private static void verifySquare(StringBuilder sb, int square, long expected, String description, long blockers) {
                boolean isRook = description.contains("Rook");
                long actual = isRook ? getRookAttacks(square, blockers) 
                                     : getBishopAttacks(square, blockers);
                
                sb.append(String.format("%-25s: %s\n", description, 
                    (actual == expected) ? "PASS" : "FAIL (expected vs actual)"));
                
                if (actual != expected) {
                    sb.append("Expected:\n");
                    sb.append(bitboardToString(expected));
                    sb.append("Actual:\n");
                    sb.append(bitboardToString(actual));
                }
            }
            
            private static String printSampleAttackMaps() {
                StringBuilder sb = new StringBuilder();
                
                // Rook at a1
                sb.append("Rook a1 (empty board):\n");
                sb.append(bitboardToString(getRookAttacks(0, 0L)));
                
                // Bishop at e4
                sb.append("\nBishop e4 (empty board):\n");
                sb.append(bitboardToString(getBishopAttacks(28, 0L)));
                
                // Queen at d1
                sb.append("\nQueen d1 (empty board):\n");
                sb.append(bitboardToString(getQueenAttacks(3, 0L)));
                
                return sb.toString();
            }
            
            private static String bitboardToString(long bitboard) {
                StringBuilder sb = new StringBuilder();
                for (int rank = 7; rank >= 0; rank--) {
                    for (int file = 0; file < 8; file++) {
                        int square = rank * 8 + file;
                        sb.append(BitboardUtils.isBitSet(bitboard, square) ? "1 " : "0 ");
                    }
                    sb.append("\n");
                }
                return sb.toString();
            }
        }
    
    public final class PawnTables extends NormalPiecesTables {
        public static final long[] whiteAttacks = new long[64];
        public static final long[] whiteSinglePushes = new long[64];
        public static final long[] whiteDoublePushes = new long[64];

        public static final long[] blackAttacks = new long[64];
        public static final long[] blackSinglePushes = new long[64];
        public static final long[] blackDoublePushes = new long[64];
        public static void main(String[] args) {
            System.out.println("Hello, World!");
            System.out.println("Pawn pushes from b2");
            printBitboard(getSinglePush(14, false));
        }
        static {
            precomputeWhite();
        }
        
        public static long getAttacks(int square, boolean isWhite) {
            return isWhite ? whiteAttacks[square] : flipVertical(whiteAttacks[square]);
        }
        public static long getSinglePush(int square, boolean isWhite) {
            return isWhite ? whiteSinglePushes[square] : flipVertical(whiteSinglePushes[square]);
            }
        public static long getDoublePush(int square, boolean isWhite) {
            long bitboard=setBit(0L, square);
            if (getFile(bitboard)!=1) {
                return 0L;
            }
            return isWhite ? whiteDoublePushes[square] : flipVertical(whiteDoublePushes[square]);

        }
        
    
        private static void precomputeWhite() {
            for (int square = 0; square < 64; square++) {
                whiteAttacks[square] = computeWhitePawnAttacks(square);
                whiteSinglePushes[square] = computeWhiteSinglePush(square);
                if (7<square && square<16) {
                    whiteDoublePushes[square] = computeWhiteDoublePush(square);
                }
            }
        }
        private static long computeWhitePawnAttacks(int square) {
            long moves = setBit(0L, square);
            moves = addBitboards(shiftBitForward(moves, 7), shiftBitForward(moves, 9));
            return moves;
        }
        private static long computeWhiteSinglePush(int square) {
            long move = setBit(0L,square+8);
            return move;
        }
        private static long computeWhiteDoublePush(int square) {
            long moves = setBit(0L,square+16);
            return moves;
        } 
    }

}