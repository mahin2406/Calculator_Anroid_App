package edu.jsu.mcis.cs408.calculator;

import java.beans.PropertyChangeEvent;

public interface AbstractView {
    void modelPropertyChange(PropertyChangeEvent evt);
}
