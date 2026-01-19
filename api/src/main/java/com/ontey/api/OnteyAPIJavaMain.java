package com.ontey.api;

import javax.swing.JOptionPane;

/**
 * The code that is triggered when the jar is executed by right-clicking it (not run by paper)
 */

public class OnteyAPIJavaMain {
   
   public static void main(String[] args) {
      JOptionPane.showMessageDialog(
        null,
        "This file is a minecraft plugin.\nPlease put it into the server's \"plugins\" directory.",
        "Invalid Launch",
        JOptionPane.ERROR_MESSAGE
      );
      System.exit(1);
   }
}
