package lib.crypt;

import java.util.ArrayList;
import java.util.Iterator;
import lib.util.CryptIO;

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
                CryptIO.notifyErr("Attempted to add character with ASCII value greater than 510: " + message.charAt(i));
            }
        }
        CryptIO.notifyResult("Message Length: " + s.length(), false);
    }

    @Override
    public boolean hasNext() {
        return position < data.size();
    }

    @Override
    public Object next() {
        return data.get(position++);
    }

    public void back() {
        position--;
    }

    public void restart() {
        position = 0;
    }

    public int getEncodedNext() {
        return Integer.parseInt("" + next());
    }

    public int getNextCharInt() {
        return (int) ((String) next()).charAt(0);
    }

    public double getAverageASCII() {
        double average = 0.0;
        for (String s : data) {
            average += (int) s.charAt(0);
        }
        return average / data.size();
    }

    public ArrayList<String> getDataArray() {
        return data;
    }

    public String getRaw() {
        return message;
    }
    
    public int getPosition() {
        return position;
    }

    public ArrayList<String> getASCIIValues() {
        return data;
    }
}
