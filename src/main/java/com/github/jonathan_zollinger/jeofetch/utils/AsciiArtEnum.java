package com.github.jonathan_zollinger.jeofetch.utils;

public enum AsciiArtEnum {
     TIE_FIGHTER("""
                 ,.
           "..-..pf.
          -L   ..#'
        .+_L  ."]#
        ,'j' .+.j`                 -'.__..,.,p.
       _~ #..<..0.                 .J-.``..._f.
      .7..#_.. _f.                .....-..,`4'
      ;` ,#j.  T'      ..         ..J....,'.j`
     .` .."^.,-0.,,,,yMMMMM,.    ,-.J...+`.j@
    .'.`...' .yMMMMM0M@^=`""g.. .'..J..".'.jH
    j' .'1`  q'^)@@#"^".`"='BNg_...,]_)'...0-
   .T ...I. j"    .'..+,_.'3#MMM0MggCBf....F.
   j/.+'.{..+       `^~'-^~~'''"''''?'"``'1`
   .... .y.}                  `.._-:`_...jf
   g-.  .Lg'                 ..,..'-....,'.
  .'.   .Y^                   .....',].._f
  ......-f.                  .-,,.,.-:--&`
                             .`...'..`_J`
                             .~......'#'
                             '..,,.,_]`
                             .L..`..``.
""");

     public final String artPiece;
    AsciiArtEnum(String artPiece) {
        this.artPiece = artPiece;
    }
}
