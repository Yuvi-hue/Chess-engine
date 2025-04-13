package backend.src.main.java.com.chess.core;

import java.util.List;

public class BitboardUtils {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        //temporary test
        printBitboard(0xFF);
    }
    public static final int RookIndexBits = 12;
    public static final int BishopIndexBits = 9;

    public static long setBit(long bitboard, int square) {
        return bitboard | (1L << square);
    }
    public static int getBit(long bitboard, int square) {
        return (int) ((bitboard >> square) & 1);
    }
    public static long clearBit(long bitboard, int square) {
        return bitboard & ~(1L << square);
    }
    public static int xorLeft(int number, int shift) {
        return number ^ (number << shift);
    }

    public static int xorRight(int number, int shift) {
        return number ^ (number >>> shift);  // Unsigned right shift
    }
    public static long xorLeft64(long number, int shift) {
        return number ^ (number << shift);
    }

    public static long xorRight64(long number, int shift) {
        return number ^ (number >>> shift);  // Unsigned right shift
    }
    public static long toggleBit(long bitboard, int square) {
        return bitboard ^ (1L << square);
    }
    public static boolean isBitSet(long bitboard, int square) {
        return (bitboard & (1L << square)) != 0;
    }
    public static long shiftBitForward(long bitboard, int square) {
        return bitboard << square;
    }
    public static long shiftBitBackward(long bitboard, int square) {
        return bitboard >> square;
    }
    public static long unsignedRightShift(long value, int shift) {
        return (value >= 0) ? value >> shift : (value >> shift) & (~0L >>> shift);
    }
    
    public static boolean isValidSquare (int square) {
        return square>=0 && square<64;
    }
    public static long addBitboards(long bitboard1, long bitboard2) {
        return bitboard1 | bitboard2;
    }
    public static long intersectBitboards(long bitboard1, long bitboard2) {
        return bitboard1 & bitboard2;
    }
    public static final long flipVertical(long bitboard) {
        return (Long.reverseBytes(bitboard));
    }
    public static int getRank(int square) {
        return (square)/8;
    }
    public static int getFile(int square) {
        return square%8;
        //return (square)%8 == 0 ? 8 : (square)%8;
    }
    public static char getFileAlpha(int square) {
        return (char) ('a' + (Long.numberOfTrailingZeros(square) -1));
    }
    public static int getBitIndex(long bitboard, int num) {
        int count = 0;
        for (int i = 0; i < 64; i++) { // There are 64 bits in a long
            if (isBitSet(bitboard, i)) { // Check if the bit at position i is set
                if (count == num) {
                    return i; // Return the index of the j-th set bit
                }
                count++; // Increment the counter when a set bit is found
            }
        }
        return -1; // If no such j-th set bit exists (out of range)
    }
    
    public static int getLSB(long bitboard) {
        return Long.numberOfTrailingZeros(bitboard);
    }
    public static int popLSB(Long[] bitboardRef) {
        long bitboard = bitboardRef[0];
        if (bitboard == 0) return -1; //edge case, no bits set
        long lsb = bitboard & -bitboard;
        bitboardRef[0] = bitboard ^ lsb;
        return Long.numberOfTrailingZeros(lsb);
    }
    public static int countBits(long bitboard) {
        return Long.bitCount(bitboard);
    }
    public static List<Integer> bitboardToSquares(long bitboard) {
        List<Integer> squares = new java.util.ArrayList<>();
        while (bitboard != 0) {
            int square = Long.numberOfTrailingZeros(bitboard);
            squares.add(square);
            bitboard &= bitboard - 1; // Clear the least significant bit
        }
        return squares;
    }
    public static long squaresToBitboard(List<Integer> squares) {
        long bitboard = 0L;
        for (int square : squares) {
            bitboard |= (1L << square);
        }
        return bitboard;
    }
    public static void printBitboard(long bitboard) {
        for (int rank = 7; rank >=0 ; rank--) {
            for (int file = 0; file < 8; file++) {
                int square = rank * 8 + file;
                System.out.print(isBitSet(bitboard, square) ? "1 " : "0 ");
            }
            System.out.println();
        }
    }
}