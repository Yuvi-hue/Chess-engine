package backend.src.main.java.com.chess.core;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Random;

import static backend.src.main.java.com.chess.core.BitboardUtils.*;
import backend.src.main.java.com.chess.core.Random.*;


public final class MagicBitboards {
    // Magic numbers for bishop and rook attacks
    

    private static final int[] bishopRelevantBits = {6,5,5,5,5,5,5,6,
                                                    5,5,5,5,5,5,5,5,
                                                    5,5,7,7,7,7,5,5,
                                                    5,5,7,9,9,7,5,5,
                                                    5,5,7,9,9,7,5,5,
                                                    5,5,7,7,7,7,5,5,
                                                    5,5,5,5,5,5,5,5,
                                                    6,5,5,5,5,5,5,6};

    private static final int[] rookRelevantBits = {12,11,11,11,11,11,11,12,
                                                    11,10,10,10,10,10,10,11,
                                                    11,10,10,10,10,10,10,11,
                                                    11,10,10,10,10,10,10,11,
                                                    11,10,10,10,10,10,10,11,
                                                    11,10,10,10,10,10,10,11,
                                                    11,10,10,10,10,10,10,11,
                                                    12,11,11,11,11,11,11,12,};


    // Masks for bishop and rook attacks
    private static final long[] bishopMasks = new long[64];
    private static final long[] rookMasks = new long[64];



    // Attacks lookup tables
    private static final long[][] bishopAttackTable = new long[64][512];
    private static final long[][] rookAttackTable = new long[64][4096];

    



    public static void main(String[] args) {
        //testAttacks(28, 0x00000000000000L, true);
        initializeMagics();
    }
    static {
        
    }

    private static int state = 1804289383;

    private static long get32BRandomNumber() {
        state = xorLeft(state, 13);
        state = xorRight(state, 17);  // Note: >>> for unsigned right shift
        state = xorLeft(state, 5);
        return Integer.toUnsignedLong(state);
    }
    private static long get64BRandomNumber() {
        long first, second, third, fourth;
        first = intersectBitboards(get32BRandomNumber(),0xFFFF);
        second = intersectBitboards(get32BRandomNumber(),0xFFFF);
        third = intersectBitboards(get32BRandomNumber(),0xFFFF);
        fourth = intersectBitboards(get32BRandomNumber(),0xFFFF);
        long random = addBitboards(first,shiftBitForward(second,16));
        random = addBitboards(random,shiftBitForward(third,32));
        random = addBitboards(random,shiftBitForward(fourth,48));
        return random;
    }
    private static long generateMagicCandidate() {
        long candidate = intersectBitboards(get64BRandomNumber(), get64BRandomNumber());
        candidate = intersectBitboards(candidate, get64BRandomNumber());
        return candidate;
    }
    //find the magic numbers
    private static long findMagic(int square, int relevantBits,boolean isBishop) {
        long[] occupancies = new long[4096];
        long[] attacks = new long[4096];
        long[] usedAttacks = new long[4096];
        long attackMask = isBishop ? bishopMasks[square] : rookMasks[square];
        int indices = (int)(setBit(0L, relevantBits));
        for (int index = 0; index < indices;index++) {
            occupancies[index]  = setOccupancy(index, attackMask);
            attacks[index] = isBishop ? generateBishopAttacks(square, occupancies[index]) : generateRookAttacks(square, occupancies[index]);
        }
        //test the magic number
        boolean failed = false;
        long magic = 0L;
        for (int randomCount=0;randomCount<100000000;randomCount++) {
            magic = generateMagicCandidate();
            if (countBits(intersectBitboards(attackMask * magic, 0xFF00000000000000L))<6) continue;
            Arrays.fill(usedAttacks, 0L);
            failed = false;
            for (int j=0;!failed && j<indices;j++) {
                int magicIndex = (int)(shiftBitBackward(occupancies[j]*magic,64-relevantBits));
                //if magic index works
                if (usedAttacks[magicIndex] == 0L) {
                    usedAttacks[magicIndex] = attacks[j];
                } else if (usedAttacks[magicIndex] != attacks[j]) {
                    failed = true;
                }
            }
        }
        if (!failed) {
            return magic;
        }
        System.out.println("Failed to find a magic number for square " + square);
        return 0L;
    }
    // Initialize the magic numbers 
    private static void initializeMagics() {
        for (int square = 0;square<64;square++) {
            long magic = findMagic(square,rookRelevantBits[square],false);
            System.out.println(Long.toHexString(magic));
        }
    }
    
    
    
    // Calculate rook mask (excluding edge squares)
    private static long calculateRookMask(int square) {
        long mask = 0L;
        int row = getRank(square);
        int column = getFile(square);
        
        // Horizontal and vertical directions, excluding edge squares
        for (int i = 1; row+i<7; i++) mask = addBitboards(mask, setBit(0L,square+i*8)); //goes up
        for (int i = 1; row-i>0; i++) mask = addBitboards(mask, setBit(0L,square-i*8)); //goes down
        for (int i = 1; column+i<7; i++) mask = addBitboards(mask, setBit(0L,square+i)); //goes right
        for (int i = 1; column-i>0; i++) mask = addBitboards(mask, setBit(0L,square-i)); //goes left
        
        return mask;
    }
        // Calculate bishop mask (excluding edge squares)
        private static long calculateBishopMask(int square) {
        long mask = 0L;
        int row = getRank(square);
        int column = getFile(square);
    
        // Diagonal directions, excluding the outer edges of the board
        for (int i=1; row+i<7 && column-i>0;i++) mask = addBitboards(mask, setBit(0L,square+i*7)); //goes up left
        for (int i=1; row+i<7 && column+i<7;i++) mask = addBitboards(mask, setBit(0L,square+i*9)); //goes up right
        for (int i=1; row-i>0 && column-i>0;i++) mask = addBitboards(mask, setBit(0L,square-i*9)); //goes down left
        for (int i=1; row-i>0 && column+i<7;i++) mask = addBitboards(mask, setBit(0L,square-i*7)); //goes down right

    
        return mask;
        }


    private static long setOccupancy(int index, long mask) {
        long occupancy = 0L;
        int numBits = countBits(mask);
        for (int count = 0; count < numBits; count++) {
            int square = getLSB(mask);
            Long[] ref = new Long[] { mask }; // wrap it
            popLSB(ref);
            mask = ref[0]; // <-- UPDATE occupancy with the modified value
            if (isBitSet(index, count)) {
                occupancy = addBitboards(occupancy, setBit(0L, square));
            }
        }
        
        return occupancy;
    }
    
    //Compute bishop attacks on the fly
    private static long generateBishopAttacks(int square, long occupiedSquares) {
        long blockers = intersectBitboards(occupiedSquares, bishopMasks[square]);
        long attacks = 0L;
        int row = getRank(square);
        int column = getFile(square);

        
        // Iterate through all possible blocker combinations
        for (int i=1; row+i<8 && column-i>=0;i++) {
            attacks = addBitboards(attacks, setBit(attacks,square+i*7)); //goes up left
            if (isBitSet(blockers, square+i*7)) {
                break;
            }
        }
        for (int i=1; row+i<8 && column+i<8;i++) {
            attacks = addBitboards(attacks, setBit(attacks,square+i*9)); //goes up right
            if (isBitSet(blockers, square+i*9)) {
                break;
            }
        }
        for (int i=1; row-i>=0 && column-i>=0;i++) {
            attacks = addBitboards(attacks, setBit(attacks,square-i*9)); //goes down left
            if (isBitSet(blockers, square-i*9)) {
                break;
            }
        }
        for (int i=1; row-i>=0 && column+i<8;i++) {
            attacks = addBitboards(attacks, setBit(attacks,square-i*7)); //goes down right
            if (isBitSet(blockers, square-i*7)) {
                break;
            }
        }
        return attacks;
    }
    private static long generateRookAttacks(int square, long occupiedSquares) {
        long blockers = intersectBitboards(occupiedSquares, rookMasks[square]);
        long attacks = 0L;
        int row = getRank(square);
        int column = getFile(square);
        // Iterate through all possible blocker combinations
        for (int i = 1; row+i<8; i++) {
            attacks = addBitboards(attacks, setBit(0L,square+i*8)); //goes up
            if (isBitSet(blockers, square+i*8)) {
                break;
            }
        }
        for (int i = 1; row-i>=0; i++) {
            attacks = addBitboards(attacks, setBit(0L,square-i*8)); //goes down
            if (isBitSet(blockers, square-i*8)) {
                break;
            }
        }
        for (int i = 1; column+i<8; i++) {
            attacks = addBitboards(attacks, setBit(0L,square+i)); //goes right
            if (isBitSet(blockers, square+i)) {
                break;
            }
        }
        for (int i = 1; column-i>=0; i++) {
            attacks = addBitboards(attacks, setBit(0L,square-i)); //goes left
            if (isBitSet(blockers, square-i)) {
                break;
            }
        }
        return attacks;
    }
}
