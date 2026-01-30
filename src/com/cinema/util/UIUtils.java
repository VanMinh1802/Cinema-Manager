package com.cinema.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class UIUtils {

  public interface Task<T> {
    T execute() throws Exception;
  }

  public interface Callback<T> {
    void onSuccess(T result);
  }

  /**
   * Executes a background task with a global wait cursor.
   * 
   * @param <T>       Result type
   * @param component Any component in the window (used to find root)
   * @param task      Logic to run in background
   * @param onSuccess Logic to run on EDT after success
   */
  public static <T> void runAsync(Component component, Task<T> task, Callback<T> onSuccess) {
    // Find the top-level window to set the cursor
    // Standard System Cursor Logic
    Window win;
    if (component instanceof Window) {
      win = (Window) component;
    } else {
      win = SwingUtilities.getWindowAncestor(component);
    }

    if (win != null) {
      win.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    final Window fWin = win;

    new SwingWorker<T, Void>() {
      @Override
      protected T doInBackground() throws Exception {
        // No artificial delay for maximum responsiveness

        return task.execute();
      }

      @Override
      protected void done() {
        try {
          if (fWin != null) {
            fWin.setCursor(Cursor.getDefaultCursor());
          }
          T result = get(); // Check for exceptions
          onSuccess.onSuccess(result);
        } catch (Exception e) {
          if (win != null) {
            win.setCursor(Cursor.getDefaultCursor());
          }
          e.printStackTrace();
          // Optional: Global error handling
          if (e.getCause() != null) {
            JOptionPane.showMessageDialog(component, "Error: " + e.getCause().getMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);
          } else {
            JOptionPane.showMessageDialog(component, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    }.execute();
  }
}
