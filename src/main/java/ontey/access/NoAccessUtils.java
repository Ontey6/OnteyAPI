package ontey.access;

import javax.swing.JOptionPane;

/**
 * More like a util, made for the specific scenario that a plugin JAR is run by a user.
 * This includes the java command in the terminal and right-clicking the JAR.
 *
 * <h1>How to implement</h1>
 * Just 2 simple steps do the trick to prevent the user from making yet another stupid mistake
 *
 * <h2>1. Creating a Main class</h2>
 * For this to work, you need a plain java main class.
 * <p>
 * That means a class with a {@code main(String[] args)} method.
 * <p>
 * In that class' {@code main} method, simply call {@link #exit()}.
 *
 * <h2>2. Add Main Class to the MANIFEST</h2>
 * Then you have to specify the main-class of the jar.
 * <p>
 * That is done by adding it into the JAR's manifest.
 * <p>
 * Gradle example:
 * <pre>{@code
 * // after the dependencies in global scope
 * jar.manifest.attributes("Main-Class": "ontey.OnteyAPIJavaMain")
 * }</pre>
 */

public class NoAccessUtils {
   
   /**
    * Shows an error popup and {@link System#exit exits}/shuts down the VM running this program
    */
   
   public static void exit() {
      JOptionPane.showMessageDialog(
        null,
        "This file is a Minecraft plugin.\nPlease put it into the server's \"\\plugins\\\" directory.",
        "Invalid Launch",
        JOptionPane.ERROR_MESSAGE
      );
      System.exit(1);
   }
}
