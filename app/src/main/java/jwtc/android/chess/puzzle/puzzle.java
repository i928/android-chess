package jwtc.android.chess.puzzle;

import jwtc.android.chess.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.InputType;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.content.SharedPreferences;

public class puzzle extends MyBaseActivity {

    /**
     * instances for the view and game of chess
     **/
    private ChessViewPuzzle _chessView;
    public static final String TAG = "puzzle";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.puzzle);

        this.makeActionOverflowMenuShown();

        _chessView = new ChessViewPuzzle(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.puzzle_topmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_help:
                intent = new Intent();
                intent.setClass(puzzle.this, HtmlActivity.class);
                intent.putExtra(HtmlActivity.HELP_MODE, "help_puzzle");
                startActivity(intent);
                return true;
//            case R.id.action_prefs:
//                intent = new Intent();
//                intent.setClass(puzzle.this, puzzlePrefs.class);
//                startActivity(intent);
//                return true;
            case R.id.action_jump:

                AlertDialog.Builder builder = new AlertDialog.Builder(puzzle.this);
                builder.setTitle(getString(R.string.title_puzzle_jump));
                final EditText input = new EditText(puzzle.this);
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                builder.setView(input);
                builder.setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            int num = Integer.parseInt(input.getText().toString());

                            if (num > 0 && num <= _chessView.numPuzzles()) {
                                _chessView.setPos(num - 1);
                                _chessView.play();
                                return;
                            }
                        } catch (Exception ex) {

                        }
                        doToast(getString(R.string.err_puzzle_jump));
                    }
                });


                AlertDialog alert = builder.create();
                alert.show();

                return true;
            case R.id.action_show:
                _chessView.showSolution();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     */
    @Override
    protected void onResume() {

        Log.i("puzzle", "onResume");

        SharedPreferences prefs = this.getPrefs();

        _chessView.OnResume(prefs);
        _chessView.updateState();

        super.onResume();
        //
    }


    @Override
    protected void onPause() {

        SharedPreferences.Editor editor = this.getPrefs().edit();
        _chessView.OnPause(editor);

        editor.commit();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        _chessView.OnDestroy();
        super.onDestroy();
    }

    public void flipBoard() {
        _chessView.flipBoard();
    }
}