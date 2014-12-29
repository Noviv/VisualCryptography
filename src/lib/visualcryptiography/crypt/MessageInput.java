package lib.visualcryptiography.crypt;

import java.util.ArrayList;
import java.util.Iterator;
import lib.visualcryptiography.util.CryptIO;

public class MessageInput implements Iterator {

    private final String message;
    private ArrayList<String> data;
    private int position;

    public MessageInput(String s) {
        message = s;
        data = new ArrayList<>();
        position = 0;
        for (int i = 0; i < message.length(); i++) {
            if ((int) message.charAt(i) < 510) {
                data.add("" + (int) message.charAt(i));
            } else {
                CryptIO.notifyErr("Attempted to add character with ASCII value greater than 510.");
            }
        }
    }

    @Override
    public boolean hasNext() {
        return position < data.size();
    }

    @Override
    public Object next() {
        return data.get(position++);
    }

    public int getEncodedNext() {
        return Integer.parseInt("" + next());
    }

    public String getRaw() {
        return message;
    }

    public ArrayList<String> getASCIIValues() {
        return data;
    }
}
