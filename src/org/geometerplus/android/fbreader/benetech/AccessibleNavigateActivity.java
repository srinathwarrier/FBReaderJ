package org.geometerplus.android.fbreader.benetech;

import org.benetech.android.R;
import org.geometerplus.fbreader.fbreader.FBReaderApp;
import org.geometerplus.zlibrary.text.view.ZLTextView;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author roms
 */
public class AccessibleNavigateActivity extends Activity {
    
    private EditText searchTermEditText;
    private Activity parentActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = this;

        setContentView(R.layout.bookshare_dialog);
        setTitle(getResources().getString(R.string.navigate_dialog_title));
        searchTermEditText = (EditText)findViewById(R.id.bookshare_dialog_search_edit_txt);
        searchTermEditText.setContentDescription(getResources().getString(R.string.navigate_dialog_label));
        searchTermEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextView dialog_search_title = (TextView) findViewById(R.id.bookshare_dialog_search_txt);
        TextView dialog_example_text = (TextView) findViewById(R.id.bookshare_dialog_search_example);
        Button dialog_ok = (Button)findViewById(R.id.bookshare_dialog_btn_ok);
        Button dialog_cancel = (Button)findViewById(R.id.bookshare_dialog_btn_cancel);
        
        final ZLTextView textView = (ZLTextView) FBReaderApp.Instance().getCurrentView();
        final ZLTextView.PagePosition pagePosition = textView.pagePosition();

        final int currentPage = pagePosition.Current;
        final int pagesNumber = pagePosition.Total;
        dialog_search_title.setText(getResources().getString(R.string.navigate_dialog_label));
        dialog_example_text.setText(getResources().getString(R.string.navigate_dialog_example, currentPage, pagesNumber));
        searchTermEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    int page;
                    try {
                        page = Integer.parseInt(searchTermEditText.getText().toString().trim());
                    } catch (NumberFormatException nfe) {
                        parentActivity.finish();
                        return true;
                    }
                    gotoPage(page);
                    return true;
                }
                return false;
            }
        });
        dialog_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int page;
                try {
                    page = Integer.parseInt(searchTermEditText.getText().toString().trim());
                } catch (NumberFormatException nfe) {
                    return;
                }
                gotoPage(page);

            }
        });
        dialog_cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                parentActivity.finish();
            }
        });
        searchTermEditText.requestFocus();
    }

    
    private void gotoPage(int page) {
        final ZLTextView view = (ZLTextView)FBReaderApp.Instance().getCurrentView();
        if (page == 1) {
            view.gotoHome();
        } else {
            view.gotoPage(page);
        }
        FBReaderApp.Instance().getViewWidget().reset();
        FBReaderApp.Instance().getViewWidget().repaint();
        finish();
    }
    
}
