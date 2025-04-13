package backend.src.main.java.com.chess.core;
import static backend.src.main.java.com.chess.core.BitboardUtils.*;

public class Random {
    private static int state = 1804289383;

    public static void main(String[] args) {
        printBitboard(get32BRandomNumber());
        printBitboard(get32BRandomNumber());
        System.out.println("Random 64-bit number:");
        printBitboard(get64BRandomNumber());
    }
    
    public static long get32BRandomNumber() {
        state = xorLeft(state, 13);
        state = xorRight(state, 17);  // Note: >>> for unsigned right shift
        state = xorLeft(state, 5);
        return Integer.toUnsignedLong(state);
    }
    public static long get64BRandomNumber() {
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
    public static long generateMagicCandidate() {
        long candidate = intersectBitboards(get64BRandomNumber(), get64BRandomNumber());
        candidate = intersectBitboards(candidate, get64BRandomNumber());
        return candidate;
    }
    //find the magic numbers
}

