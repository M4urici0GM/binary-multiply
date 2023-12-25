

## Binary Multiplication

While I was rewriting my project (Tcproxy)[https://github.com/m4urici0gm/tcproxy], I came across serializing and 
deserializing numbers into binary and vice versa.
The thing is, I never even thought about how numbers worked behind the scenes, and now, for the first time,
my mind dragged me into this hole, and man, that's AWESOME.

The first thing i went to learn, is how binary numbers are multiplied. and its very simple, looks like multiplying normal
numbers:

Given a multiplicant and a multiplier, we first grab the LSB(Least significant bit, or the rightmost bit) from the multplier 
(in this case 1) and multiply each number from the multiplicant.

For Instance, given the binary `multiplicant` `11111011` and the `multiplier` `11111101` we first grab the `1` from the

multiplier and mulitply each bit for the `multiplicant`, giving us `11111011` (since the bit was 1, we can copy the bits from the multiplicant)

now we do the same step for each bit on the multiplier, but with the difference that we shift `<<` new partial result by 1 bit,
until we reach the leftmost bit on the multiplier. and its done, that's it.

Given the previous example:

```
              1 0 1 =  5 -> multiplicant
            * 0 1 1 =  3 -> multiplier
--------------------------------------------
              1 0 1 
            1 0 1 |   
          0 0 0   |    
--------------------------------------------
          0 1 1 1 1 = 15
```

But then it hit me, but what about negative numbers? Well, that's the trick, in short, negative numbers doesnt exist
(sort of).
I Will not enter on the theory (because i dont fully understand it yet), but just put the feet underwater:

My hero, idol, whatever you wanna call it, no less than `John von Neumann` proposed the strategy to represent negative
numbers into computer since in his 1945 `First Draft of a Report on the EDVAC`, and nowdays we know this strategy as 
`Two's Complement`.
He realized that by reserving the MSB for indicating if the number is positive `0` and negative `1` it would massively
simplify hardware operations for multiplying numbers. not even his proposal is used until today, this streamlined the entire
hardware design for bit to bit operation.

How it works?
Lets get our previous examples..
Given the 8bit number 3, to turn it into -3, we will grab `00000101` then flip all bits and then sum 1 into it.
```
  ~ 0 0 0 0 0 0 1 1 = 3
--------------------------
    1 1 1 1 1 1 0 0
  + 0 0 0 0 0 0 0 1
--------------------------
    1 1 1 1 1 1 0 1 = -3
```

And there we have it, we have `11111101`, which is the binary representation of -3
With another example:

```
  ~ 0 0 0 0 0 1 0 1 = 5
--------------------------
    1 1 1 1 1 0 1 0
  + 0 0 0 0 0 0 0 1
--------------------------
    1 1 1 1 1 0 1 1 = -5
```
It's simple, right?
Now we can multiply those numbers the same way as previously:
```
              1 1 1 1 1 0 1 1 = -5
            * 1 1 1 1 1 1 0 1 = -3
--------------------------------------------
              1 1 1 1 1 0 1 1
            0 0 0 0 0 0 0 0 |
          1 1 1 1 1 0 1 1   |
        1 1 1 1 1 0 1 1     |
      1 1 1 1 1 0 1 1 |     |
    1 1 1 1 1 0 1 1   |     |
  1 1 1 1 1 0 1 1     |     |
1 1 1 1 1 0 1 1       |     |
--------------------------------------------
   discarded| 0 0 0 0 1 1 1 1 = +15
```
But now you may ask, why are we discarding the first 7 bits that overflowed, but not before?
Well, we need to be carefull when multiplying to numbers. We were able to not discard in the first example because
both numbers were 8 bits, so the resulting 4 bits will not overflow the memory. But now, since the result is bigger
than 8 bits, we need to truncate the result to the rightmost 8 bits.
The same is valid for other numbers, if we multiply two 16 bit numbers, we need to truncate it to the rightmost 16 bits.
But if we multiply an 8 bit number, and a 16 bit number, we need to "promote" the 8bit number to a 16bit one, in order to 
multiply and truncate correctly. That's why we can have weird results if multiply to longs (64bit) and then truncate it to 
32 or even 16bits.


