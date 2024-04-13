package com.wendley.room.model;

/**
 *
 * @author Wendley S
 */
public class CodeGeneration {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static StringBuffer buffer;

    public static String generateRoomCode(Byte size) {
        buffer = new StringBuffer(size);
     
        for(Byte i = 0; i < size; i++){
            buffer.append(CHARS.charAt(getRandomNumber(0, 26)));
        }

        return buffer.toString();
    }
    
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
