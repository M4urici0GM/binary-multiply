package dev.mgbarbosa.binarym;

import static java.lang.String.format;

import java.util.ArrayDeque;

class App {
    public static void main(final String[] args) {

        final var multiplier = "00000011";
        final var multiplicant = "11111101";

        final var totalSum = multiply(multiplier, multiplicant);
        final var sixteenBitSum = format("%32s", totalSum).replace(' ', '1');

        System.out.println(sixteenBitSum);
        System.out.println(Integer.toBinaryString(-9));

        final var parsedInt = (int) Long.parseLong(sixteenBitSum, 2);

        System.out.println(format("%8s, %s", sixteenBitSum, parsedInt));
    }

    public static String twosComplement(final String str) {
        // First, we flip all bits
        final var arr = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            arr[i] = str.charAt(i) == '1'
                    ? '0'
                    : '1';
        }

        // Then we add 1 to it.
        final var flippedStr = new String(arr);
        final var sum = sum(flippedStr, "001");

        return sum.substring(sum.length() - str.length());
    }

    private static String normalizeBinary(final String binaryStr, final int size) {
        return format("%" + size + "s", binaryStr).replace(' ', '0');
    }

    public static String multiply(final String a, final String b) {
        final var size = Math.max(a.length(), b.length());
        final var multiplicant = normalizeBinary(a, size);
        final var multiplier = normalizeBinary(b, size);

        final var partials = new ArrayDeque<String>();
        var shiftCount = 0;
        for (var i = multiplier.length() - 1; i >= 0; i--) {
            final var c = multiplicant.charAt(i);
            final var result = new StringBuilder();

            for (var j = multiplicant.length() - 1; j >= 0; j--) {
                final var cc = multiplier.charAt(j);
                final var mResult = multiply(cc, c);

                result.insert(0, mResult);
            }

            partials.add(format("%s%s", result.toString(), "0".repeat(shiftCount)));
            shiftCount += 1;
        }

        var sum = partials.pop();
        while (partials.peek() != null) {
            sum = sum(sum, partials.pop());
        }

        return sum.substring(sum.length() - size); // truncate string to initial bit size.
    }

    public static String sum(final String a, final String b) {
        final var result = new StringBuilder();
        final var biggestStr = Math.max(a.length(), b.length());

        final var aF = normalizeBinary(a, biggestStr);
        final var bF = normalizeBinary(b, biggestStr);

        boolean carry = false;
        for (int i = aF.length() - 1; i >= 0; i--) {
            final var numA = aF.charAt(i);
            final var numB = bF.charAt(i);

            final var sumResult = sum(numA, numB, carry);

            result.insert(0, sumResult.result);
            carry = sumResult.carry;
        }

        if (carry == true) {
            result.insert(0, '1');
        }

        return result.toString();
    }

    public static SumResult sum(final char a, final char b, final boolean carry) {
        if ((a == '0' && b == '0')) {
            return new SumResult(carry ? '1' : '0', false);
        }

        if ((a == '0' && b == '1') || (a == '1' && b == '0')) {
            return new SumResult(carry ? '0' : '1', carry);
        }

        return new SumResult(carry ? '1' : '0', true);
    }

    public static char multiply(final char a, final char b) {
        if (a == '0' || b == '0') {
            return '0';
        }

        return '1';
    }

    static class SumResult {
        public char result;
        public boolean carry;

        public SumResult(final char result, final boolean carry) {
            this.result = result;
            this.carry = carry;
        }
    }
}
