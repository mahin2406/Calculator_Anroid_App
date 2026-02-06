package edu.jsu.mcis.cs408.calculator;

import android.view.View;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import edu.jsu.mcis.cs408.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int KEYS_WIDTH = 5;
    private static final int KEYS_HEIGHT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //call initLayout()
        initLayout();
    }

    private void initLayout() {
        ConstraintLayout layout = binding.mainLayout;
        String[] buttonTextArray = getResources().getStringArray(R.array.button_text);
        String[] buttonTagsArray = getResources().getStringArray(R.array.button_tags);

        int[][] horizontals = new int[KEYS_HEIGHT][KEYS_WIDTH];
        int[][] verticals = new int[KEYS_WIDTH][KEYS_HEIGHT];

        TextView display = new TextView(this);
        display.setId(View.generateViewId());
        display.setText(getString(R.string.display_placeholder));
        display.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48f);
        display.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        layout.addView(display);

        int buttonIndex = 0;
        for (int row = 0; row < KEYS_HEIGHT; ++row) {
            for (int col = 0; col < KEYS_WIDTH; ++col) {
                Button button = new Button(this);
                button.setId(View.generateViewId());
                button.setText(buttonTextArray[buttonIndex]);
                button.setTag(buttonTagsArray[buttonIndex]);
                button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
                layout.addView(button);

                horizontals[row][col] = button.getId();
                verticals[col][row] = button.getId();
                buttonIndex++;
            }
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        // display constraints
        constraintSet.connect(display.getId(), ConstraintSet.TOP, R.id.guideNorth, ConstraintSet.TOP);
        constraintSet.connect(display.getId(), ConstraintSet.START, R.id.guideWest, ConstraintSet.START);
        constraintSet.connect(display.getId(), ConstraintSet.END, R.id.guideEast, ConstraintSet.END);
        constraintSet.constrainWidth(display.getId(), ConstraintSet.MATCH_CONSTRAINT);

        //calculate 8dp margin
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

        //create chains - this handles the guidelines
        for (int row = 0; row < KEYS_HEIGHT; row++) {
            constraintSet.createHorizontalChain(
                    R.id.guideWest, ConstraintSet.LEFT,
                    R.id.guideEast, ConstraintSet.RIGHT,
                    horizontals[row], null, ConstraintSet.CHAIN_SPREAD
            );
        }

        for (int col = 0; col < KEYS_WIDTH; col++) {
            constraintSet.createVerticalChain(
                    display.getId(), ConstraintSet.BOTTOM,
                    R.id.guideSouth, ConstraintSet.BOTTOM,
                    verticals[col], null, ConstraintSet.CHAIN_SPREAD
            );
        }

        // apply shared button properties
        for (int i = 0; i < buttonTextArray.length; i++) {
            int buttonId = horizontals[i / KEYS_WIDTH][i % KEYS_WIDTH];
            constraintSet.constrainWidth(buttonId, ConstraintSet.MATCH_CONSTRAINT);
            constraintSet.constrainHeight(buttonId, ConstraintSet.MATCH_CONSTRAINT);
            constraintSet.setMargin(buttonId, ConstraintSet.TOP, margin);
            constraintSet.setMargin(buttonId, ConstraintSet.BOTTOM, margin);
            constraintSet.setMargin(buttonId, ConstraintSet.START, margin);
            constraintSet.setMargin(buttonId, ConstraintSet.END, margin);
        }

        constraintSet.applyTo(layout);
    }
}