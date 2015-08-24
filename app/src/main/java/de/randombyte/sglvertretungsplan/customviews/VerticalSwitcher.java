package de.randombyte.sglvertretungsplan.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import de.randombyte.sglvertretungsplan.R;

/**
 * Something like a NumberPicker placed horizontally.
 */
public class VerticalSwitcher extends FrameLayout {

    private String[] entries = {"First", "Second", "Third"}; //default entries if isInEditMode
    private int index = 0;

    private ImageView increaseButton;
    private ImageView decreaseButton;
    private TextView valueTextView;

    public VerticalSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.VerticalSwitcher, 0, 0);

        float textSize;

        try {
            if (!isInEditMode()) {
                entries = context.getResources()
                        .getStringArray(attributes.getResourceId(R.styleable.VerticalSwitcher_entries, 0));
            }
            textSize = attributes.getDimension(R.styleable.VerticalSwitcher_android_textSize, 16);
        } finally {
            attributes.recycle();
        }

        //View "creation"
        /*setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));*/

        View rootView = LayoutInflater
                .from(context).inflate(R.layout.customview_vertical_switcher, this, false);

        increaseButton = (ImageView) rootView.findViewById(R.id.increase_button);
        decreaseButton = (ImageView) rootView.findViewById(R.id.decrease_button);
        valueTextView = (TextView) rootView.findViewById(R.id.selected_entry_textview);

        addView(rootView);

        increaseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                index--;
                updateView();
            }
        });
        decreaseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                updateView();
            }
        });

        valueTextView.setTextSize(textSize);
        updateView();
    }

    public String[] getEntries() {
        return entries;
    }

    public void setEntries(String[] entries) {
        this.entries = entries;
        updateView();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        updateView();
    }

    /**
     * Shortcut
     */
    public String getSelectedEntry() {
        return entries[index];
    }

    private void updateView() {
        valueTextView.setText(entries[index]);
        increaseButton.setVisibility(index > 0 ? VISIBLE : INVISIBLE);
        decreaseButton.setVisibility(index < entries.length - 1 ? VISIBLE : INVISIBLE);
    }
}