package com.rukbal.db.utils;

public class CommonConst {
    public static final int SIZE_XXS = 15, SIZE_XS = 31, SIZE_S = 63, SIZE_M = 127, SIZE_L = 255, SIZE_XL = 511, SIZE_XXL = 1023;
    public enum SIZE {
//        static final short PAYLOAD = 102, ACK = 103, PAYLOAD_AND_ACK = 104;
        XXS((short) 15),
        XS((short) 31),
        S((short) 63),
        M((short) 127),
        L((short) 255),
        XL((short) 511),
        XXL((short) 1023);

        private final int value;
        SIZE(short i) {
            this.value=i;
        }
        public final int getValue() {
            return value;
        }
    }
}
