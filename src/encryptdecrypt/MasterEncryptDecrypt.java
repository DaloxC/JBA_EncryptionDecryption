/* Write a code that allows you to encrypt or decrypt messages from one recipient to another.
 Add privacy to messages used on the Internet

Requirements:
The message must have a numeric code.
The message must allow its decryption.
The message should allow the user to choose the algorithm-specific encryption: Cesar or Unicode.

DaloxC
*/
package encryptdecrypt;

import java.io.*;
import java.util.*;
// Block to Algorithm
abstract class Algorithm {

    abstract String encrypt(String message, int key);

    abstract String decrypt(String message, int key);
}
// Class to UNICODE ALGORITHM
class UnicodeAlgorithm extends Algorithm {

    // Block to ENCRYPT logic
    public String encrypt(String message, int key) {
        StringBuilder cipherText = new StringBuilder();

        for (char c : message.toCharArray()) {
            cipherText.append((char) (c + key));
        }

        return cipherText.toString();
    }
    // Block to DECRYPT logic
    public String decrypt(String message, int key) {
        return encrypt(message, -key);
    }
}
// Class to CHANGE ALGORITHM
class ChangeAlgorithm extends Algorithm {
    // Block to ENCRYPT change
    public String encrypt(String message, int key) {
        StringBuilder plainText = new StringBuilder();

        for (char c : message.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                int shift = Character.isUpperCase(c) ? 65 : 97;
                plainText.append((char) (modulo(c - shift + key) + shift));
            } else {
                plainText.append(c);
            }
        }

        return plainText.toString();
    }
    // Block to DECRYPT change
    public String decrypt(String message, int key) {
        return encrypt(message, -key);
    }
    // Block to MODULO
    private static int modulo(int x) {
        return (x % 26 + 26) % 26;
    }
}
// Class to ENIGMA
class Enigma {
    int key = 0;
    String mode = "enc";
    String data = "";
    String inPath = "";
    String outPath = "";
    Algorithm algorithm = new ChangeAlgorithm();
    // Constructor and options.
    public void execute() {
        readData();

        switch (this.mode) {
            case "enc":
                String cipherText = algorithm.encrypt(data, key);
                output(cipherText, outPath);
                break;
            case "dec":
                String plainText = algorithm.decrypt(data, key);
                output(plainText, outPath);
                break;
            default:
                System.err.println("unknown mode");
                System.exit(1);
        }
    }
    // Block to OUTPUT TEXT.
    private static void output(String message, String outPath) {
        if (outPath.equals("")) {
            System.out.println(message);
        } else {
            File file = new File(outPath);

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(message);
            } catch (IOException e) {
                System.err.println("can't write to " + outPath);
                System.exit(1);
            }
        }
    }
    // Block to READ DATA.
    private void readData() {
        if (data.equals("")) {
            String fileName = inPath;
            File file = new File(fileName);
            try (Scanner scanner = new Scanner(file)) {
                data = scanner.nextLine();
            } catch (FileNotFoundException e) {
                System.err.println(fileName + " not found");
                System.exit(1);
            }
        }
    }
    // Block to ARGS logic
    public void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            switch (args[i]) {
                case "-alg":
                    switch (args[i + 1]) {
                        case "shift":
                            algorithm = new ChangeAlgorithm();
                            break;
                        case "unicode":
                            algorithm = new UnicodeAlgorithm();
                            break;
                        default:
                            System.err.println("unknown algorithm " + args[i + 1]);
                            System.exit(1);
                    }
                    break;
                case "-mode":
                    mode = args[i + 1];
                    break;
                case "-key":
                    key = Integer.parseInt(args[i + 1]);
                    break;
                case "-in":
                    inPath = args[i + 1];
                    break;
                case "-data":
                    data = args[i + 1];
                    break;
                case "-out":
                    outPath = args[i + 1];
                    break;
                default:
                    System.err.println("unknown argument");
                    System.exit(1);
            }
        }
    }
}
// Class MAIN
public class MasterEncryptDecrypt {
    public static void main(String[] args) {
        Enigma enigma = new Enigma();
        enigma.parseArgs(args);
        enigma.execute();
    }
}

