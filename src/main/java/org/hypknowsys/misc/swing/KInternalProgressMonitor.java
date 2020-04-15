/*
 * Copyright (C) 2000-2005, Henner Graubitz, Myra Spiliopoulou, Karsten 
 * Winkler. All rights reserved.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.hypknowsys.misc.swing;

import java.io.*;
import java.awt.*;
import java.beans.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * @version 2.1, 15 August 2003
 * @author Karsten Winkler
 */
  
public class KInternalProgressMonitor extends Object {
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private KInternalFrame  dialog;
  private KDesktopPane    desktop;
  private JOptionPane     pane;
  private JProgressBar    myBar;
  private JLabel          noteLabel;
  private Component       parentComponent;
  private String          note;
  private Object[]        cancelOption = null;
  private Object          message;
  private int             min;
  private int             max;
  private int             v;
  private int             lastDisp;
  private int             reportDelta;
  // support for steps
  private String          StepNote;
  private int             MinStep;
  private int             MaxStep;
  private int             CurrentStep;
  private JLabel          StepNoteLabel;
  private JProgressBar    StepBar;
  
  
  private boolean        IsIndeterminate = false;
  private boolean        IsModal = false;
  private String Title = null;
  private String CancelOption = null;
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## auxiliary attributes  */
  /* ########## ########## ########## ########## ########## ######### */

  private transient StringBuffer TmpStringBuffer = null;

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constants  */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## constructors  */
  /* ########## ########## ########## ########## ########## ######### */

  public KInternalProgressMonitor(Component parentComponent, Object message, 
  String note, int min, int max, boolean pIndeterminate, String pTitle, 
  String pCancelOption, boolean pShowImmediately, KDesktopPane pDesktop) {
    
    this(parentComponent, message, note, min, max, pIndeterminate, pTitle, 
    pCancelOption, pShowImmediately, pDesktop, null, 0, 0);
        
  }
  
  /* ########## ########## ########## ########## ########## ######### */

  public KInternalProgressMonitor(Component parentComponent, Object message, 
  String note, int min, int max, boolean pIndeterminate, String pTitle, 
  String pCancelOption, boolean pShowImmediately, KDesktopPane pDesktop,
  String pStepNote, int pMinStep, int pMaxStep) {
        
    this.min = min;
    this.max = max;
    this.parentComponent = parentComponent;
    this.IsIndeterminate = pIndeterminate;
    this.Title = pTitle;
    this.CancelOption = pCancelOption;
    this.IsModal = false;
    this.desktop = pDesktop;
    
    cancelOption = new Object[1];
    if (this.CancelOption == null)
      cancelOption[0] = new String("Cancel");
    else 
      cancelOption[0] = this.CancelOption;
    
    reportDelta = (max - min) / 100;
    if (reportDelta < 1) reportDelta = 1;
    v = min;
    this.message = message;
    this.note = note;
    this.StepNote = pStepNote;
    this.MinStep = pMinStep;
    this.MaxStep = pMaxStep;
    this.CurrentStep = pMinStep;
    
    if (pShowImmediately)
      this.createDialog();

  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## accessor methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## mutator methods */
  /* ########## ########## ########## ########## ########## ######### */

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## standard methods */
  /* ########## ########## ########## ########## ########## ######### */

  public String toString() { 

    TmpStringBuffer = new StringBuffer(1000);
    TmpStringBuffer.append(this.getClass().getName());
    
    return TmpStringBuffer.toString();
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  /* ########## interface methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## public methods */
  /* ########## ########## ########## ########## ########## ######### */

  public void setProgress(int nv) {
    
    if (myBar != null && this.IsIndeterminate)
      myBar.setIndeterminate(false);
    else
      this.IsIndeterminate = false;
    
    v = nv;
    if (nv >= lastDisp + reportDelta || nv < lastDisp) {
      lastDisp = nv;
      if (myBar != null)
        myBar.setValue(nv);
      else 
        this.createDialog();
    }
    
  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setStep(int pNewStep) {
    
    CurrentStep = pNewStep;
    if (StepBar != null)
      StepBar.setValue(CurrentStep);
    
  } 
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void close() {
    
    if (dialog != null) {
      dialog.setVisible(false);
      dialog.dispose();
      dialog = null;
      pane = null;
      myBar = null;
      
    }
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getMinimum() {
    
    return min;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getMinimumStep() {
    
    return MinStep;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setMinimum(int m) {
    
    min = m;
    
  }

  /* ########## ########## ########## ########## ########## ######### */
  
  public void setMinimumStep(int pMinStep) {
    
    MinStep = pMinStep;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getMaximum() {
    
    return max;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public int getMaximumStep() {
    
    return MaxStep;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setMaximum(int m) {
    
    max = m;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setMaximumStep(int pMaxStep) {
    
    MaxStep = pMaxStep;
    
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  public boolean isCanceled() {
    
    if (pane == null) return false;
    Object v = pane.getValue();
    return ((v != null) &&
    (cancelOption.length == 1) &&
    (v.equals(cancelOption[0])));
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setNote(String note) {
    
    this.note = note;
    if (noteLabel != null) {
      noteLabel.setText(note);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setStepNote(String pStepNote) {
    
    this.StepNote = pStepNote;
    if (StepNoteLabel != null) {
      StepNoteLabel.setText(pStepNote);
    }
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getNote() {
    
    return note;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public String getStepNote() {
    
    return StepNote;
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  
  public void setIndeterminate(boolean pIsIndeterminate) {
   
    this.IsIndeterminate = pIsIndeterminate;
    if (myBar != null)
      myBar.setIndeterminate(pIsIndeterminate);
    else 
      this.createDialog();
    
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## protected methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## private methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  private class ProgressOptionPane extends JOptionPane {

    ProgressOptionPane(Object messageList) {
      
      super(messageList,
      JOptionPane.INFORMATION_MESSAGE,
      JOptionPane.DEFAULT_OPTION,
      null,
      KInternalProgressMonitor.this.cancelOption,
      null);
      
    }
    
    public int getMaxCharactersPerLineCount() {
      
      return 60;
      
    }  
    
    public KInternalFrame createInternalFrame(String title) {
      
      Frame frame = JOptionPane.getFrameForComponent(parentComponent);
      final JDialog dialog4Size = new JDialog(frame, title, false);
      Container contentPane = dialog4Size.getContentPane();
      contentPane.setLayout(new BorderLayout());
      contentPane.add(this, BorderLayout.CENTER);
      dialog4Size.pack();
      
      final KInternalFrame dialog = new KInternalFrame( 
      title, true, false, false, false, dialog4Size.getPreferredSize().width, 
      dialog4Size.getPreferredSize().height, this);
      dialog.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
      
      // bug: work around to force initial focus in case of multiple steps
      Component[] allComponents = this.getComponents();
      for (int i = 0; i < allComponents.length; i++) {
        if (allComponents[i] instanceof JPanel) {
          Component[] myComponents = ( (JPanel)allComponents[i] ).getComponents();
          for (int j = 0; j < myComponents.length; j++) {
            if (myComponents[j] instanceof JButton) {
              dialog.setInitialFocusComponent((JButton)myComponents[j]);
              dialog.setDefaultButton((JButton)myComponents[j]);
              break;
            }
          }
        }
        if (allComponents[i] instanceof JButton) {
          dialog.setInitialFocusComponent((JButton)allComponents[i]);
          dialog.setDefaultButton((JButton)allComponents[i]);
          break;
        }
      }
      
      dialog.addInternalFrameListener(new InternalFrameAdapter() {
        boolean gotFocus = false;        
        public void internalFrameClosing(InternalFrameEvent we) {
          setValue(cancelOption[0]);
          super.internalFrameClosing(we);
        }        
        public void internalFrameActivated(InternalFrameEvent we) {
          if (!gotFocus) {
            selectInitialValue();
            gotFocus = true;
          }
        }
        
      });
      
      addPropertyChangeListener(new PropertyChangeListener() {
        
        public void propertyChange(PropertyChangeEvent event) {
          if(dialog.isVisible() &&
          event.getSource() == ProgressOptionPane.this &&
          (event.getPropertyName().equals(VALUE_PROPERTY) ||
          event.getPropertyName().equals(INPUT_VALUE_PROPERTY))){
            dialog.setVisible(false);
            dialog.dispose();
          }
        }
      });
      return dialog;
      
    }
  }
    
  /* ########## ########## ########## ########## ########## ######### */
  
  private void createDialog() {
    
    myBar = new JProgressBar();
    myBar.setIndeterminate(this.IsIndeterminate);
    myBar.setMinimum(min);
    myBar.setMaximum(max);
    myBar.setValue(min);
    if (note != null) noteLabel = new JLabel(note);
    if (StepNote != null) {
      StepBar = new JProgressBar();
      StepBar.setIndeterminate(false);
      StepBar.setMinimum(MinStep);
      StepBar.setMaximum(MaxStep);
      StepBar.setValue(MinStep);
      StepNoteLabel = new JLabel(StepNote);
      pane = new ProgressOptionPane(new Object[] {message, 
      StepNoteLabel, StepBar, noteLabel, myBar});
    }
    else {
      pane = new ProgressOptionPane(new Object[] {message, noteLabel, myBar});      
    }
    dialog = ( (ProgressOptionPane)pane ).createInternalFrame(this.Title);
    dialog.show();          
    desktop.addCenter(dialog);
   
  }
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## static methods */
  /* ########## ########## ########## ########## ########## ######### */
  
  /* ########## ########## ########## ########## ########## ######### */
  /* ########## main method for debugging purposes  */
  /* ########## ########## ########## ########## ########## ######### */

  public static void main(String args[]) {}
  
}