package blockchain;

import java.util.Random;

public class RandomMsgGenerator {

    private static final Random random = new Random();

    private static final String[] MESSAGES = {
            "Hello!",
            "How are you?",
            "Have a nice day!",
            "What's up?",
            "Good morning!",
            "Take care!",
            "Stay positive!",
            "Enjoy your weekend!",
            "Keep smiling!",
            "You're awesome!"
    };

    public String generate() {
        int index = random.nextInt(MESSAGES.length);
        return MESSAGES[index];
    }
}

