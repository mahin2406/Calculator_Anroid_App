package edu.jsu.mcis.cs408.calculator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractController implements PropertyChangeListener {

    private final List<AbstractModel> models = new ArrayList<>();
    private final List<AbstractView> views = new ArrayList<>();

    public void addModel(AbstractModel model) {
        models.add(model);
        model.addPropertyChangeListener(this);
    }

    public void removeModel(AbstractModel model) {
        models.remove(model);
        model.removePropertyChangeListener(this);
    }

    public void addView(AbstractView view) {
        views.add(view);
    }

    public void removeView(AbstractView view) {
        views.remove(view);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (AbstractView v : views) {
            v.modelPropertyChange(evt);
        }
    }
}