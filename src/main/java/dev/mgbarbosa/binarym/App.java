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

    public static String multiply(String multiplicant, String multiplier) {
        final var biggestStr = Math.max(multiplier.length(), multiplicant.length());
        multiplicant = format("%" + biggestStr + "s", multiplicant).replace(' ', '0');
        multiplier = format("%" + biggestStr + "s", multiplier).replace(' ', '0');

        final var partials = new ArrayDeque<String>();
        var shiftCount = 0;
        for (var multiplierI = multiplier.length() - 1; multiplierI >= 0; multiplierI--) {
            final var c = multiplicant.charAt(multiplierI);
            var result = new StringBuilder();

            for (var multiplicantJ = multiplicant.length() - 1; multiplicantJ >= 0; multiplicantJ--) {
                final var cc = multiplier.charAt(multiplicantJ);
                final var mResult = multiply(cc, c);

                result = result.insert(0, mResult);
            }

            final var binary = result.toString() + "0".repeat(shiftCount);
            partials.add(binary);
            shiftCount += 1;
        }

        var sum = partials.pop();
        while (partials.peek() != null) {
            sum = sum(sum, partials.pop());
        }

        return sum.substring(sum.length() - biggestStr);
    }

    public static String sum(String a, String b) {
        final var result = new StringBuilder();
        final var biggestStr = Math.max(a.length(), b.length());

        a = format("%" + biggestStr + "s", a).replace(' ', '0');
        b = format("%" + biggestStr + "s", b).replace(' ', '0');

        boolean carry = false;
        for (int i = a.length() - 1; i >= 0; i--) {
            final var numA = a.charAt(i);
            final var numB = b.charAt(i);

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
            return new SumResult(carry ? '0': '1', carry);
        }

        return new SumResult(carry ? '1' : '0', true);
    }

    static class SumResult {
        public char result;
        public boolean carry;

        public SumResult(final char result, final boolean carry) {
            this.result = result;
            this.carry = carry;
        }
    }

    public static char multiply(final char a, final char b) {
        if (a == '0' || b == '0') {
            return '0';
        }

        return '1';
    }
}
